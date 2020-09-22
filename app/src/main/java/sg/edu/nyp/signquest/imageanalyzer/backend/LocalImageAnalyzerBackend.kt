package sg.edu.nyp.signquest

import android.content.Context
import android.graphics.*
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import sg.edu.nyp.signquest.imageanalyzer.ImageAnalyzerBackend
import java.nio.ByteBuffer

class LocalImageAnalyzerBackend(val context: Context) : ImageAnalyzerBackend {
    private val charAlphabetRange = 'A'..'Z'

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }


    override fun translate(imageProxy: ImageProxy): Char? {
        TODO("Not Implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    fun toGrayscale(bmpOriginal: Bitmap): Bitmap? {
        val bmpMonochrome = Bitmap.createBitmap(bmpOriginal.width, bmpOriginal.height, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(bmpMonochrome)
        val ma = ColorMatrix()
        ma.setSaturation(0f)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(ma)
        canvas.drawBitmap(bmpOriginal, 0F, 0F, paint)
        return bmpMonochrome
    }

}