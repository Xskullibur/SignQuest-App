package sg.edu.nyp.signquest.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


object AlertUtils {
    /**
     * Show an basic alert dialog to user
     * @param context - the parent context
     * @param title - title of the alert
     * @param message - message of the alert
     * @param onOkCallback - callback when the user pressed ok
     */
    @JvmStatic
    fun showAlert(context: Context, title: String, message: String, onOkCallback: (() -> Unit)? = null){
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok){ _, _ ->
                onOkCallback?.invoke()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


    fun Fragment.showAlert(title: String, message: String, onOkCallback: (() -> Unit)?  = null){
        showAlert(this.requireContext(), title, message, onOkCallback)
    }

}