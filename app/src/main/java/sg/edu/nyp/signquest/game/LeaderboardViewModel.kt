package sg.edu.nyp.signquest.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class LeaderboardViewModel(application: Application): AndroidViewModel(application) {
    var scoreList = mutableListOf<ScoreDetail>()

    val db = Room.databaseBuilder(getApplication(),
    AppDatabase::class.java, "signquest.db").fallbackToDestructiveMigration().build()

    fun addPlayerScore(score: Int, name: String){
        db.scoreDetailDao().addScoreDetail(ScoreTable(null, score, name))
    }
}




