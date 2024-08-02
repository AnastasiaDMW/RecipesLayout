package ru.itis.recipeslayout.fragments.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import ru.itis.recipeslayout.R
import ru.itis.recipeslayout.databinding.ProgressDialogLoadingViewBinding

@RequiresApi(Build.VERSION_CODES.R)
class CustomProgressLoadDialog(context: Context) {

    private var binding: ProgressDialogLoadingViewBinding? = null
    private var dialog: AlertDialog

    fun start(title: String = "") {
        binding?.run {
            pbTitle.text = title
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
        val view = inflater.inflate(R.layout.progress_dialog_loading_view, null)
        binding = ProgressDialogLoadingViewBinding.bind(view)
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        binding?.run {
            cvLoading.setCardBackgroundColor(Color.parseColor("#80919191"))

            setColorFilter(
                pbLoading.indeterminateDrawable,
                ResourcesCompat.getColor(context.resources, com.google.android.material.R.color.design_default_color_primary, null)
            )
            pbTitle.setTextColor(Color.WHITE)
            dialog.setContentView(view)
        }
        dialog.window?.run {
            setBackgroundDrawableResource(android.R.color.transparent)
            setGravity(Gravity.CENTER)
            dialog.window?.setLayout(600, 500)
        }
        view.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}