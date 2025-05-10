package com.tutar.sportsbetapp.presentation.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.tutar.sportsbetapp.R

class DefaultEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        applyDefaultStyle()
    }

    private fun applyDefaultStyle() {
        setBackgroundResource(R.drawable.edittext_underline)
        setTextColor(ContextCompat.getColor(context, R.color.editTextDefaultTextColor))

        val vertical = resources.getDimensionPixelSize(R.dimen.space_small)
        val horizontal = resources.getDimensionPixelSize(R.dimen.space_medium)
        setPadding(horizontal, vertical, horizontal, vertical)
    }
}