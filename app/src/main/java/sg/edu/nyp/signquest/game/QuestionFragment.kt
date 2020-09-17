package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_question_main.view.*
import kotlinx.android.synthetic.main.fragment_question_top.*
import sg.edu.nyp.signquest.R


class QuestionFragment : GameExpandedAppBarFragment(), GameCountDownTimer {
    override val topContainerId: Int
        get() = R.layout.fragment_question_top
    override val mainContainerId: Int
        get() = R.layout.fragment_question_main

    var questionArray : MutableList<Char> = "abcdefghijklmnopqrstuvwxyz".toMutableList()
    var questionPoolArray : MutableList<Char> = "abcdefghijklmnopqrstuvwxyz".toMutableList()
    var awsner = ""
    var score = 0
    var count = 25
    var questionTemArray : MutableList<Char> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        score = 0
        count = 25
        setGameCountDownTimer(this)
        startCountDownTimer(6000)

        val view = super.onCreateView(inflater, container, savedInstanceState)

        var randomChoice = (1..4).random()
        awsner = questionPoolArray[(0..count).random()].toString()
        //signQuestionImageView.setImageResource(R.drawable.y)

        while(questionTemArray.count() < 4){
            var letter = questionArray[(0..25).random()]
            if(!questionTemArray.contains(letter)){
                questionTemArray.add(letter)
            }
        }

        view?.button1?.text = questionTemArray[0].toString()
        view?.button2?.text = questionTemArray[1].toString()
        view?.button3?.text = questionTemArray[2].toString()
        view?.button4?.text = questionTemArray[3].toString()


        if(randomChoice == 1){
            view?.button1?.text = awsner
        }
        else if(randomChoice == 2){
            view?.button2?.text = awsner
        }
        else if(randomChoice == 3){
            view?.button3?.text = awsner
        }
        else{
            view?.button4?.text = awsner
        }

        questionPoolArray.remove(awsner[0])
        count--
        return view
    }

    override fun onTick(millisUntilFinished: Long) {

    }

    override fun onFinish() {
        resetTimer()
        genQuestion()
    }

    fun genQuestion(){
        questionTemArray.clear()
        var randomChoice = (1..4).random()

        awsner = questionPoolArray[(0..count).random()].toString()

        while(questionTemArray.count() < 4){
            var randomAnswer = (0..25).random()
            var letter = questionArray[randomAnswer]
            if(!questionTemArray.contains(letter)){
                questionTemArray.add(letter)
            }
        }

        view?.button1?.text = questionTemArray[0].toString()
        view?.button2?.text = questionTemArray[1].toString()
        view?.button3?.text = questionTemArray[2].toString()
        view?.button4?.text = questionTemArray[3].toString()

        if(randomChoice == 1){
            view?.button1?.text = awsner
        }
        else if(randomChoice == 2){
            view?.button2?.text = awsner
        }
        else if(randomChoice == 3){
            view?.button3?.text = awsner
        }
        else{
            view?.button4?.text = awsner
        }

        questionPoolArray.remove(awsner[0])
        count--
    }



    fun resetTimer(){
        //questionCountText.text = "test"
        resetCountDownTimer()
        startCountDownTimer(6000)
    }

}