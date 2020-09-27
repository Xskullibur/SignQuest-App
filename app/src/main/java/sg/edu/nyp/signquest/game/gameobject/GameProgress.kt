package sg.edu.nyp.signquest.game.gameobject

import java.io.Serializable

class GameProgress(
    //Returns the total amount of questions available
    val totalAmountOfQuestion: Int,
    //List of available Gloss that can be used to test the player
    private val availableChar: List<Char>
): Serializable {

    //List of questions available
    var questions: List<Question> = List(totalAmountOfQuestion){
        randomQuestion()
    }

    //The index of where the current question is being answered
    var currentlyQuestionIndex: Int = 0
        private set

    //Returns the current question that is being answered
    val currentQuestion: Question
        get() = questions.elementAt(currentlyQuestionIndex)

    //Returns the next question to be answered, if the current question is the last question, this
    //function will return null
    val nextQuestion: Question?
        get() = if (questions.size > currentlyQuestionIndex)
            questions.elementAt(currentlyQuestionIndex + 1)
        else null

    var score: Int = 0

    /**
     * Return a random question, either a MCQ question or Player to Sign Question
     */
    private fun randomQuestion(): Question{
        //Random question type, MCQ or Player to Sign
//        val questionType = QuestionType.values().random()
        //DEBUG
        val questionType = QuestionType.SIGN_WORD

        //Random question from the list of availableChar
        return questionType.generateQuestion(availableChar)
    }

    /**
     * Move on to the next question
     */
    fun nextQuestion(): Boolean {
        return if (totalAmountOfQuestion - 1 > currentlyQuestionIndex) {
            currentlyQuestionIndex++
            true
        } else {
            false
        }
    }
}
