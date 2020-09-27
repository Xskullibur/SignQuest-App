 package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_player_to_sign_main.*
import kotlinx.android.synthetic.main.fragment_player_to_sign_top.view.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected
import sg.edu.nyp.signquest.utils.buildAnalyzer

 /**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignFragment : GameExpandedAppBarFragment(), CameraListener, GameCountDownTimer, OnSignDetected {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_top
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_main

    override val viewModel: PlayerToSignViewModel by viewModels()

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraManager = CameraManager(this, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraManager.requestPermission()

        //Init observable
        viewModel.gloss.observe(viewLifecycleOwner){
            if(it != null)setGlossOnDisplay(it)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
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
            buildAnalyzer(this.requireContext(), this)
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
    }

    override fun signDetected(predictedValue: Char) {
        context?.mainLooper?.let {
            Handler(it).post {
                viewModel.gloss.observe(viewLifecycleOwner){
                    if (it != null && predictedValue.toString() == it.value) {
                        correct()
                    }
                }
            }
        }
    }

}