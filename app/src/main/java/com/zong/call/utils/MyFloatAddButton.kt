package com.zong.call.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zong.common.ext.dp


class MyFloatAddButton : FloatingActionButton{

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var parentHeight = 0
    private var parentWidth = 0

    private var lastX = 0
    private var lastY = 0


    private var downX = 0
    private var downY = 0
    private var upX = 0
    private var upY = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rawX = event.rawX.toInt()
        val rawY = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val viewParent = parent
                run {
                    lastX = rawX
                    downX = lastX
                }
                run {
                    lastY = rawY
                    downY = lastY
                }
                if (viewParent != null) {
                    //请求父控件不中断事件
                    viewParent.requestDisallowInterceptTouchEvent(true)
                    val parent = viewParent as ViewGroup
                    //获取父控件的高度
                    parentHeight = parent.getHeight()
                    //获取父控件的宽度
                    parentWidth = parent.getWidth()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val dx: Int = rawX - lastX
                val dy: Int = rawY - lastY
                var x = x + dx
                var y = y + dy
                //检测是否到达边缘 左上右下
                x = when{
                    x<0 -> 0F
                    x > parentWidth - width -> parentWidth.toFloat() - width
                    else -> x
                }
                //控件距离底部的margin
                val bottomMargin = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    60f,
                    resources.displayMetrics
                ).toDouble()
                y = when{
                    y<120.dp -> 120.dp.toFloat()
                    y > parentHeight - height - bottomMargin.toFloat() -> parentHeight - height - bottomMargin.toFloat()
                    else -> y
                }
                setX(x)
                setY(y)
                lastX = rawX
                lastY = rawY
            }
            MotionEvent.ACTION_UP -> {
                upX = event.rawX.toInt()
                upY = event.rawY.toInt()
                val distanceX: Int =
                    Math.abs(Math.abs(upX) - Math.abs(downX))
                val distanceY: Int =
                    Math.abs(Math.abs(upY) - Math.abs(downY))
                //当手指按下的事件跟手指抬起事件之间的距离小于10时执行点击事件
                if (Math.max(distanceX, distanceY) <= 10) {
                    click.invoke(this)
                }
                moveHide(rawX)
            }
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
        //如果是拖拽则消s耗事件，否则正常传递即可。
        return true
        return super.onTouchEvent(event)
    }

    private fun moveHide(rawX: Int) {
        if (rawX >= parentWidth / 2) {

            //靠右吸附
//            animate().setInterpolator(DecelerateInterpolator())
//                .setDuration(500)
//                .xBy(parentWidth - width - x)
//                .start()

            val oa = ObjectAnimator.ofFloat(this, "x", x, parentWidth - width.toFloat())
            oa.interpolator = DecelerateInterpolator()
            oa.duration = 400
            oa.start()

        } else {
            //靠左吸附
            val oa = ObjectAnimator.ofFloat(this, "x", x, 0f)
            oa.interpolator = DecelerateInterpolator()
            oa.duration = 400
            oa.start()
        }
    }

    private var click: (View) -> Unit = {

    }

    fun setClick(click: (View) -> Unit) {
        this.click = click
    }

    override fun setX(x: Float) {
        super.setX(x)
        //Log.e("setX", x.toString())
//        when(x){
//            0F -> setBackgroundResource(R.drawable.home_tab_bg_left)
//            parentWidth.toFloat() - width -> setBackgroundResource(R.drawable.home_tab_bg_right)
//            else -> setBackgroundResource(R.drawable.home_tab_bg_center)
//        }

    }


}
