package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_player_to_sign_main.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.`object`.Gloss

private const val GLOSS_PARAM = "gloss_param"

/**
 * Fragment represent the Screen to let Player do the sign language
 * Use the [PlayerToSignFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerToSignFragment : GameExpandedAppBarFragment(), CameraListener {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_top
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_main

    private var gloss: Gloss? = null

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraManager = CameraManager(this, this)
        arguments?.let {
            gloss = it.getParcelable(GLOSS_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraManager.requestPermission()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * When permission is granted
     */
    override fun onCameraIsAccessible() {
        //Show camera on preview
        cameraManager.showCamera(cameraView.createSurfaceProvider())
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param gloss [Gloss].
         * @return A new instance of fragment PlayerToSignFragment.
         */
        @JvmStatic
        fun newInstance(gloss: Gloss) =
            PlayerToSignFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GLOSS_PARAM, gloss)
                }
            }
    }
}