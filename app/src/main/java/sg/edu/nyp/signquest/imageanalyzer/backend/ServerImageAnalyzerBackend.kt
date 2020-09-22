package sg.edu.nyp.signquest.imageanalyzer.backend

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.imageanalyzer.ImageAnalyzerBackend
import sg.edu.nyp.signquest.proto.ImageRequest
import sg.edu.nyp.signquest.proto.TranslatedReply
import sg.edu.nyp.signquest.proto.TranslationServiceGrpc
import sg.edu.nyp.signquest.utils.toBitmap
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class ServerImageAnalyzerBackend(val context: Context) : ImageAnalyzerBackend{

    private val TRANSLATE_SERVER_ENDPOINT = context.getString(R.string.translate_server_endpoint)
    private val mChannel: ManagedChannel = ManagedChannelBuilder.forAddress(TRANSLATE_SERVER_ENDPOINT, 5000)
        .usePlaintext().build()
    private val stub = TranslationServiceGrpc.newBlockingStub(mChannel)

    private val TAG = ServerImageAnalyzerBackend::class.java.canonicalName

    override fun translate(imageProxy: ImageProxy): Char? {
        val bitmap = imageProxy.toBitmap()!!
        //Resize image to 28x28
        val resizeBitmap = Bitmap.createScaledBitmap(bitmap, 848, 640, false)

        //Get resize buffer
        val resizeBuffer = ByteBuffer.allocate(resizeBitmap.byteCount)

        resizeBitmap.copyPixelsToBuffer(resizeBuffer)

        resizeBuffer.flip()

        val imageRequest = ImageRequest.newBuilder()
            .setPixels(ByteString.copyFrom(resizeBuffer)).build()

        return try{
            val translatedReply = stub.translate(imageRequest)
            //Get first char
            translatedReply.char[0]
        }catch(e: StatusRuntimeException){
            null
        }
    }

    override fun stop() {
        mChannel.shutdownNow()
        Log.d(TAG, "Shutting down server connection")
        mChannel.awaitTermination(15, TimeUnit.SECONDS))
        Log.d(TAG, "Server connection was shutdown")
    }

}