package com.zong.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged

/**
 * @author administrator
 */
class UIEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    constructor(context: Context) : this(context, null)

    private var focus = false

    init {
        setOnFocusChangeListener { _, hasFocus ->
            this.focus = hasFocus
            invalidate()
        }
    }

    private var linePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var noFocusHeight = 0
    private var hasFocusHeight = 0
    private var hasFocusColor = Color.parseColor("#00FFFFFF")
    private var noFocusColor = Color.parseColor("#00FFFFFF")

    private var leftP = 0
    private var rightP = 0

    /**
     * 当输入框获取焦点时，线的颜色大小都会改变
     */
    fun setLine(
        @ColorRes hasFocusColor: Int,
        @ColorRes noFocusColor: Int = hasFocusColor,
        noFocusHeight: Int = 1,
        hasFocusHeight: Int = 1,
        leftP: Int = 0,
        rightP: Int = 0
    ) {
        this.noFocusHeight = noFocusHeight
        this.hasFocusHeight = hasFocusHeight
        this.hasFocusColor = ContextCompat.getColor(context, hasFocusColor)
        this.noFocusColor = ContextCompat.getColor(context, noFocusColor)
        this.leftP = leftP
        this.rightP = rightP
        invalidate()
    }

    /**
     * 当输入框获取焦点时，线的颜色大小都会改变
     */
    fun setLine(
        hasFocusColor: String,
        noFocusColor: String = hasFocusColor,
        noFocusHeight: Int = 1,
        hasFocusHeight: Int = 1,
        leftP: Int = 0,
        rightP: Int = 0
    ) {
        this.noFocusHeight = noFocusHeight
        this.hasFocusHeight = hasFocusHeight
        this.hasFocusColor = Color.parseColor(hasFocusColor)
        this.noFocusColor = Color.parseColor(noFocusColor)
        this.leftP = leftP
        this.rightP = rightP
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (focus) {
            linePaint.color = hasFocusColor
            linePaint.strokeWidth = hasFocusHeight.toFloat()
        } else {
            linePaint.color = noFocusColor
            linePaint.strokeWidth = noFocusHeight.toFloat()
        }

        canvas?.drawLine(
            0f + leftP,
            height.toFloat(),
            width.toFloat() - rightP,
            height.toFloat(),
            linePaint
        )
    }

    private var defaultTextSize = 0f


    /**
     * 设置输入框有文字时的文字大小)
     * 设置此属性每当输入框有内容时，文字大小将变成
     * @param hasTextSize 的大小
     */
    fun setHasTextSize(@DimenRes hasTextSize: Int) {
        defaultTextSize = textSize
        doAfterTextChanged {
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                if (it?.length == 0) defaultTextSize else resources.getDimension(hasTextSize)
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            this.parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(event)
    }


    fun setMobile() {
        addTextChangedListener(mobileWatcher)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(mobileWatcher)

    }

    private val mobileWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            val string = s?.toString().orEmpty()
            if (string.isNotEmpty() && string.substring(string.length - 1, string.length) == " ") {
                setText(string.substring(0, string.length - 1))
                setSelection(string.length - 1)
                return
            }
            if (string.length == 9) {
                addTrim(string, 9)
                return
            }
            if (string.length == 4) {
                addTrim(string, 4)
                return
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    private fun addTrim(string: String, index: Int) {
        val start = string.substring(0, index - 1)
        val end = string.subSequence(index - 1, string.length)
        val builder = StringBuilder().append(start)
            .append(" ")
            .append(end)
        Log.d("afterTextChanged", builder.toString())
        setText(builder.toString())
        setSelection(builder.length)

    }

    fun getInputTrim() = text?.toString().orEmpty().replace(" ", "")


}
