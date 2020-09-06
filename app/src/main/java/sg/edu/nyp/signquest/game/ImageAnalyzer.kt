package sg.edu.nyp.signquest.game

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

typealias PixelListener = (pixels: List<Int>) -> Unit

class ImageAnalyzer(private val listener: PixelListener): ImageAnalysis.Analyzer {


    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)

        return data
    }

    override fun analyze(image: ImageProxy) {

        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }

        listener(pixels)

        image.close()
    }
}