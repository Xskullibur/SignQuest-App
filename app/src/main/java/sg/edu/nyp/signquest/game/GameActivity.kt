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

class GameActivity : AppCompatActivity(), QuestionListener {

    companion object {
        /**
         * Create an activity intent to start GameActivity
         * Usage:
         *      val intent = GameActivity.createActivityIntent(context. glossary)
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

    private val viewModel: GameViewModel by viewModels()

//    private val availableChar = ('A'..'Z').filter {
//        //The letter 'J' and 'Z' is not included
//        it != 'J' || it != 'Z'
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val _gameProgress = viewModel.gameProgress.value
        if(_gameProgress == null){

            //Get chars from bundle
            val availableChar = intent.extras?.getCharArray(ARGS_GAME_AVAILABLE_GLOSSARY)!!.toList()

            //Randomly generate 20 questions
            val random20Questions = List(20){ randomQuestion(availableChar) }

            //Create Game Progress for storing game state
            val gameProgress = GameProgress(random20Questions)
            viewModel.createGameProgress(gameProgress)

            viewModel.gameProgress.observe(this){

                showQuestion(it.currentQuestion)
            }
            viewModel.isGameCompleted.observe(this){isGameCompleted ->
                if(isGameCompleted){
                    viewModel.gameProgress.value?.let {gameProgress
                        showAlert(this,"Score", "${gameProgress.score}/${gameProgress.totalAmountOfQuestion}")
                    }
                }
            }

            showQuestion(random20Questions[0])
        }else{
            showQuestion(_gameProgress.currentQuestion)
        }


    }

    fun showQuestion(question: Question){
        when (question) {
            is PlayerToSignQuestion -> {
                showQuestion(question)
            }
            is MCQQuestion -> {
                showQuestion(question)
            }
            else -> {
                throw IllegalArgumentException("Unknown class for question, question should be either an instance of PLayerToSignQuestion or MCQQuestion")
            }
        }
    }

    fun showQuestion(question: PlayerToSignQuestion){
        val fragment = PlayerToSignFragment.newInstance(viewModel.gameProgress.value!!, question)
        supportFragmentManager.commit {
            replace(R.id.game_fragment_container, fragment)
        }
    }
    fun showQuestion(question: MCQQuestion){
        val fragment = MCQQuestionFragment.newInstance(viewModel.gameProgress.value!!, question)
        supportFragmentManager.commit {
            replace(R.id.game_fragment_container, fragment)
        }
    }

    /**
     * Return a random question, either a MCQ question or Player to Sign Question
     */
    fun randomQuestion(availableChar: List<Char>): Question{
        return if(Random.nextBoolean()){
            //Player to Sign Question
            randomPlayerToSignQuestion(availableChar)
        }else{
            //MCQ Question
            randomMCQQuestion(availableChar)
        }
    }

    /**
     * Returns a random Player to Sign Question
     */
    fun randomPlayerToSignQuestion(availableChar: List<Char>): PlayerToSignQuestion {
        val charToPredict = availableChar.random()
        return PlayerToSignQuestion(Gloss(charToPredict.toString()))
    }

    /**
     * Returns a random MCQ Question
     */
    fun randomMCQQuestion(availableChar: List<Char>): MCQQuestion {
        val selectedGlossary = availableChar.shuffled().take(4).map { Gloss(it.toString()) }

        val glossToPredict = selectedGlossary[0]
        val otherGlossaryChoice = selectedGlossary.drop(1)

        return MCQQuestion(glossToPredict, otherGlossaryChoice.toSet())
    }

    override fun onComplete(correct: Boolean) {
        if(correct){
            println(correct)
            viewModel.addScore(1)
        }

        //Next question fragment
        viewModel.nextQuestion()

    }

}