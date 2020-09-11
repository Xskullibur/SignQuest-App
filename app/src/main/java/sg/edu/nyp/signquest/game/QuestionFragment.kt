package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.utils.AlertUtils.showAlert

class QuestionFragment : GameExpandedAppBarFragment(), GameCountDownTimer {
    override val topContainerId: Int
        get() = R.layout.fragment_question_top
    override val mainContainerId: Int
        get() = R.layout.fragment_question_main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setGameCountDownTimer(this)
        startCountDownTimer(3000)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onTick(millisUntilFinished: Long) {

    }

    override fun onFinish() {
        this.showAlert("Hello", "Hello World")
    }
}