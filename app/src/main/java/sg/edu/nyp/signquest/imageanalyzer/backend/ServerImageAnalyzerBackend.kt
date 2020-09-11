package sg.edu.nyp.signquest.imageanalyzer.backend

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.imageanalyzer.ImageAnalyzerBackend
import sg.edu.nyp.signquest.proto.ImageRequest
import sg.edu.nyp.signquest.proto.TranslatedReply
import sg.edu.nyp.signquest.proto.TranslationServiceGrpc
import sg.edu.nyp.signquest.utils.toBitmap
import java.nio.ByteBuffer

class ServerImageAnalyzerBackend(val context: Context) : ImageAnalyzerBackend{

    private val TRANSLATE_SERVER_ENDPOINT = context.getString(R.string.translate_server_endpoint)
    private val mChannel: ManagedChannel = ManagedChannelBuilder.forAddress(TRANSLATE_SERVER_ENDPOINT, 5000)
        .usePlaintext().build()
    private val stub = TranslationServiceGrpc.newBlockingStub(mChannel)

    override fun translate(imageProxy: ImageProxy): Char {
        val bitmap = imageProxy.toBitmap()
        //Resize image to 28x28
        val resizeBitmap = Bitmap.createScaledBitmap(bitmap!!, 28, 28, false)

        //Get resize buffer
        val resizeBuffer = ByteBuffer.allocate(resizeBitmap.byteCount)

        resizeBitmap.copyPixelsToBuffer(resizeBuffer)

        val imageRequest = ImageRequest.newBuilder()
            .setPixels(ByteString.copyFrom(resizeBuffer)).build()
        val translatedReply = stub.translate(imageRequest)
        //Get first char
        return translatedReply.char[0]
    }

}