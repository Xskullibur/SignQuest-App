package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_question_main.view.*
import kotlinx.android.synthetic.main.fragment_question_top.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.GameProgress
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.MCQQuestion

const val ARGS_MCQ_GAME_PROGRESS = "args_mcq_fragment_game_progress"
const val ARGS_MCQ_QUESTION = "args_mcq_fragment_question"

class MCQQuestionFragment : GameExpandedAppBarFragment(), GameCountDownTimer {
    override val topContainerId: Int
        get() = R.layout.fragment_question_top
    override val mainContainerId: Int
        get() = R.layout.fragment_question_main

    private val viewModel: MCQQuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val gameProgress = it.get(ARGS_MCQ_GAME_PROGRESS) as GameProgress
            val question = it.get(ARGS_MCQ_QUESTION) as MCQQuestion

            viewModel.setGameProgress(gameProgress)
            this.setGameProgress(gameProgress)
            viewModel.setMCQQuestion(question)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState).apply {

            setGameCountDownTimer(this@MCQQuestionFragment)
            startCountDownTimer(5000)

            viewModel.question.observe(viewLifecycleOwner) { question ->

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

    override fun onTick(millisUntilFinished: Long) {

    }

    override fun onFinish() {
        wrong()
    }

    private fun onOptionClick(view: View) {
        val gloss = view.tag as Gloss

        viewModel.glossToBeAnswered.observe(viewLifecycleOwner) {
            if (gloss.value == it.value) {
                correct()
            } else {
                wrong()
            }
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

    companion object {
        fun newInstance(gameProgress: GameProgress, question: MCQQuestion): MCQQuestionFragment{
            return MCQQuestionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARGS_MCQ_GAME_PROGRESS, gameProgress)
                    putSerializable(ARGS_MCQ_QUESTION, question)
                }
            }
        }
    }

}