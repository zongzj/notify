package com.zong.common.view.helper

import android.graphics.*
import android.view.View
import android.view.ViewGroup
import com.android.common.view.helper.IBadge
import com.zong.common.ext.sp

/**
 * @author administrator
 */
class DrawBadge(
    private val size: Int = 30,
    private val bgColor: Int = Color.RED,
    private val numberColor: Int = Color.WHITE,
    private val numberSize: Int = 11.sp,
    private val offsetX: Int = 0,
    private val offsetY: Int = 0
) : IBadge {

    companion object {
        //圆圈位置
        const val TOP_END = 1
        const val TOP_START = 2
        const val BOTTOM_END = 3
        const val BOTTOM_START = 4
    }

    //圆圈画笔
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    //数字画笔
    private val numberPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var number = ""


    private lateinit var view: View

    private val textRect = Rect()

    override fun drawBadge(canvas: Canvas?, view: View) {
        this.view = view
        clipToChild(view)
        canvas?.let {
            paint.color = bgColor

            rectF.right = view.width.toFloat() + offsetX
            rectF.left = view.width - size.toFloat() + offsetX

            rectF.top = 0F + offsetY
            rectF.bottom = size.toFloat() + offsetY

            canvas.drawRoundRect(rectF, size.toFloat(), size.toFloat(), paint)

            if (number.isNotEmpty() && number != "0") {
                numberPaint.color = numberColor
                numberPaint.textSize = numberSize.toFloat()
                numberPaint.textAlign = Paint.Align.CENTER
                val numberX = (rectF.left + rectF.right) / 2

                val mrtrices = numberPaint.fontMetrics

                val numberY = (rectF.bottom) + ((mrtrices.top + mrtrices.bottom)/2)

                canvas.drawText(number, numberX, numberY, numberPaint)

            }
        }
    }


    fun setNumber(number: String) {
        this.number = number
        invalidate()
    }

    fun clearNumber() {
        this.number = "0"
        invalidate()
    }

    private fun invalidate() {
        if (this::view.isInitialized) {
            view.invalidate()
        }
    }

    private fun clipToChild(view: View) {
        val parent = view.parent
        if (parent is ViewGroup) {
            parent.clipChildren = false
        }
    }

}
