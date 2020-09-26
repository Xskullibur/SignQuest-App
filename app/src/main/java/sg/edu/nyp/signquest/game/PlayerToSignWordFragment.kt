package sg.edu.nyp.signquest.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sg.edu.nyp.signquest.R


/**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignWordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_to_sign_word_main, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayerToSignWordFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}