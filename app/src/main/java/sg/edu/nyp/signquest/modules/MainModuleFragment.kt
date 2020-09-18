package sg.edu.nyp.signquest.modules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_module.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.utils.MainUtils


/**
 * A simple [Fragment] subclass.
 * Use the [MainModuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainModuleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moduleAdapter = ModuleAdapter(this.requireContext(), MainUtils.data)
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