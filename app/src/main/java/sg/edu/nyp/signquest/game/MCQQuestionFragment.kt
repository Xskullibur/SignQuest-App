package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_question_main.view.*
import kotlinx.android.synthetic.main.fragment_question_top.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.Score

class MCQQuestionFragment : GameExpandedAppBarFragment(), GameCountDownTimer {
    override val topContainerId: Int
        get() = R.layout.fragment_question_top
    override val mainContainerId: Int
        get() = R.layout.fragment_question_main

    private var motionLayoutView: MotionLayout? = null

    override val viewModel: MCQQuestionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {

            motionLayoutView = this?.findViewById(R.id.fragment_question_motion_layout)

            setGameCountDownTimer(this@MCQQuestionFragment)
            if(!viewModel.timerIsStarted){
                startCountDownTimer(5000)
            }

            viewModel.mcqQuestion.observe(viewLifecycleOwner) { question ->
                if(question != null) {
                    setGlossPicture(question.glossToBeAnswered)

                    val availableGlossary = listOf(question.glossToBeAnswered, *question.otherGlossaryChoice.toTypedArray()).shuffled()

                    mainContainerView.optionBtn1.text = availableGlossary[0].value
                    mainContainerView.optionBtn1.tag = availableGlossary[0]

                    mainContainerView.optionBtn2.text = availableGlossary[1].value
                    mainContainerView.optionBtn2.tag = availableGlossary[1]

                    mainContainerView.optionBtn3.text = availableGlossary[2].value
                    mainContainerView.optionBtn3.tag = availableGlossary[2]

                    mainContainerView.optionBtn4.text = availableGlossary[3].value
                    mainContainerView.optionBtn4.tag = availableGlossary[3]

                    mainContainerView.optionBtn1.setOnClickListener(::onOptionClick)
                    mainContainerView.optionBtn2.setOnClickListener(::onOptionClick)
                    mainContainerView.optionBtn3.setOnClickListener(::onOptionClick)
                    mainContainerView.optionBtn4.setOnClickListener(::onOptionClick)
                }
            }
        }
    }

    override fun onTick(millisUntilFinished: Long) {}

    override fun onFinish() {
        wrong()
    }

    private fun onOptionClick(view: View) {
        val gloss = view.tag as Gloss

        viewModel.glossToBeAnswered.observe(viewLifecycleOwner) {
            if (gloss.value == it.value) {
                correct(Score(1))
            } else {
                wrong()
            }
            viewModel.glossToBeAnswered.removeObservers(viewLifecycleOwner)
        }
    }

    private fun setGlossPicture(gloss: Gloss){
        signQuestionImageView.setImageResource(
            resources.getIdentifier(
                gloss.value.toLowerCase(),
                "drawable",
                requireContext().packageName
            )
        )
    }

    override fun exitFragmentTransition(onFinished: () -> Unit) {
        //Undo fragment enter transition
        motionLayoutView?.transitionToStart()
        motionLayoutView?.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                onFinished()
            }

        })
    }

    companion object {
        fun newInstance(): MCQQuestionFragment{
            return MCQQuestionFragment()
        }
    }

}