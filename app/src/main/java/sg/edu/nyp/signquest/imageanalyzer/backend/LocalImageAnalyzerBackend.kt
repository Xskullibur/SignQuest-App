package sg.edu.nyp.signquest

import android.content.Context
import android.graphics.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer


class SignLanguageImageAnalyzer(val context: Context) : ImageAnalysis.Analyzer {

    private val TAG = SignLanguageImageAnalyzer::class.java.name

    private val charAlphabetRange = 'A'..'Z'

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }



    override fun analyze(imageProxy: ImageProxy) {

//        val buffer = imageProxy.planes[0].buffer
//        val data = buffer.toByteArray()

        //val bitmap = imageProxy.toBitmap()

//        // Releases model resources if no longer used.
//        model.close()


//        FileOutputStream(File(context.getFilesDir(), "test.png")).use{
//            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
//            Log.d(TAG, "Write image to file")
//        }


//        val pixels = data.map { it.toInt() and 0xFF }

        imageProxy.close()
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