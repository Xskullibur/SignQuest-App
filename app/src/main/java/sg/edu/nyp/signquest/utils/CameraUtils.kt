package sg.edu.nyp.signquest.utils

import android.util.Size
import androidx.camera.core.ImageAnalysis
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraUtils {

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
                .setAnalyzer(executor, ImageAnalysis.Analyzer { image ->
                    print(image)
                })

        }
    }

}