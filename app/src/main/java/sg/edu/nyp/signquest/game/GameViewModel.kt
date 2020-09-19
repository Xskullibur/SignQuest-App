package sg.edu.nyp.signquest.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sg.edu.nyp.signquest.game.gameobject.GameProgress

class GameViewModel : ViewModel() {

    private val _gameProgress = MutableLiveData<GameProgress>()
    val gameProgress: LiveData<GameProgress>
        get() = _gameProgress

    private val _isGameCompleted = MutableLiveData(false)
    val isGameCompleted: LiveData<Boolean> = _isGameCompleted


    fun createGameProgress(gameProgress: GameProgress){
        _gameProgress.value = gameProgress
    }

    fun nextQuestion(){
        val haveNextQuestion = _gameProgress.value?.nextQuestion()
        if(haveNextQuestion != null){
            if(haveNextQuestion){
                //Update observable
                _gameProgress.value = _gameProgress.value
            }else{
                _isGameCompleted.value = true
            }
        }
    }


    fun addScore(score: Int){
        _gameProgress.value?.score?.plus(score)
        _gameProgress.value = _gameProgress.value
    }


}