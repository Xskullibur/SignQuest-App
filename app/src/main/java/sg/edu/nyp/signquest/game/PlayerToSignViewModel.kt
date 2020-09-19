package sg.edu.nyp.signquest.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import sg.edu.nyp.signquest.game.gameobject.GameProgress
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.PlayerToSignQuestion

class PlayerToSignViewModel : ViewModel() {

    private val _gameProgress = MutableLiveData<GameProgress>()

    private val _question = MutableLiveData<PlayerToSignQuestion>()
    val gloss: LiveData<Gloss> get() = Transformations.map(_question){
        it.glossToBeAnswered
    }

    fun setGameProgress(gameProgress: GameProgress){
        _gameProgress.value= gameProgress
    }

    fun setPlayerToSignQuestion(playerToSignQuestion: PlayerToSignQuestion){
        _question.value = playerToSignQuestion
    }


}