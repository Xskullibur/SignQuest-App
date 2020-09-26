package sg.edu.nyp.signquest.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.GameProgress
import sg.edu.nyp.signquest.game.gameobject.PlayerToSignQuestion
import sg.edu.nyp.signquest.imageanalyzer.OnSignDetected


/**
 * Fragment represent the Screen to let Player do the sign language
 */
class PlayerToSignWordFragment : GameExpandedAppBarFragment(), OnSignDetected {

    override val topContainerId: Int = R.layout.fragment_player_to_sign_top
    override val mainContainerId: Int = R.layout.fragment_player_to_sign_word_main

    override val viewModel: PlayerToSignWordViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = PlayerToSignWordFragment()
    }

    override fun signDetected(predictedValue: Char) {
        TODO("Not yet implemented")
    }
}