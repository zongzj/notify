package com.zong.common.ext

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * @author administrator
 */

fun View.setGradientDrawable(
    @ColorRes color: Int,
    radius: Int = 4.dp,
    @ColorRes strokeColor: Int = 0,
    strokeWidth: Int = 0,
    block: (GradientDrawable) -> Unit = {}
) {

    val drawable = GradientDrawable()

    drawable.color = ColorStateList.valueOf(ContextCompat.getColor(context, color))
    drawable.cornerRadius = radius.toFloat()
    if (strokeWidth != 0) {
        drawable.setStroke(strokeWidth, ContextCompat.getColor(context, strokeColor))
    }
    block.invoke(drawable)
    background = drawable

}

fun View.setGradientDrawable(
    @ColorRes color: Int,
    radius: Int = 4.dp,
    strokeColor: String,
    strokeWidth: Int = 0.5.dp,
    block: (GradientDrawable) -> Unit = {}
) {

    val drawable = GradientDrawable()
    drawable.color = ColorStateList.valueOf(ContextCompat.getColor(context, color))
    drawable.cornerRadius = radius.toFloat()
    drawable.setStroke(strokeWidth, Color.parseColor(strokeColor))
    block.invoke(drawable)
    background = drawable
}

fun View.setGradientDrawable(
    color: String,
    radius: Int = 4.dp,
    strokeColor: String = "#FFFFFF",
    strokeWidth: Int = 0,
    block: (GradientDrawable) -> Unit = {}
) {

    val drawable = GradientDrawable()
    drawable.color = ColorStateList.valueOf(Color.parseColor(color))
    drawable.cornerRadius = radius.toFloat()
    drawable.setStroke(strokeWidth, Color.parseColor(strokeColor))
    block.invoke(drawable)
    background = drawable
}



