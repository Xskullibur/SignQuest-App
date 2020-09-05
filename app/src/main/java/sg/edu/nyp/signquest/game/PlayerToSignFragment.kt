package sg.edu.nyp.signquest.game

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_player_to_sign.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.`object`.Gloss
import sg.edu.nyp.signquest.utils.AlertUtils.showAlert

private const val GLOSS_PARAM = "gloss_param"

/**
 * Fragment represent the Screen to let Player do the sign language
 * Use the [PlayerToSignFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerToSignFragment : Fragment() {
    private var gloss: Gloss? = null

    private val TAG = PlayerToSignFragment::class.java.name

    //Camera permission
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                this.showCamera()
            } else {
                this.showAlert(getString(R.string.no_camera_permission_title), getString(R.string.no_camera_permission_message))
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gloss = it.getParcelable(GLOSS_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        return inflater.inflate(R.layout.fragment_player_to_sign, container, false)
    }

    private fun showCamera(){
        val context = this.requireContext()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraView.createSurfaceProvider())
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this.requireContext()))
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param gloss [Gloss].
         * @return A new instance of fragment PlayerToSignFragment.
         */
        @JvmStatic
        fun newInstance(gloss: Gloss) =
            PlayerToSignFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GLOSS_PARAM, gloss)
                }
            }
    }
}