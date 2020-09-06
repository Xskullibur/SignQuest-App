package sg.edu.nyp.signquest.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_practice.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.CameraListener
import sg.edu.nyp.signquest.game.CameraManager

class PracticeFragment : Fragment(), CameraListener {

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraManager = CameraManager(this, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraManager.requestPermission()

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

        // Build Image Analyzer
//        val imageAnalyzer = cameraManager.buildImageAnalysis()

        //Show camera on preview
        cameraManager.showCamera(practice_cameraView.createSurfaceProvider(), null)
    }

}