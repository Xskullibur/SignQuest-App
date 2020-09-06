package sg.edu.nyp.signquest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.*

class CustomDialogFragment: DialogFragment() {

    companion object {

        const val TAG = "CUSTOM_Dialog.Fragment"
        private const val KEY_TITLE = "title"

        fun newInstance(title: String): CustomDialogFragment {
            val fragment = CustomDialogFragment()

            val args = Bundle()
            args.putString(KEY_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
        val title = arguments?.getString("title")
        view.title.text = title
    }

    private fun setupClickListeners(view: View) {
//        view.backBtn.setOnClickListener {
//            dismiss()
//        }
//
//        view.restartBtn.setOnClickListener {
//            dismiss()
//        }
//
//        view.nextBtn.setOnClickListener {
//            dismiss()
//        }
    }

}