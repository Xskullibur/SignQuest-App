package sg.edu.nyp.signquest.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.*
import sg.edu.nyp.signquest.utils.AlertUtils.showAlert
import kotlin.random.Random

const val ARGS_GAME_AVAILABLE_GLOSSARY = "args_game_available_glossary"
const val ARGS_GAME_TOTAL_QUESTION = "args_game_total_question"

private const val DEFAULT_TOTAL_QUESTION = 5

class GameActivity : AppCompatActivity(), QuestionListener {

    companion object {
        /**
         * Create an activity intent to start GameActivity
         * Usage:
         *      val intent = GameActivity.createActivityIntent(<context>, <glossary>)
         *      startActivity(intent)
         * @param context
         * @param glossary - the glossary available to test the user
         */
        fun createActivityIntent(context: Context, glossary: CharArray): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putCharArray(ARGS_GAME_AVAILABLE_GLOSSARY, glossary)
                })
            }
        }
    }
    //View models and current fragment
    private val viewModel: GameViewModel by viewModels()
    private var currentFragment: GameExpandedAppBarFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)
        //Get chars from bundle
        val availableChar = intent.extras?.getCharArray(ARGS_GAME_AVAILABLE_GLOSSARY)!!.toList()

        //Get how many question to generate
        val argTotalQuestion = intent.extras?.getInt(ARGS_GAME_TOTAL_QUESTION)
        val totalQuestion = if(argTotalQuestion != null && argTotalQuestion != 0) argTotalQuestion else DEFAULT_TOTAL_QUESTION

        //Create Game Progress only if there is no Game Progress already created, example screen rotation
        if(viewModel.gameProgress.value == null){
            //Create Game Progress for storing game state
            val gameProgress = GameProgress(totalQuestion, availableChar)
            viewModel.createGameProgress(gameProgress)
        }

        viewModel.gameProgress.observe(this){
            if(savedInstanceState == null) showQuestion(it, it.currentQuestion)
        }
        viewModel.isGameCompleted.observe(this){isGameCompleted ->
            if(isGameCompleted){
                viewModel.gameProgress.value?.let {gameProgress ->
                    showAlert(this,"Score", "${gameProgress.score}/${gameProgress.totalAmountOfQuestion}")
                    finish()
                }
            }
        }
    }

    private fun showQuestion(gameProgress: GameProgress, question: Question){
        val fragment = question.createFragment(gameProgress)
        if(this.currentFragment == null){
            replaceCurrentQuestionFragment(fragment)
        }else{
            this.currentFragment?.exitFragmentTransition {
                this.currentFragment = null
                replaceCurrentQuestionFragment(fragment)
            }
        }

    }

    private fun replaceCurrentQuestionFragment(questionFragment: GameExpandedAppBarFragment){
        supportFragmentManager.commit {
            currentFragment = questionFragment
            replace(R.id.game_fragment_container, questionFragment)
        }
    }

    override fun onComplete(correct: Boolean) {
        if(correct){
            println(correct)
            viewModel.addScore(1)
        }

        //Go to the next question fragment
        viewModel.nextQuestion()
    }

}