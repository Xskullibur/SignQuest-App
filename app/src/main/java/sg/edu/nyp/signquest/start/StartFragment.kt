package sg.edu.nyp.signquest.start

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_start.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.ARGS_GAME_AVAILABLE_GLOSSARY
import sg.edu.nyp.signquest.game.ARGS_GAME_MODULE_ID
import sg.edu.nyp.signquest.game.GameActivity

class StartFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        //Bind menu buttons
        gameBtn.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putCharArray(ARGS_GAME_AVAILABLE_GLOSSARY, ('A'..'Z').toList().joinToString("").toCharArray())
                    putString(ARGS_GAME_MODULE_ID, "1")
                })
            }
            startActivity(intent)
        }

        learnBtn.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_mainModuleFragment)
        }

        leaderboardBtn.setOnClickListener{
            navController.navigate(R.id.action_startFragment_to_leaderboardFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)

    }

}