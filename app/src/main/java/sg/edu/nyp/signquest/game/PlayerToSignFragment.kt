 package sg.edu.nyp.signquest.game

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageAnalysis
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_player_to_sign_main.*
import kotlinx.android.synthetic.main.fragment_player_to_sign_top.view.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.Score
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected
import sg.edu.nyp.signquest.imageanalyzer.SignLanguageImageAnalyzer
import sg.edu.nyp.signquest.imageanalyzer.backend.ServerImageAnalyzerBackend
import java.util.concurrent.Executors

 /**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignFragment : GameExpandedAppBarFragment(), CameraListener, GameCountDownTimer, OnSignDetected {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_top
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_main

    override val viewModel: PlayerToSignViewModel by viewModels()

    private lateinit var cameraManager: CameraManager
    private lateinit var imageAnalyzer: SignLanguageImageAnalyzer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Init observable
        viewModel.gloss.observe(viewLifecycleOwner){
            if(it != null)setGlossOnDisplay(it)
        }

        return super.onCreateView(inflater, container, savedInstanceState).also {
            //Init camera
            cameraManager = CameraManager(this, this)
            cameraManager.requestPermission()
        }
    }

    private fun setGlossOnDisplay(gloss: Gloss){
        topContainerView.glossTxtView.text = gloss.value
    }

    /**
     * When permission is granted
     */
    override fun onCameraIsAccessible() {
        //Show camera on preview
        cameraManager.showCamera(cameraView.createSurfaceProvider()) {
            buildAnalyzer(this.requireContext())
        }

        setGameCountDownTimer(this)
        if(!viewModel.timerIsStarted){
            //Start game timer
            startCountDownTimer(10000)
        }
    }

    companion object {
        fun newInstance(): PlayerToSignFragment{
            return PlayerToSignFragment()
        }
    }

    override fun onTick(millisUntilFinished: Long) {

    }

    override fun onFinish() {
        wrong()
        stop()
    }

     private fun buildAnalyzer(context: Context): ImageAnalysis {
         return ImageAnalysis.Builder()
             .build()
             .also {
                 it.setAnalyzer(
                     Executors.newSingleThreadExecutor(),
                     SignLanguageImageAnalyzer(context, ServerImageAnalyzerBackend(context)).apply {
                         this.onSignDetected = this@PlayerToSignFragment
                         this@PlayerToSignFragment.imageAnalyzer = this
                     })
             }
     }

    override fun signDetected(predictedValue: Char) {
        context?.mainLooper?.let {
            Handler(it).post {
                if(view != null)
                viewModel.gloss.observe(viewLifecycleOwner){
                    if (!viewModel.completed && it != null && predictedValue.toString() == it.value) {
                        viewModel.completed = true
                        correct(Score(1))

                        //Shutdown server
                        stop()

                        viewModel.gloss.removeObservers(viewLifecycleOwner)
                    }
                }
            }
        }
    }

     private fun stop() {
         cameraManager.stopCamera()
         imageAnalyzer.stop()
     }

 }