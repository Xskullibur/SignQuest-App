package sg.edu.nyp.signquest.game.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_custom__play__dialog.view.*
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.subtitle
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.title
import sg.edu.nyp.signquest.R

class CustomPlayDialogFragment : DialogFragment() {

    private var title: String? = null
    private var subtitle: String? = null

    companion object {

        private lateinit var buttonCallback: (DialogFragment) -> Unit

        const val TAG = "CUSTOM_PLAY_Dialog.Fragment"
        private const val KEY_TITLE = "title"
        private const val KEY_SUBTITLE = "subtitle"

        fun newInstance(
            title: String,
            subtitle: String,
            onBtnClick: (DialogFragment) -> Unit,
        ): CustomPlayDialogFragment {

            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subtitle)

            buttonCallback = onBtnClick

            val fragment = CustomPlayDialogFragment()
            fragment.arguments = args
            return fragment

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Details
        title = arguments?.getString(CustomPlayDialogFragment.KEY_TITLE)
        subtitle = arguments?.getString(CustomPlayDialogFragment.KEY_SUBTITLE)

        // Styling
        val style = DialogFragment.STYLE_NORMAL
        val theme = R.style.DialogTheme
        setStyle(style, theme)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custom_dialog, container, false)
        val button = view.dialogBtn

        button.setOnClickListener {
            buttonCallback.invoke(this)
        }

        // Title
        view.title.text = title
        view.subtitle.text = subtitle

        return view
    }

}