package sg.edu.nyp.signquest.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_tutorial.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.Glossary
import sg.edu.nyp.signquest.game.gameobject.Step


class TutorialFragment : Fragment() {

    private val args: TutorialFragmentArgs by navArgs()
    private lateinit var glossary: Glossary
    private lateinit var step: Step
    private lateinit var moduleId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.let {
            glossary = it.glossary
            step = it.step
            moduleId = it.module
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorial_sign_txt.text = "' ${glossary.value} '"
        tutorial_image.setImageResource(resources.getIdentifier(
            glossary.src,
            "drawable",
            requireContext().packageName
        ))

        tryBtn.setOnClickListener {
            // Check permissions and navigate
            val action = TutorialFragmentDirections.actionTutorialFragmentToPracticeFragment(glossary, step, moduleId)
            it.findNavController().navigate(action)
        }

        tutorial_topAppBar.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

}