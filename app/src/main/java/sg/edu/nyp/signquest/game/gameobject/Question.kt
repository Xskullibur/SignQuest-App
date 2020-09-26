package sg.edu.nyp.signquest.game.gameobject

import sg.edu.nyp.signquest.game.GameExpandedAppBarFragment
import sg.edu.nyp.signquest.game.MCQQuestionFragment
import sg.edu.nyp.signquest.game.PlayerToSignFragment
import java.io.Serializable

abstract class Question(val glossToBeAnswered: Gloss): Serializable {
    /**
     * Create a fragment which contains the screen to let the user answer the answer.
     * @return a fragment which correspond to the question that the user will interact with to answer the question.
     */
    abstract fun createFragment(gameProgress: GameProgress): GameExpandedAppBarFragment
}

class PlayerToSignQuestion(glossToBeAnswered: Gloss): Question(glossToBeAnswered) {
    /**
     * Create a [PlayerToSignFragment] for letting user play [PlayerToSignQuestion]
     */
    override fun createFragment(
        gameProgress: GameProgress
    ) = PlayerToSignFragment.newInstance(gameProgress, this)
}

class MCQQuestion(glossToBeAnswered: Gloss, val otherGlossaryChoice: Set<Gloss>): Question(glossToBeAnswered){
    /**
     * Create a [MCQQuestionFragment] for letting user play [PlayerToSignQuestion]
     */
    override fun createFragment(gameProgress: GameProgress
    ) = MCQQuestionFragment.newInstance(gameProgress, this)
}

enum class QuestionType {
    MCQ {
        /**
         * Generate a random MCQ question
         */
        override fun generateQuestion(availableChar: List<Char>): Question {
            val selectedGlossary = availableChar.shuffled().take(4).map { Gloss(it.toString()) }

            val glossToPredict = selectedGlossary[0]
            val otherGlossaryChoice = selectedGlossary.drop(1)

            return MCQQuestion(glossToPredict, otherGlossaryChoice.toSet())
        }
    },
    SIGN_ALPHABET {
        /**
         * Generate a random Sign Alphabet question
         */
        override fun generateQuestion(availableChar: List<Char>): Question {
            val charToPredict = availableChar.random()
            return PlayerToSignQuestion(Gloss(charToPredict.toString()))
        }
    };

    /**
     * Generate a random question.
     * @param availableChar list of available chars or gloss to choose from.
     * @return the randomly generated question.
     */
    abstract fun generateQuestion(availableChar: List<Char>): Question

}