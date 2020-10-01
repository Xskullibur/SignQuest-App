package sg.edu.nyp.signquest.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
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
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_player_to_sign_main.cameraView
import kotlinx.android.synthetic.main.fragment_player_to_sign_word_main.*
import kotlinx.android.synthetic.main.fragment_player_to_sign_word_top.view.*
import kotlinx.android.synthetic.main.game_expanded_appbar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.view.CircleProgressBar
import sg.edu.nyp.signquest.game.view.CustomPlayDialogFragment
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

    override val topContainerId: Int = R.layout.fragment_player_to_sign_word_top
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_word_main

    override val viewModel: PlayerToSignWordViewModel by viewModels()

    private lateinit var circleProgressBars: List<CircleProgressBar>

    private lateinit var cameraManager: CameraManager
    private lateinit var imageAnalyzer: SignLanguageImageAnalyzer

    private var currentValueAnimator: ValueAnimator? = null

    private var isComplete = false

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

            // Show Monster
            showMonsterGif()

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
            it.textColor = R.color.colorPrimaryDark
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
                    // Update Progress
                    type = CircleProgressBarStateType.InCorrect

                    if(view != null)
                    circleProgressBar.setCircleProgressBarState(this@PlayerToSignWordFragment.requireContext(), this@startCountDown)
                    circleProgressBar.progress = 100F
                    circleProgressBar.invalidate()

                    startNextCharacterCircleProgressBar()
                }
            })
            start()
        }

    }

    private fun startNextCharacterCircleProgressBar() {
        if (isComplete) return
        if (viewModel.haveNextIndex()) {
            viewModel.nextIndex()
            val state = viewModel.getCircleProgressBarStateForIndex(viewModel.currentIndex)
            state.startCountDown(circleProgressBars[viewModel.currentIndex])
        }else{
            if(cameraManager.cameraStarted)cameraManager.stopCamera()
            isComplete = true

            if (view != null)
            viewModel.gloss.observeOnce(viewLifecycleOwner, Observer {
                it?.let {

                    if (viewModel.numberOfCorrect == it.value.length) {
                        Glide.with(this@PlayerToSignWordFragment)
                                .asGif()
                                .load(R.drawable.yellow_monster_hit)
                                .into(topContainerView.monsterGif)
                    }

                    val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
                    val fragment = CustomPlayDialogFragment.newInstance(
                        title = "Well Done!",
                        subtitle = "${viewModel.numberOfCorrect} - ${it.value.length}"
                    ){ dialog ->
                        correct(viewModel.numberOfCorrect)
                        dialog.dismiss()
                    }

                    fragment.show(fragmentManager, CustomPlayDialogFragment.TAG)

                }
            })


        }
    }

    override fun signDetected(predictedValue: Char) {
        lifecycleScope.launch(Dispatchers.Main) {

            if (view != null)
            viewModel.gloss.observeOnce(viewLifecycleOwner, Observer {
                it?.let {
                    if(predictedValue == it.value[viewModel.currentIndex]){
                        // Update GIF
                        val listener = Glide.with(this@PlayerToSignWordFragment)
                                .asGif()
                                .load(R.drawable.yellow_monster_hit)
                                .listener(object : RequestListener<GifDrawable> {

                                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        resource?.setLoopCount(1)
                                        resource?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                                            override fun onAnimationEnd(drawable: Drawable?) {
                                                super.onAnimationEnd(drawable)

                                                showMonsterGif()

                                            }
                                        })
                                        return false
                                    }

                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean) = false

                                })
                                .into(topContainerView.monsterGif)

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

    private fun showMonsterGif() {
       lifecycleScope.launch(Dispatchers.Main) {
           Glide.with(this@PlayerToSignWordFragment)
               .asGif()
               .load(R.drawable.yellow_monster_idle)
               .into(topContainerView.monsterGif)
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
