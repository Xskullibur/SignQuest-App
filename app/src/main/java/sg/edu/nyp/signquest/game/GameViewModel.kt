package sg.edu.nyp.signquest.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sg.edu.nyp.signquest.game.gameobject.GameProgress

class GameViewModel(application: Application): AndroidViewModel(application) {

    private val _gameProgress = MutableLiveData<GameProgress>()
    val gameProgress: LiveData<GameProgress>
        get() = _gameProgress

    private val _isGameCompleted = MutableLiveData(false)
    val isGameCompleted: LiveData<Boolean> = _isGameCompleted

    private val _newQuestion = MutableLiveData(false)
    val newQuestion: LiveData<Boolean> = _newQuestion

    val db = Room.databaseBuilder(getApplication(),
        AppDatabase::class.java, "signquest.db").fallbackToDestructiveMigration().build()


    fun createGameProgress(gameProgress: GameProgress){
        _gameProgress.value = gameProgress
    }

    fun nextQuestion(){
        val haveNextQuestion = _gameProgress.value?.nextQuestion()
        if(haveNextQuestion != null){
            if(haveNextQuestion){
                //Update observable
                _newQuestion.value = true
            }else{
                _isGameCompleted.value = true
            }
        }
    }


    fun addScore(score: Int){
        _gameProgress.value?.let {
            it.score += score
            _gameProgress.value = it
        }
    }

    suspend fun addPlayerScore(score: Int, name: String) = withContext(Dispatchers.IO){
        db.scoreDetailDao().addScoreDetail(ScoreTable(null, score, name))
    }

    suspend fun getScores() = withContext(Dispatchers.IO) {
        db.scoreDetailDao().scoreDetails()
    }

}