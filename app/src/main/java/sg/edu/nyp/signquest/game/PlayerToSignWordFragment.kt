package sg.edu.nyp.signquest.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageAnalysis
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.setMargins
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_player_to_sign_main.*
import kotlinx.android.synthetic.main.fragment_player_to_sign_word_main.*
import kotlinx.android.synthetic.main.game_expanded_appbar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.view.CircleProgressBar
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected
import sg.edu.nyp.signquest.imageanalyzer.SignLanguageImageAnalyzer
import sg.edu.nyp.signquest.imageanalyzer.backend.ServerImageAnalyzerBackend
import sg.edu.nyp.signquest.utils.observeOnce
import java.util.concurrent.Executors

const val TOTAL_CHAR_MILLISECONDS = 10000
/**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignWordFragment : GameExpandedAppBarFragment(), CameraListener, OnSignDetected {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_word_main
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_main

    override val viewModel: PlayerToSignWordViewModel by viewModels()

    private lateinit var circleProgressBars: List<CircleProgressBar>

    private lateinit var cameraManager: CameraManager
    private lateinit var imageAnalyzer: SignLanguageImageAnalyzer

    private var currentValueAnimator: ValueAnimator? = null

    companion object {
        @JvmStatic
        fun newInstance() = PlayerToSignWordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).also {

            //Hide appbar timer
            it?.timerContainer?.visibility = View.INVISIBLE

            //Init camera
            cameraManager = CameraManager(this, this)
            cameraManager.requestPermission()

            viewModel.gloss.observeOnce(viewLifecycleOwner, Observer {
                it?.let {
                    val list = mutableListOf<CircleProgressBar>()
                    for ((i, c) in it.value.withIndex()){
                        val circleProgressBar = createCircleProgressBar(c)
                        val state = viewModel.getCircleProgressBarStateForIndex(i)
                        circleProgressBar.setCircleProgressBarState(this.requireContext(), state)
                        addCircleProgressBar(circleProgressBar)

                        list += circleProgressBar
                    }
                    circleProgressBars = list

                    startNextCharacterCircleProgressBar()
                }
            })
        }
    }

    private fun createCircleProgressBar(char: Char) =
        CircleProgressBar(this.requireContext()).also {
            it.textColor = Color.WHITE
            it.text = char.toString()
            it.color = getColor(this.requireContext(), R.color.colorAccent)
            it.textSize = 90
            it.progress = 0f
            it.strokeWidth = getDpInPixels(8f).toFloat()
            val layoutSize = getDpInPixels(80f)
            it.layoutParams = ViewGroup.MarginLayoutParams(layoutSize, layoutSize).apply { setMargins(15) }
            it.init()
        }

    private fun addCircleProgressBar(circleProgressBar: CircleProgressBar){
        words_linear_layout.addView(circleProgressBar)
    }

    private fun CircleProgressBar.setCircleProgressBarState(context: Context, circleProgressBarState: CircleProgressBarState){
        progress = (circleProgressBarState.milliSecondsLeft / TOTAL_CHAR_MILLISECONDS.toFloat() * 100)
        color = getColor(context, when(circleProgressBarState.type){
            CircleProgressBarStateType.InComplete -> R.color.circle_progress_bar_state_incomplete
            CircleProgressBarStateType.Correct -> R.color.circle_progress_bar_state_correct
            CircleProgressBarStateType.InCorrect -> R.color.circle_progress_bar_state_incorrect
            CircleProgressBarStateType.Current -> R.color.circle_progress_bar_state_current
        })
        this.invalidate()
    }

    private fun getDpInPixels(dps: Float): Int{
        val scale = this.requireContext().resources.displayMetrics.density
        return (dps * scale + 0.5f).toInt()
    }

    private fun CircleProgressBarState.startCountDown(circleProgressBar: CircleProgressBar){
        currentValueAnimator = ValueAnimator.ofInt(milliSecondsLeft).apply {
            duration = milliSecondsLeft.toLong()
            addUpdateListener {
                type = CircleProgressBarStateType.Current
                milliSecondsLeft = duration.toInt() - (it.animatedValue as Int)
                if(view != null)
                circleProgressBar.setCircleProgressBarState(this@PlayerToSignWordFragment.requireContext(), this@startCountDown)
            }
            addListener (object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    type = CircleProgressBarStateType.InCorrect

                    if(view != null)
                    circleProgressBar.setCircleProgressBarState(this@PlayerToSignWordFragment.requireContext(), this@startCountDown)

                    startNextCharacterCircleProgressBar()
                }
            })
            start()
        }

    }

    private fun startNextCharacterCircleProgressBar() {
        if (viewModel.haveNextIndex()) {
            viewModel.nextIndex()
            val state = viewModel.getCircleProgressBarStateForIndex(viewModel.currentIndex)
            state.startCountDown(circleProgressBars[viewModel.currentIndex])
        }else{
            if(cameraManager.cameraStarted)cameraManager.stopCamera()
            correct(viewModel.numberOfCorrect)
        }
    }

    override fun signDetected(predictedValue: Char) {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.gloss.observeOnce(viewLifecycleOwner, Observer {
                it?.let {
                    if(predictedValue == it.value[viewModel.currentIndex]){

                        //Add score
                        viewModel.numberOfCorrect++

                        val state = viewModel.getCircleProgressBarStateForIndex(viewModel.currentIndex)
                        state.type = CircleProgressBarStateType.Correct
                        circleProgressBars[viewModel.currentIndex].setCircleProgressBarState(this@PlayerToSignWordFragment.requireContext(), state)
                        currentValueAnimator?.pause()
                        startNextCharacterCircleProgressBar()
                    }
                }
            })
        }
    }

    override fun onCameraIsAccessible() {
        //Show camera on preview
        cameraManager.showCamera(cameraView.createSurfaceProvider()) {
            buildAnalyzer(this.requireContext())
        }
    }

    private fun buildAnalyzer(context: Context): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(
                    Executors.newSingleThreadExecutor(),
                    SignLanguageImageAnalyzer(context, ServerImageAnalyzerBackend(context)).apply {
                        this.onSignDetected = this@PlayerToSignWordFragment
                        this@PlayerToSignWordFragment.imageAnalyzer = this
                    })
            }
    }

}

class CircleProgressBarState(
    var milliSecondsLeft: Int = TOTAL_CHAR_MILLISECONDS,
    var type: CircleProgressBarStateType = CircleProgressBarStateType.InComplete
)
enum class CircleProgressBarStateType{
    InComplete, Correct, InCorrect, Current
}
