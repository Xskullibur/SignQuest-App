package sg.edu.nyp.signquest.tutorial

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_tutorial.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.view.CustomDialogFragment

private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class TutorialFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tryBtn.setOnClickListener {
            // Check permissions and navigate
            it.findNavController().navigate(R.id.action_tutorialFragment_to_practiceFragment)
        }

        val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
        val fragment = CustomDialogFragment.newInstance(
            title = "Congratulation",
            subtitle = "Stage 0-0",
            onBackBtnClick = {
                it.dismiss()
            },
            onRestartBtnClick = {
                it.dismiss()
            },
            onNextBtnClick = {
                it.dismiss()
            }
        )

        fragment.show(fragmentManager, CustomDialogFragment.TAG)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

}