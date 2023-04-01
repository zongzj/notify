package com.zong.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.android.common.view.helper.IBadge
import com.google.android.material.textview.MaterialTextView
import com.zong.common.ext.dp

/**
 * @author administrator
 */
open class UITextView(context: Context, attrs: AttributeSet?) : MaterialTextView(context, attrs) {

    constructor(context: Context) : this(context, null)


    //下划线高度(单位像素)
    private var bottomLineHeight = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        paint.strokeWidth = bottomLineHeight.toFloat()
        paint.color = Color.TRANSPARENT
    }

    private var badge: IBadge? = null

    fun setBadge(badge: IBadge) {
        this.badge = badge
        invalidate()
    }

    fun getBadge() = badge

    private var leftP = 0
    private var rightP = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        badge?.apply {
            drawBadge(canvas, this@UITextView)
        }

        canvas?.drawLine(
            0F + leftP,
            height.toFloat(),
            width.toFloat() - rightP,
            height.toFloat(),
            paint
        )

    }


    //设置下划线颜色
    fun setLine(
        @ColorRes hasFocusColor: Int, lineHeight: Int = 0.5.dp, leftP: Int = 0,
        rightP: Int = 0
    ) {
        paint.color = ContextCompat.getColor(context, hasFocusColor)
        paint.strokeWidth = lineHeight.toFloat()
        this.leftP = leftP
        this.rightP = rightP
        invalidate()
    }

    //设置下划线颜色
    fun setLine(
        hasFocusColor: String, lineHeight: Int = 0.5.dp
        , leftP: Int = 0,
        rightP: Int = 0
    ) {
        paint.color = Color.parseColor(hasFocusColor)
        paint.strokeWidth = lineHeight.toFloat()
        this.leftP = leftP
        this.rightP = rightP
        invalidate()
    }


}
