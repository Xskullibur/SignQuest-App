package sg.edu.nyp.signquest.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_tutorial.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.Glossary


class TutorialFragment : Fragment() {

    private val args: TutorialFragmentArgs by navArgs()
    private lateinit var glossary: Glossary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.let {
            glossary = it.glossary
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
            it.findNavController().navigate(R.id.action_tutorialFragment_to_practiceFragment)
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