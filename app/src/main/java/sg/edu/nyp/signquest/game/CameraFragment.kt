package sg.edu.nyp.signquest.game

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.utils.AlertUtils.showAlert

abstract class CameraFragment:  Fragment() {

    private val TAG = CameraFragment::class.java.name

    //Camera permission
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                this.onCameraIsAccessible()
            } else {
                this.onCameraPermissionIsRejected()
                this.showAlert(getString(R.string.no_camera_permission_title), getString(R.string.no_camera_permission_message))
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Called when the [Manifest.permission.CAMERA] permission is granted
     */
    abstract fun onCameraIsAccessible()

    /**
     * Called when the [Manifest.permission.CAMERA] permission is rejected
     */
    protected fun onCameraPermissionIsRejected(){}

    /**
     * Start camera to show in preview
     * @param surfaceProvider - surface to show the camera preview at, make sure the [View] is a
     *  instance of [androidx.camera.view.PreviewView] and call [androidx.camera.view.PreviewView.createSurfaceProvider]
     *  to get the [Preview.SurfaceProvider] used by the CameraX to display the preview.
     *  @param imageAnalysis - Do image analysis on each image the camera captured, pass null if you are not doing any image analysis
     */
    protected fun showCamera(surfaceProvider: Preview.SurfaceProvider, imageAnalysis: ImageAnalysis? = null){
        val context = this.requireContext()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build()
                .also {
                    it.setSurfaceProvider(surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                if(imageAnalysis != null){
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis)
                }else{
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview)
                }

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

}