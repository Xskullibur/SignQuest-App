package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_question_main.view.*
import kotlinx.android.synthetic.main.fragment_question_top.view.*
import kotlinx.android.synthetic.main.game_expanded_appbar.*
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
    var questionCount = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        score = 0
        count = 25
        //questionCountText.text = "Question $questionCount/10"
        setGameCountDownTimer(this)
        startCountDownTimer(6000)

        val view = super.onCreateView(inflater, container, savedInstanceState)

        var randomChoice = (1..4).random()
        awsner = questionPoolArray[(0..count).random()].toString()
        topContainerView.signQuestionImageView.setImageResource(R.drawable.y)

        while(questionTemArray.count() < 4){
            var letter = questionArray[(0..25).random()]
            if(!questionTemArray.contains(letter) && !questionTemArray.contains(awsner[0])){
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
        questionCount++

        view?.button1?.setOnClickListener{
            if(view?.button1?.text == awsner){
                score++
            }
            genQuestion()
        }

        view?.button2?.setOnClickListener{
            if(view?.button2?.text == awsner){
                score++
            }
            genQuestion()
        }

        view?.button3?.setOnClickListener{
            if(view?.button3?.text == awsner){
                score++
            }
            genQuestion()
        }

        view?.button4?.setOnClickListener{
            if(view?.button4?.text == awsner){
                score++
            }
            genQuestion()
        }

        return view
    }

    override fun onTick(millisUntilFinished: Long) {

    }

    override fun onFinish() {
        genQuestion()
    }

    fun genQuestion(){
        if(questionCount < 11){
            resetTimer()
            questionCountText.text = "Question $questionCount/10"
            questionTemArray.clear()
            var randomChoice = (1..4).random()

            awsner = questionPoolArray[(0..count).random()].toString()

            while(questionTemArray.count() < 4){
                var randomAnswer = (0..25).random()
                var letter = questionArray[randomAnswer]
                if(!questionTemArray.contains(letter) && !questionTemArray.contains(awsner[0])){
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
            questionCount++
        }else{
            questionCountText.text = "Score $score/10"
        }
    }

//    fun setImage(letter: Char){
//        if(letter == 'a'){
//            signQuestionImageView.setImageResource(R.drawable.a)
//        }else if(letter == 'b'){
//            signQuestionImageView.setImageResource(R.drawable.b)
//        }else if(letter == 'c'){
//            signQuestionImageView.setImageResource(R.drawable.c)
//        }else if(letter == 'd'){
//            signQuestionImageView.setImageResource(R.drawable.d)
//        }else if(letter == 'e'){
//            signQuestionImageView.setImageResource(R.drawable.e)
//        }else if(letter == 'f'){
//            signQuestionImageView.setImageResource(R.drawable.f)
//        }else if(letter == 'g'){
//            signQuestionImageView.setImageResource(R.drawable.g)
//        }else if(letter == 'h'){
//            signQuestionImageView.setImageResource(R.drawable.h)
//        }else if(letter == 'i'){
//            signQuestionImageView.setImageResource(R.drawable.i)
//        }else if(letter == 'j'){
//            signQuestionImageView.setImageResource(R.drawable.j)
//        }else if(letter == 'k'){
//            signQuestionImageView.setImageResource(R.drawable.k)
//        }else if(letter == 'i'){
//            signQuestionImageView.setImageResource(R.drawable.i)
//        }else if(letter == 'm'){
//            signQuestionImageView.setImageResource(R.drawable.m)
//        }else if(letter == 'n'){
//            signQuestionImageView.setImageResource(R.drawable.n)
//        }else if(letter == 'o'){
//            signQuestionImageView.setImageResource(R.drawable.o)
//        }else if(letter == 'p'){
//            signQuestionImageView.setImageResource(R.drawable.p)
//        }else if(letter == 'q'){
//            signQuestionImageView.setImageResource(R.drawable.q)
//        }else if(letter == 'r'){
//            signQuestionImageView.setImageResource(R.drawable.r)
//        }else if(letter == 's'){
//            signQuestionImageView.setImageResource(R.drawable.s)
//        }else if(letter == 'u'){
//            signQuestionImageView.setImageResource(R.drawable.u)
//        }else if(letter == 'v'){
//            signQuestionImageView.setImageResource(R.drawable.v)
//        }else if(letter == 'w'){
//            signQuestionImageView.setImageResource(R.drawable.w)
//        }else if(letter == 'x'){
//            signQuestionImageView.setImageResource(R.drawable.x)
//        }else if(letter == 'y'){
//            signQuestionImageView.setImageResource(R.drawable.y)
//        }else{
//            signQuestionImageView.setImageResource(R.drawable.z)
//        }
//    }

    fun resetTimer(){
        //questionCountText.text = "Question 01/20"
        resetCountDownTimer()
        startCountDownTimer(6000)
    }

}