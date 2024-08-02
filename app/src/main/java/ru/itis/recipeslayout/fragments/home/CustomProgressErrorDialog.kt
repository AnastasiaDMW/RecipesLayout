package ru.itis.recipeslayout.fragments.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.databinding.ProgressDialogErrorViewBinding

class CustomProgressErrorDialog(
    context: Context
) {

    private var binding: ProgressDialogErrorViewBinding? = null
    private var dialog: AlertDialog

    fun start(retryAction: () -> Unit) {
        binding?.run {
            btnRetry.setOnClickListener { retryAction() }
        }
        dialog.show()
    }

    fun stop() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    init {
        val builder = AlertDialog.Builder(context)
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog_error_view, null)
        binding = ProgressDialogErrorViewBinding.bind(view)
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        binding?.run {
            cvError.setCardBackgroundColor(Color.parseColor("#80919191"))

            dialog.setContentView(view)
        }
        dialog.window?.run {
            setBackgroundDrawableResource(android.R.color.transparent)
            setGravity(Gravity.CENTER)
        }
        view.setBackgroundColor(Color.TRANSPARENT)
    }

}