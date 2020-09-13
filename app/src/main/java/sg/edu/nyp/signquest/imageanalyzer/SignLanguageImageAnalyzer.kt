package sg.edu.nyp.signquest.imageanalyzer

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy


class SignLanguageImageAnalyzer(val context: Context,
                                private val imageAnalyzerBackend: ImageAnalyzerBackend) : ImageAnalysis.Analyzer {

    private val TAG = SignLanguageImageAnalyzer::class.java.name

    override fun analyze(imageProxy: ImageProxy) {
        val predictedChar = imageAnalyzerBackend.translate(imageProxy)
        Log.i(TAG, "Predicted Alphabet: $predictedChar")
        imageProxy.close()
    }



}