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
//            val action = StartFragmentDirections.actionStartFragmentToPlayerToSignFragment(Gloss("Eat"))
//            navController.navigate(action)
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }

        learnBtn.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_mainModuleFragment)
        }

        quizBtn.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_questionFragment)
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