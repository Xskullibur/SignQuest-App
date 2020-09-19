package sg.edu.nyp.signquest.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import sg.edu.nyp.signquest.game.gameobject.GameProgress
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.MCQQuestion

class MCQQuestionViewModel: ViewModel()  {

    private val _gameProgress = MutableLiveData<GameProgress>()
    val gameProgress: LiveData<GameProgress> = _gameProgress

    private val _question = MutableLiveData<MCQQuestion>()
    val question: LiveData<MCQQuestion> = _question


    val glossToBeAnswered: LiveData<Gloss> =
        Transformations.map(_question) {
            it.glossToBeAnswered
        }
    val otherGlossaryChoice: LiveData<Set<Gloss>> = Transformations.map(_question){
        it.otherGlossaryChoice
    }

    fun setGameProgress(gameProgress: GameProgress){
        _gameProgress.value= gameProgress
    }

    fun setMCQQuestion(mcqQuestion: MCQQuestion){
        _question.value = mcqQuestion
    }



}