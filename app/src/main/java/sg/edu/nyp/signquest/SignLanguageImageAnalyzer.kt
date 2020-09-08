package sg.edu.nyp.signquest

import android.content.Context
import android.graphics.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import sg.edu.nyp.signquest.ml.Christina
import java.nio.ByteBuffer


class SignLanguageImageAnalyzer(val context: Context) : ImageAnalysis.Analyzer {

    private val TAG = SignLanguageImageAnalyzer::class.java.name

    private val charAlphabetRange = 'A'..'Z'
    val model = Christina.newInstance(context)

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
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_baseline_access_time_24)

        //Resize image to 28x28
        val resizeBitmap = toGrayscale(Bitmap.createScaledBitmap(bitmap!!, 28, 28, false))!!

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 28, 28, 1), DataType.FLOAT32)
        val resizeBuffer = ByteBuffer.allocate(resizeBitmap.byteCount)

        resizeBitmap.copyPixelsToBuffer(resizeBuffer)

        val pixels = resizeBuffer.toByteArray().take(784).map { (it.toInt() and 0xFF) / 255 }.toIntArray()

        inputFeature0.loadArray(pixels)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val results = outputFeature0.floatArray
        val charIndex = results.indexOf(results.max()!!)
        val predictedChar = charAlphabetRange.elementAt(charIndex)

        println("Predicted Alphabet: $predictedChar")


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