package com.tutar.sportsbetapp.presentation.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.tutar.sportsbetapp.R

class DefaultTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputLayout(context, attrs) {

    private val originalHint = hint
    private var isErrorShowing = false

    private val focusedColor by lazy {
        ContextCompat.getColor(
            context,
            R.color.focusedHintTextColor
        )
    }

    private val defaultColor by lazy {
        ContextCompat.getColor(
            context,
            R.color.defaultHintTextColor
        )
    }

    private val underlineFilled by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.edittext_underline_filled
        )
    }
    private val underlineDefault by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.edittext_underline
        )
    }
    private val underlineError by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.edittext_underline_error
        )
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val editText = editText
        if (editText is DefaultEditText) {

            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showError(null)
                }
                updateVisuals()
            }

            editText.addTextChangedListener {
                showError(null)
                updateVisuals()
            }

            post { updateVisuals() }
        }
    }

    fun showError(message: String?) {
        if (!message.isNullOrEmpty()) {
            // Show red error hint color
            defaultHintTextColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.inputErrorColor))
            hint = message
            isErrorShowing = true
            updateVisuals()
        } else {
            // Let visuals determine the appropriate color
            hint = originalHint
            isErrorShowing = false
            updateVisuals()
        }
    }

    private fun updateVisuals() {
        val editText = editText
        if (editText is DefaultEditText) {
            val isFilledWithoutError = !editText.text.isNullOrEmpty() && !isErrorShowing
            val color = when {
                editText.isFocused && (isFilledWithoutError || editText.text.isNullOrEmpty()) -> focusedColor
                isFilledWithoutError -> focusedColor
                isErrorShowing -> ContextCompat.getColor(context, R.color.inputErrorColor)
                else -> defaultColor
            }

            defaultHintTextColor = ColorStateList.valueOf(color)
            editText.background = when {
                editText.isFocused && isFilledWithoutError -> underlineFilled
                isErrorShowing -> underlineError
                else -> underlineDefault
            }
        }
    }

}
