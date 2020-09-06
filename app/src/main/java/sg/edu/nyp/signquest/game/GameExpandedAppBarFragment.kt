package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.game_expanded_appbar.*
import kotlinx.android.synthetic.main.game_expanded_appbar.view.*
import sg.edu.nyp.signquest.databinding.GameExpandedAppbarBinding

interface GameCountDownTimer {
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}

abstract class GameExpandedAppBarFragment : Fragment() {

    abstract val topContainerId: Int
    abstract val mainContainerId: Int

    protected lateinit var topContainerView: View
    protected lateinit var mainContainerView: View

    private val viewModel: GameExpandedAppBarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.currentSecondsString.observe(viewLifecycleOwner){
            println(it)
        }

        return GameExpandedAppbarBinding.inflate(inflater, container, false).run {
            viewModel = this@GameExpandedAppBarFragment.viewModel
            lifecycleOwner = this@GameExpandedAppBarFragment


            backImageButton.setOnClickListener {
                this@GameExpandedAppBarFragment.findNavController().popBackStack()
            }
            root.topContainer.layoutResource = topContainerId
            topContainerView = root.topContainer.inflate()
            root.mainContainer.layoutResource = mainContainerId
            mainContainerView = root.mainContainer.inflate()
            root
        }
    }

    protected fun startCountDownTimer(totalMillisSeconds: Long, countDownInterval: Long = 1000){
        if(!viewModel.timerIsStarted)viewModel.startCountDownTimer(totalMillisSeconds, countDownInterval)
    }

    protected fun resetCountDownTimer(){
        if(viewModel.timerIsStarted)viewModel.resetCountDownTimer()
    }


}