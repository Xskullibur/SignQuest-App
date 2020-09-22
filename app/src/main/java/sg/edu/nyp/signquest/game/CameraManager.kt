package sg.edu.nyp.signquest.game

import android.Manifest
import android.util.Log
import android.view.Surface
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.utils.AlertUtils.showAlert

interface CameraListener {
    /**
     * Called when the [Manifest.permission.CAMERA] permission is granted
     */
    fun onCameraIsAccessible()

    /**
     * Called when the [Manifest.permission.CAMERA] permission is rejected
     */
    fun onCameraPermissionIsRejected(){}
}

class CameraManager(val fragment: Fragment, private val cameraListener: CameraListener) {

    private val TAG = CameraManager::class.java.name

    private val context = fragment.requireContext()

    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalysis: ImageAnalysis? = null

    //Camera permission
    val requestPermissionLauncher =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraListener.onCameraIsAccessible()
            } else {
                cameraListener.onCameraPermissionIsRejected()
                showAlert(context, context.getString(R.string.no_camera_permission_title),
                    context.getString(R.string.no_camera_permission_message))
            }
        }


    /**
     * Request for Camera Permission
     */
    fun requestPermission(){
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }


    /**
     * Start camera to show in preview
     * @param surfaceProvider - surface to show the camera preview at, make sure the [View] is a
     *  instance of [androidx.camera.view.PreviewView] and call [androidx.camera.view.PreviewView.createSurfaceProvider]
     *  to get the [Preview.SurfaceProvider] used by the CameraX to display the preview.
     *  @param imageAnalysisBuilder - function to create a [ImageAnalyzer] to do image analysis on each image the camera captured,
     *  pass null if you are not doing any image analysis
     */
    fun showCamera(surfaceProvider: Preview.SurfaceProvider, imageAnalysisBuilder: (() -> ImageAnalysis)? = null){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

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
                cameraProvider?.unbindAll()

                imageAnalysis = imageAnalysisBuilder?.invoke()

                if(imageAnalysis != null){
                    cameraProvider?.bindToLifecycle(
                        fragment, cameraSelector, preview, imageAnalysis)
                }else{
                    cameraProvider?.bindToLifecycle(
                        fragment, cameraSelector, preview)
                }

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun stopCamera(){
        if(cameraProvider==null){
            throw IllegalStateException("Camera is not initialized!")
        }
        cameraProvider?.unbindAll()
        imageAnalysis = null
    }

    fun stopImageAnalysis(){
        cameraProvider?.unbind(imageAnalysis)
        imageAnalysis = null
    }

}