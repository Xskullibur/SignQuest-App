package sg.edu.nyp.signquest.game

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import sg.edu.nyp.signquest.MainActivity
import kotlinx.coroutines.launch
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.*
import sg.edu.nyp.signquest.game.view.ConfettiType
import sg.edu.nyp.signquest.game.view.CustomDialogFragment
import sg.edu.nyp.signquest.utils.AlertUtils.showAlert
import sg.edu.nyp.signquest.utils.ResourceManager
import javax.annotation.Resource
import kotlin.random.Random

const val ARGS_GAME_AVAILABLE_GLOSSARY = "args_game_available_glossary"
const val ARGS_GAME_TOTAL_QUESTION = "args_game_total_question"
const val ARGS_GAME_MODULE_ID = "args_game_module_id"

const val NEXT = "next"

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
         * @param moduleId - module id for the current quiz
         */
        fun createActivityIntent(context: Context, glossary: CharArray, moduleId: String): Intent {
            return Intent(context, GameActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putCharArray(ARGS_GAME_AVAILABLE_GLOSSARY, glossary)
                    putString(ARGS_GAME_MODULE_ID, moduleId)
                })
            }
        }
    }
    //View models and current fragment
    private val viewModel: GameViewModel by viewModels()
    private var currentFragment: GameExpandedAppBarFragment? = null
    private var scoreTable: List<ScoreTable>? = null
    private var scoreList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch{
            scoreTable = viewModel.getScores()

            scoreTable?.forEach {
                scoreList.add(it.score)
            }
        }

        setContentView(R.layout.activity_game)
        //Get chars from bundle
        val availableChar = intent.extras?.getCharArray(ARGS_GAME_AVAILABLE_GLOSSARY)!!.toList()
        val moduleId = intent.extras?.getString(ARGS_GAME_MODULE_ID)!!

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
            if(savedInstanceState == null) showQuestion(it.currentQuestion)
        }
        viewModel.isGameCompleted.observe(this){isGameCompleted ->
            if(isGameCompleted){
                viewModel.gameProgress.value?.let {gameProgress ->
                    lifecycleScope.launch{
                        viewModel.addPlayerScore(gameProgress.score, "player")
                    }

                    // Update step to completed
                    val module = ResourceManager.getModule(moduleId)
                    module.nextIncompleteStep()?.completed = true

                    var title = "Please Try Again!"
                    var confettiType = ConfettiType.None

                    if(scoreList.maxOrNull() ?:0 < gameProgress.score){
                        title = "Well Done!"
                        confettiType = ConfettiType.Burst
                    }
                    else if(gameProgress.score >= gameProgress.totalAmountOfQuestion / 2)
                    {
                        title = "Nice Try!"
                        confettiType = ConfettiType.StreamFromTop
                    }
                    else{
                        title = "Good Effort"
                        confettiType = ConfettiType.None
                    }


                    val fragmentManager = supportFragmentManager.beginTransaction()
                    val fragment = CustomDialogFragment.newInstance(
                        title = title,
                        subtitle = "${gameProgress.score}/${gameProgress.totalAmountOfQuestion}",
                        confettiType = confettiType,
                        onBackBtnClick = {
                            finish()
                            it.dismiss()
                        },
                        onRestartBtnClick = {
                            // Get glossary for game
                            finish()

                            val glossList = ResourceManager.getCompletedGlossary(moduleId)
                            if (glossList != null) {
                                val intent = createActivityIntent(this, glossList, moduleId)
                                startActivity(intent)
                            }
                            it.dismiss()
                        },
                        onNextBtnClick = {
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra(NEXT, true)
                                putExtra(ARGS_GAME_MODULE_ID, moduleId)
                            })
                            finish()

                            it.dismiss()
                        }
                    )

                    fragment.show(fragmentManager, CustomDialogFragment.TAG)
                }
            }
        }
    }

    private fun showQuestion(question: Question){
        val fragment = question.createFragment()
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