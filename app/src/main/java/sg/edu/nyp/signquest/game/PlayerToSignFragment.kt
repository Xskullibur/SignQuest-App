package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_player_to_sign_main.*
import kotlinx.android.synthetic.main.fragment_player_to_sign_top.view.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.`object`.Gloss


/**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignFragment : GameExpandedAppBarFragment(), CameraListener {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_top
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_main

    private val args: PlayerToSignFragmentArgs by navArgs()
    private val viewModel: PlayerToSignViewModel by viewModels()

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraManager = CameraManager(this, this)
        args.let {
            viewModel.setGloss(it.gloss)
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
        cameraManager.showCamera(cameraView.createSurfaceProvider())

        //Start game timer
        startCountDownTimer(60000)
    }
}