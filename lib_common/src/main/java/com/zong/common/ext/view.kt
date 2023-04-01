package com.zong.common.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * @author administrator
 */
fun View.hideSoftInputFromWindow() {
    GlobalScope.launch {
        delay(100)
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(
                this@hideSoftInputFromWindow.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone(boolean: Boolean = true) {
    visibility = if (!boolean) View.VISIBLE else View.GONE
}

fun View.invisible(boolean: Boolean = true) {
    visibility = if (!boolean) View.VISIBLE else View.INVISIBLE
}

fun View.showSoftInput() {

    requestFocus()
    GlobalScope.launch {
        delay(100)
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(
                this@showSoftInput,
                InputMethodManager.SHOW_IMPLICIT
            )
    }
}

fun <V : View> V.btRipple() {
    post {
        val colorInt = Color.parseColor("#991B6BCD")
        val d = ColorDrawable(Color.parseColor("#771B6BCD"))
        val e = ColorDrawable(Color.parseColor("#991B6BCD"))
        val rippleColor = ColorStateList.valueOf(colorInt)

        val drawable = RippleDrawable(rippleColor, d, e)
        drawable.isAutoMirrored = false
        drawable.setColor(rippleColor)
        drawable.setHotspotBounds(0, 0, measuredWidth, measuredHeight)
        drawable.setHotspot(measuredWidth / 2F, measuredHeight / 2F)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.radius = min(measuredWidth / 2, measuredHeight / 2)
        }
        background = drawable
    }
}

fun <V : View> V.imageRipple() {

    if (this is ImageView) {
        post {
            val colorInt = Color.parseColor("#55B65A")
            val rippleColor = ColorStateList.valueOf(colorInt)
            val d = ColorDrawable(Color.parseColor("#00FFFFFF"))
            val e = ColorDrawable(Color.parseColor("#70FFFFFF"))
            val drawable = RippleDrawable(rippleColor, d, e)
            drawable.isAutoMirrored = false
            drawable.setColor(rippleColor)
            drawable.setHotspotBounds(0, 0, measuredWidth, measuredHeight)
            drawable.setHotspot(measuredWidth / 2F, measuredHeight / 2F)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                drawable.radius = min(measuredWidth / 2, measuredHeight / 2)
            }
            background = drawable
        }

    } else {

        post {
            val colorInt = Color.parseColor("#55B65A")
            val rippleColor = ColorStateList.valueOf(colorInt)
            val d = ColorDrawable(Color.parseColor("#FFFFFFFF"))
            val e = ColorDrawable(Color.parseColor("#50FFFFFF"))
            val drawable = RippleDrawable(rippleColor, d, e)
            drawable.isAutoMirrored = false
            drawable.setColor(rippleColor)
            drawable.setHotspotBounds(0, 0, measuredWidth, measuredHeight)
            drawable.setHotspot(measuredWidth / 2F, measuredHeight / 2F)
            background = drawable
        }

    }
}

fun <V : View> V.textRipple() {
    post {
        val colorInt = Color.parseColor("#50CCCCCC")
        val rippleColor = ColorStateList.valueOf(colorInt)
        val d = ColorDrawable(Color.parseColor("#FFFFFFFF"))
        val e = ColorDrawable(Color.parseColor("#505533FF"))
        val drawable = RippleDrawable(rippleColor, d, e)
        drawable.isAutoMirrored = false
        drawable.setColor(rippleColor)
        drawable.setHotspotBounds(0, 0, measuredWidth, measuredHeight)
        drawable.setHotspot(measuredWidth / 2F, measuredHeight / 2F)
        background = drawable
    }


}

fun <V : TextView> V.textcolor(@ColorRes color: Int): V {
    val colorInt = ContextCompat.getColor(context, color)
    setTextColor(colorInt)
    return this
}

fun <V : TextView> V.textcolor(color: String): V {
    setTextColor(Color.parseColor(color))
    return this
}

fun <V : TextView> V.textsize(size_dp: Int): V {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size_dp.toFloat())
    return this
}

fun <V : TextView> V.hint(text: String): V {
    this.hint = text
    return this
}

fun <V : Button> V.tintColor(@ColorRes color: Int): V {
    val colorInt = ContextCompat.getColorStateList(context, color)
    backgroundTintList = colorInt
    backgroundTintMode = PorterDuff.Mode.SRC
    return this
}
/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}




