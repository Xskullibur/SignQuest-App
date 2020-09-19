package sg.edu.nyp.signquest.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageAnalysis
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_practice.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.CameraListener
import sg.edu.nyp.signquest.game.CameraManager
import sg.edu.nyp.signquest.game.view.ConfettiType
import sg.edu.nyp.signquest.game.gameobject.Glossary
import sg.edu.nyp.signquest.game.gameobject.Step
import sg.edu.nyp.signquest.game.view.CustomDialogFragment
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected
import sg.edu.nyp.signquest.imageanalyzer.SignLanguageImageAnalyzer
import sg.edu.nyp.signquest.imageanalyzer.backend.ServerImageAnalyzerBackend
import sg.edu.nyp.signquest.utils.ResourceManager
import java.util.concurrent.Executors

class PracticeFragment : Fragment(), CameraListener, OnSignDetected {

    private val args: PracticeFragmentArgs by navArgs()
    private lateinit var glossary: Glossary
    private lateinit var step: Step
    private lateinit var moduleId: String

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.let {
            glossary = it.glossary
            step = it.step
            moduleId = it.module
        }

        cameraManager = CameraManager(this, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request for Camera Permission
        cameraManager.requestPermission()

        // Back Button
        practice_topAppBar.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }

        // Subtitle
        practice_topAppBar.subtitle = "' ${glossary.value} '"

        // Gloss
        practice_gloss.text = "Practice Signing ' ${glossary.value} '"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practice, container, false)

    }

    override fun onCameraIsAccessible() {
        //Show camera on preview
        cameraManager.showCamera(practice_cameraView.createSurfaceProvider()) { buildAnalyzer() }
    }

    override fun signDetected(predictedValue: Char) {
        if (predictedValue.toString() == glossary.value) {

            // TODO: Update MainUtils
//            glossary.completed = true

            val fragmentManager = requireActivity().supportFragmentManager.beginTransaction()
            val fragment = CustomDialogFragment.newInstance(
                title = "Good Job!",
                subtitle = "Stage ${moduleId}-${step.id}",
                confettiType = ConfettiType.StreamFromTop,
                onBackBtnClick = {
                    requireView().findNavController().popBackStack(R.id.startFragment, false)
                    it.dismiss()
                },
                onRestartBtnClick = {
                    it.dismiss()
                },
                onNextBtnClick = {

                    // TODO: Check Status
                    val (module, step, gloss) = ResourceManager.findNext(moduleId)
                    requireView().findNavController().popBackStack()
                    it.dismiss()
                }
            )

            fragment.show(fragmentManager, CustomDialogFragment.TAG)
        }
    }

    private fun buildAnalyzer(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                val context = this.requireContext()
                it.setAnalyzer(
                    Executors.newSingleThreadExecutor(),
                    SignLanguageImageAnalyzer(context, ServerImageAnalyzerBackend(context)).apply {
                        onSignDetected = this@PracticeFragment
                    }
                )
            }
    }


}