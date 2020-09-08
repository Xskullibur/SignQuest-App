package sg.edu.nyp.signquest.game.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_custom_dialog.*
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.*
import sg.edu.nyp.signquest.R

class CustomDialogFragment() : DialogFragment() {

    private var title: String? = null
    private var subtitle: String? = null

    companion object {

        private lateinit var backBtnCallback: (DialogFragment) -> Unit
        private lateinit var restartBtnCallback: (DialogFragment) -> Unit
        private lateinit var nextBtnCallback: (DialogFragment) -> Unit

        const val TAG = "CUSTOM_Dialog.Fragment"
        private const val KEY_TITLE = "title"
        private const val KEY_SUBTITLE = "subtitle"

        fun newInstance(
            title: String,
            subtitle: String,
            onBackBtnClick: (DialogFragment) -> Unit,
            onRestartBtnClick: (DialogFragment) -> Unit,
            onNextBtnClick: (DialogFragment) -> Unit
        ): CustomDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subtitle)
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

        title = arguments?.getString(KEY_TITLE)
        subtitle = arguments?.getString(KEY_SUBTITLE)
        val style = DialogFragment.STYLE_NORMAL
        val theme = R.style.DialogTheme
        setStyle(style, theme)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

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
}