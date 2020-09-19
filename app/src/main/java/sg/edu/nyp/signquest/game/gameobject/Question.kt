package sg.edu.nyp.signquest.game.gameobject

import java.io.Serializable

abstract class Question(val glossToBeAnswered: Gloss): Serializable

class PlayerToSignQuestion(glossToBeAnswered: Gloss): Question(glossToBeAnswered)

class MCQQuestion(glossToBeAnswered: Gloss, val otherGlossaryChoice: Set<Gloss>): Question(glossToBeAnswered)