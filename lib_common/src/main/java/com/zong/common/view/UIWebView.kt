package com.zong.common.view

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.webkit.WebView


/**
 * @author administrator
 */

fun Context.getFixdsContext() : Context{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        return createConfigurationContext(Configuration())
    } else {
        return this
    }
}

class UIWebView : WebView {

    private var detector: GestureDetector? = null
    private var isScrollBottom = false


    constructor(context: Context) : super(context.getFixdsContext()){
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context.getFixdsContext(), attrs){
        init()
    }

    private fun init(){
//
//        detector = GestureDetector(context, object : SimpleOnGestureListener() {
//            override fun onDown(e: MotionEvent): Boolean {
//                if (!isScrollBottom) {
//                    requestDisallowInterceptTouchEvent(true)
//                } else {
//                    isScrollBottom = false
//                }
//                return false
//            }
//
//            override fun onScroll(
//                e1: MotionEvent,
//                e2: MotionEvent,
//                distanceX: Float,
//                distanceY: Float
//            ): Boolean {
//                val webcontent = contentHeight * scale
//                val webnow = height + scrollY.toFloat()
//                isScrollBottom = Math.abs(webcontent - webnow) < 1
//                return true
//            }
//        })


    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context.getFixdsContext(),
        attrs,
        defStyleAttr
    ){
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context.getFixdsContext(), attrs, defStyleAttr, defStyleRes){
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(context.getFixdsContext(), attrs, defStyleAttr, privateBrowsing){
        init()
    }

    fun loadData(data: String){
        super.loadData(data, "text/html; charset=UTF-8", null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //detector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


}
