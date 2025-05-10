package com.tutar.sportsbetapp.presentation.extensions

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.tutar.sportsbetapp.R

fun Activity.createProgressDialog(isCancelable: Boolean = false): Dialog = Dialog(this).apply {
    setCancelable(isCancelable)
    window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context,android.R.color.transparent)))
    setContentView(R.layout.view_progress)
}