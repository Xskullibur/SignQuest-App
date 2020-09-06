package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.game_expanded_appbar.*
import kotlinx.android.synthetic.main.game_expanded_appbar.view.*
import sg.edu.nyp.signquest.R
import java.lang.IllegalStateException

interface GameCountDownTimer {
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}

abstract class GameExpandedAppBarFragment : Fragment() {

    abstract val topContainerId: Int
    abstract val mainContainerId: Int

    private var timer: CountDownTimer? = null
    private var gameCountDownTimer: GameCountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.game_expanded_appbar, container, false).also {
            it.backImageButton.setOnClickListener {
                this.findNavController().popBackStack()
            }
        }

        view.topContainer.layoutResource = topContainerId
        view.topContainer.inflate()
        view.mainContainer.layoutResource = mainContainerId
        view.mainContainer.inflate()

        return view
    }

    protected fun startCountDownTimer(totalMillisSeconds: Long, countDownInterval: Long = 1000){
        if(timer != null) throw IllegalStateException("Timer has already been started")
        timer = object: CountDownTimer(totalMillisSeconds, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timerTxtView.text = (millisUntilFinished / 1000).toString()
                gameCountDownTimer?.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                gameCountDownTimer?.onFinish()
            }
        }.start()
    }

    protected fun resetCountDownTimer(){
        if(timer == null) throw IllegalStateException("Timer has not been started")
        timer!!.cancel()

        timer = null

    }


}