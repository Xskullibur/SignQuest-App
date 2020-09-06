package sg.edu.nyp.signquest

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import sg.edu.nyp.signquest.utils.toBitmap
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer


class SignLanguageImageAnalyzer(val context: Context) : ImageAnalysis.Analyzer {

    private val TAG = SignLanguageImageAnalyzer::class.java.name

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }



    override fun analyze(imageProxy: ImageProxy) {

        val buffer = imageProxy.planes[0].buffer
        val data = buffer.toByteArray()

        val bitmap = imageProxy.toBitmap()


        FileOutputStream(File(context.getFilesDir(), "test.png")).use{
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
            Log.d(TAG, "Write image to file")
        }


        val pixels = data.map { it.toInt() and 0xFF }

        imageProxy.close()
    }
}