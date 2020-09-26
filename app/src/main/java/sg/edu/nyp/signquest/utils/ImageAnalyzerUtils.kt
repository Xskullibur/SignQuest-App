package sg.edu.nyp.signquest.utils

import android.content.Context
import androidx.camera.core.ImageAnalysis
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected
import sg.edu.nyp.signquest.imageanalyzer.SignLanguageImageAnalyzer
import sg.edu.nyp.signquest.imageanalyzer.backend.ServerImageAnalyzerBackend
import java.util.concurrent.Executors

fun buildAnalyzer(context: Context, onSignDetected: OnSignDetected?): ImageAnalysis {
    return ImageAnalysis.Builder()
        .build()
        .also {
            it.setAnalyzer(
                Executors.newSingleThreadExecutor(),
                SignLanguageImageAnalyzer(context, ServerImageAnalyzerBackend(context)).apply {
                    this.onSignDetected = onSignDetected
            })
        }
}