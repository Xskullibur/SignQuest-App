package sg.edu.nyp.signquest.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.setMargins
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_player_to_sign_word_main.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.view.CircleProgressBar
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected

const val TOTAL_CHAR_MILLISECONDS = 10000
/**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignWordFragment : GameExpandedAppBarFragment(), OnSignDetected {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_word_main
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_main

    override val viewModel: PlayerToSignWordViewModel by viewModels()

    private lateinit var circleProgressBars: List<CircleProgressBar>

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
            viewModel.gloss.observe(viewLifecycleOwner) {
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

                    startNextCountDownCircleProgressBar()
                }
            }
        }
    }

    private fun createCircleProgressBar(char: Char) =
        CircleProgressBar(this.requireContext()).also {
            it.text = char.toString()
            it.color = getColor(this.requireContext(), R.color.colorAccent)
            it.textSize = 90
            it.progress = 0f
            it.strokeWidth = getDpInPixels(8f).toFloat()
            val layoutSize = getDpInPixels(100f)
            it.layoutParams = ViewGroup.MarginLayoutParams(layoutSize, layoutSize).apply { setMargins(5) }
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
        ValueAnimator.ofInt(milliSecondsLeft).apply {
            duration = milliSecondsLeft.toLong()
            addUpdateListener {
                type = CircleProgressBarStateType.Current
                milliSecondsLeft = duration.toInt() - (it.animatedValue as Int)
                circleProgressBar.setCircleProgressBarState(this@PlayerToSignWordFragment.requireContext(), this@startCountDown)
            }
            addListener (object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    type = CircleProgressBarStateType.InCorrect
                    circleProgressBar.setCircleProgressBarState(this@PlayerToSignWordFragment.requireContext(), this@startCountDown)

                    startNextCountDownCircleProgressBar()
                }
            })
            start()
        }
    }

    private fun startNextCountDownCircleProgressBar() {
        if (viewModel.haveNextIndex()) {
            viewModel.nextIndex()
            val state = viewModel.getCircleProgressBarStateForIndex(viewModel.currentIndex)
            state.startCountDown(circleProgressBars[viewModel.currentIndex])
        }
    }

    override fun signDetected(predictedValue: Char) {
        TODO("Not yet implemented")
    }
}

class CircleProgressBarState(
    var milliSecondsLeft: Int = TOTAL_CHAR_MILLISECONDS,
    var type: CircleProgressBarStateType = CircleProgressBarStateType.InComplete
)
enum class CircleProgressBarStateType{
    InComplete, Correct, InCorrect, Current
}
