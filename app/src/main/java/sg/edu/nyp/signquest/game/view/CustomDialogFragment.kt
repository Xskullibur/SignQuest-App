package sg.edu.nyp.signquest.game.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_custom_dialog.*
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import sg.edu.nyp.signquest.R

public enum class ConfettiType {
    Burst, StreamFromTop
}

class CustomDialogFragment : DialogFragment() {

    private var title: String? = null
    private var subtitle: String? = null
    private var confettiType: ConfettiType? = null

    companion object {

        private lateinit var backBtnCallback: (DialogFragment) -> Unit
        private lateinit var restartBtnCallback: (DialogFragment) -> Unit
        private lateinit var nextBtnCallback: (DialogFragment) -> Unit

        const val TAG = "CUSTOM_Dialog.Fragment"
        private const val KEY_TITLE = "title"
        private const val KEY_SUBTITLE = "subtitle"
        private const val KEY_CONFETTI = "confettiType"

        fun newInstance(
            title: String,
            subtitle: String,
            confettiType: ConfettiType,
            onBackBtnClick: (DialogFragment) -> Unit,
            onRestartBtnClick: (DialogFragment) -> Unit,
            onNextBtnClick: (DialogFragment) -> Unit
        ): CustomDialogFragment {

            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subtitle)
            args.putSerializable(KEY_CONFETTI, confettiType)

            backBtnCallback = onBackBtnClick
            restartBtnCallback = onRestartBtnClick
            nextBtnCallback = onNextBtnClick

            val fragment = CustomDialogFragment()
            fragment.arguments = args
            return fragment

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Details
        title = arguments?.getString(KEY_TITLE)
        subtitle = arguments?.getString(KEY_SUBTITLE)
        confettiType = arguments?.getSerializable(KEY_CONFETTI) as ConfettiType

        // Styling
        val style = DialogFragment.STYLE_NORMAL
        val theme = R.style.DialogTheme
        setStyle(style, theme)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_custom_dialog, container, false)
        val bckBtn = view.backBtn
        val restartBtn = view.restartBtn
        val nextBtn = view.nextBtn

        bckBtn.setOnClickListener {
            backBtnCallback.invoke(this)
        }

        restartBtn.setOnClickListener {
            restartBtnCallback.invoke(this)
        }

        nextBtn.setOnClickListener {
            nextBtnCallback.invoke(this)
        }

        // Title
        view.title.text = title
        view.subtitle.setText(subtitle)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Confetti
        when (confettiType) {
            ConfettiType.Burst -> burstFromCenter()
            ConfettiType.StreamFromTop -> streamFromTop()
        }

    }

    private fun streamFromTop() {
        confettiView.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 3f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12), Size(16, 6f))
            .setPosition(-50f, resources.displayMetrics.widthPixels + 50f, -50f, -50f)
            .streamFor(300, 800L)
    }

    private fun burstFromCenter() {
        confettiView.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(5f, 10f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .setPosition(confettiView.x + resources.displayMetrics.widthPixels / 2, confettiView.y + resources.displayMetrics.heightPixels / 2)
            .burst(100)
    }

}