 package sg.edu.nyp.signquest.game

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
import kotlinx.android.synthetic.main.game_expanded_appbar.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.GameProgress
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.PlayerToSignQuestion
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected
import sg.edu.nyp.signquest.imageanalyzer.SignLanguageImageAnalyzer
import sg.edu.nyp.signquest.imageanalyzer.backend.ServerImageAnalyzerBackend
import java.util.concurrent.Executors

const val ARGS_PLAYER_TO_SIGN_GAME_PROGRESS = "args_player_to_sign_fragment_game_progress"
const val ARGS_PLAYER_TO_SIGN_QUESTION = "args_player_to_sign_fragment_question"
/**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignFragment : GameExpandedAppBarFragment(), CameraListener, GameCountDownTimer, OnSignDetected {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_top
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_main

    private val viewModel: PlayerToSignViewModel by viewModels()

    private lateinit var cameraManager: CameraManager

    private val time : Long = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraManager = CameraManager(this, this)
        arguments?.let {
            val gameProgress = it.get(ARGS_PLAYER_TO_SIGN_GAME_PROGRESS) as GameProgress
            val question = it.get(ARGS_PLAYER_TO_SIGN_QUESTION) as PlayerToSignQuestion

            viewModel.setGameProgress(gameProgress)
            this.setGameProgress(gameProgress)
            viewModel.setPlayerToSignQuestion(question)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraManager.requestPermission()

        //Init observable
        viewModel.gloss.observe(viewLifecycleOwner){
            setGlossOnDisplay(it)
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
        cameraManager.showCamera(cameraView.createSurfaceProvider()){
            this.buildAnalyzer()
        }

        //Start game timer
        setGameCountDownTimer(this)
        startCountDownTimer(time)
    }

    private fun buildAnalyzer(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                val context = this.requireContext()
                it.setAnalyzer(Executors.newSingleThreadExecutor(),
                    SignLanguageImageAnalyzer(context, ServerImageAnalyzerBackend(context)).apply {
                        onSignDetected = this@PlayerToSignFragment
                    })
            }
    }

    companion object {
        fun newInstance(gameProgress: GameProgress, question: PlayerToSignQuestion): PlayerToSignFragment{
            return PlayerToSignFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARGS_PLAYER_TO_SIGN_GAME_PROGRESS, gameProgress)
                    putSerializable(ARGS_PLAYER_TO_SIGN_QUESTION, question)
                }
            }
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
                    if (predictedValue.toString() == it.value) {
                        val timeleft = timerTxtView.text.toString().toLong()
                        var score = (10 / (time/1000))*timeleft
                        correct(score.toInt())
                    }
                }
            }
        }
    }

}