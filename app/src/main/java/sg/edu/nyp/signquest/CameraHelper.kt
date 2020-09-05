package sg.edu.nyp.signquest

import android.util.Rational
import android.util.Size
import android.view.TextureView
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.impl.PreviewConfig
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraHelper {

    companion object {

        private lateinit var executor: ExecutorService

        fun buildImageAnalysisUseCase(
            resolution: Size = Size(1280, 720),
            strategy: Int = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        ) {

            // Initialize background executor
            executor = Executors.newSingleThreadExecutor()

            ImageAnalysis.Builder()
                .setTargetResolution(resolution)
                .setBackpressureStrategy(strategy)
                .build()
                .setAnalyzer(executor, ImageAnalysis.Analyzer {image ->
                    print(image)
                })

        }
    }

}