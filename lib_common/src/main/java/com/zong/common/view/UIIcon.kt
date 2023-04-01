package com.android.common.view

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener

/**
 * @author administrator
 */
class UIIcon(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    constructor(context: Context) : this(context, null)


    init {
        visibility = View.GONE
    }

    fun bindPassword(
        editText: EditText,
        @DrawableRes passwordShowImage: Int,
        @DrawableRes passwordGoneImage: Int,
        always: Boolean = false
    ) {
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        if (!always) {
            editText.addTextChangedListener {
                visibility = if (it?.isEmpty() == true) View.GONE else View.VISIBLE
            }
        } else {
            visibility = View.VISIBLE
        }
        setImageResource(passwordGoneImage)
        setOnClickListener {
            if (editText.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                editText.transformationMethod = PasswordTransformationMethod.getInstance()
                setImageResource(passwordGoneImage)
            } else {
                editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                setImageResource(passwordShowImage)
            }
        }
    }

    fun bindClear(editText: EditText, @DrawableRes clearResId: Int) {
        editText.addTextChangedListener {
            visibility = if (it?.isEmpty() == true) View.GONE else View.VISIBLE
        }
        setImageResource(clearResId)
        setOnClickListener {
            editText.setText("")
        }
    }


}