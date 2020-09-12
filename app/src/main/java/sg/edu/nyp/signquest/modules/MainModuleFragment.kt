package sg.edu.nyp.signquest.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_main_module.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.`object`.Module
import kotlin.math.log


/**
 * A simple [Fragment] subclass.
 * Use the [MainModuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainModuleFragment : Fragment() {

    private val MODULE_LIST = arrayListOf(
        Module("01", "Alphabets", "Learn the basics starting with signing alphabets!"),
        Module("02", "Finger Spelling", "Make use of what you have learnt in the previous module to spell words."),
        Module("03", "Greetings", "Learn how to greet people with the power of sign language"),
        Module("04", "Basic", "Learn how to greet people with the power of sign language"),
        Module("05", "Propositions", "Learn how to greet people with the power of sign language"),
        Module("06", "Food", "Learn how to greet people with the power of sign language"),
        Module("07", "Advanced", "Learn how to greet people with the power of sign language")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moduleAdapter = ModuleAdapter(this.requireContext(), MODULE_LIST)
        moduleList.adapter = moduleAdapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_module, container, false)
    }

}