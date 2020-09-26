package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import kotlinx.android.synthetic.main.fragment_leaderboard.view.*
import kotlinx.android.synthetic.main.leaderboard_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sg.edu.nyp.signquest.R

class LeaderboardFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var scoreList: List<ScoreDetail> = mutableListOf(ScoreDetail(1000, "dinodance.json"))

        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(this@LeaderboardFragment.requireContext(),
                AppDatabase::class.java, "signquest.db").fallbackToDestructiveMigration().build()
            db.scoreDetailDao().addScoreDetail(ScoreTable(null, 10, "asdasdasd"))


            withContext(Dispatchers.Main){
                view.scoreList.adapter = LeaderboardAdapter(scoreList)
                view.scoreList.layoutManager = LinearLayoutManager(this@LeaderboardFragment.requireContext())
            }

        }
//        val scoreList = db.scoreDetailDao().scoreDetails()

        // Inflate the layout for this fragment


        return view
    }

    class LeaderboardAdapter(val leaderboards: List<ScoreDetail>): RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>(){
        class LeaderboardViewHolder(val view:LinearLayout): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_card, parent, false) as LinearLayout
            return  LeaderboardViewHolder(view)
        }

        override fun getItemCount(): Int {
            return  leaderboards.size
        }

        override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
            val leaderboard = leaderboards[position]
            holder.view.animationView.setAnimation(leaderboard.animation)
            holder.view.scoreAmount.text = leaderboard.score.toString()
        }


    }

}
@Entity(tableName = "ScoreTable")
data class ScoreTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "score")
    val score: Int,
    @ColumnInfo(name = "name")
    val name: String
){

}

class ScoreDetail(
    val score: Int,
    val animation: String
)

@Dao
interface ScoreDetailDao{
    @Query("SELECT * FROM ScoreTable")
    fun scoreDetails():List<ScoreTable>

    @Insert
    fun addScoreDetail(vararg scoreTable: ScoreTable)

    @Delete
    fun removeScoreDetail(scoreTable: ScoreTable)
}

@Database(entities = arrayOf(ScoreTable::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoreDetailDao(): ScoreDetailDao
}