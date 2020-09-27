package sg.edu.nyp.signquest.game

import androidx.lifecycle.ViewModel

open class LeaderboardViewModel: ViewModel() {
    var scoreList: List<ScoreDetail> = mutableListOf(ScoreDetail(3, "", "Ben"),ScoreDetail(4, "", "Mary"),ScoreDetail(2, "", "May"),ScoreDetail(5, "", "John"), ScoreDetail(2, "", "Raven"))

    fun addPlayerScore(score: Int, name: String){
        scoreList.toMutableList().add(ScoreDetail(score, "", name))
    }
}