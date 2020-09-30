package sg.edu.nyp.signquest.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.game_expanded_appbar.view.*
import sg.edu.nyp.signquest.databinding.GameExpandedAppbarBinding
import sg.edu.nyp.signquest.game.gameobject.GameProgress

interface GameCountDownTimer {
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}

interface QuestionListener {
    fun onComplete(correct: Boolean, score: Int)
}

abstract class GameExpandedAppBarFragment : Fragment() {

    abstract val topContainerId: Int
    abstract val mainContainerId: Int

    protected lateinit var topContainerView: View
    protected lateinit var mainContainerView: View

    protected val gameViewModel: GameViewModel by activityViewModels()
    abstract val viewModel: GameExpandedAppBarViewModel

    private lateinit var questionListener: QuestionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        gameViewModel.gameProgress.observe(viewLifecycleOwner){
            viewModel.setGameProgress(it)
            view?.questionCountText?.text = "Question ${it.currentlyQuestionIndex+1}/${it.totalAmountOfQuestion}"
        }

        return GameExpandedAppbarBinding.inflate(inflater, container, false).run {
            viewModel = this@GameExpandedAppBarFragment.viewModel
            lifecycleOwner = this@GameExpandedAppBarFragment

            backImageButton.setOnClickListener {
                resetCountDownTimer()
                activity?.finish()
            }
            root.topContainer.layoutResource = topContainerId
            topContainerView = root.topContainer.inflate()
            root.mainContainer.layoutResource = mainContainerId
            mainContainerView = root.mainContainer.inflate()
            root
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is QuestionListener){
            questionListener = context
        }else{
            throw IllegalArgumentException("Parent context need to be an instance of QuestionListener")
        }

    }

    fun correct(score: Int = 1){
        Toast.makeText(activity?.applicationContext, "Correct!", Toast.LENGTH_SHORT).show()
        questionListener.onComplete(true, score)
        resetCountDownTimer()
    }

    fun wrong(){
        Toast.makeText(activity?.applicationContext, "Incorrect...", Toast.LENGTH_SHORT).show()
        questionListener.onComplete(false, 0)
        resetCountDownTimer()
    }

    protected fun startCountDownTimer(totalMillisSeconds: Long, countDownInterval: Long = 1000){
        if(!viewModel.timerIsStarted)viewModel.startCountDownTimer(totalMillisSeconds, countDownInterval)
    }

    protected fun resetCountDownTimer() {
        if (viewModel.timerIsStarted) viewModel.resetCountDownTimer()
    }

    protected fun setGameCountDownTimer(gameCountDownTimer: GameCountDownTimer){
        viewModel.setGameCountDownTimer(gameCountDownTimer)
    }

    open fun exitFragmentTransition(onFinished: () -> Unit){onFinished()}

}