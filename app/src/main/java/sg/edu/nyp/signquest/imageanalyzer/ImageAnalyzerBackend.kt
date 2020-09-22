package sg.edu.nyp.signquest.imageanalyzer

import androidx.camera.core.ImageProxy

interface ImageAnalyzerBackend {

    fun translate(imageProxy: ImageProxy): Char?
    fun stop()
}