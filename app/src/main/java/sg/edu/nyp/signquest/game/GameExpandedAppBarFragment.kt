package sg.edu.nyp.signquest.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.game_expanded_appbar.view.*
import sg.edu.nyp.signquest.R

abstract class GameExpandedAppBarFragment : Fragment() {

    abstract val topContainerId: Int
    abstract val mainContainerId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.game_expanded_appbar, container, false)

        view.topContainer.layoutResource = topContainerId
        view.topContainer.inflate()
        view.mainContainer.layoutResource = mainContainerId
        view.mainContainer.inflate()

        return view
    }

}