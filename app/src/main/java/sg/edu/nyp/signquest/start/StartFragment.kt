package sg.edu.nyp.signquest.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_start.view.*
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_start.*
import sg.edu.nyp.signquest.R

class StartFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        //Bind menu buttons
        view.gameBtn.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_playerToSignFragment)
        }

        learnBtn.setOnClickListener {
            navController.navigate(R.id.action_startFragment_to_tutorialFragment)
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