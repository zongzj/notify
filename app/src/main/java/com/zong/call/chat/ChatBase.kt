package com.zong.call.chat

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Build
import cn.vove7.andro_accessibility_api.ext.ScreenTextFinder
import cn.vove7.andro_accessibility_api.viewnode.ViewNode
import com.blankj.utilcode.util.ScreenUtils
import com.zong.call.utils.TTS
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.LinkedBlockingQueue

open class ChatBase {
    private val TAG = "ChatBase"
    var isCanScroll = false

    val mNodeQueue: LinkedBlockingQueue<ViewNode> = LinkedBlockingQueue<ViewNode>()
    lateinit var baseService: MyAccessibilityService
    open fun showView(baseService: MyAccessibilityService) {
        this.baseService = baseService
    }

    open fun putMsg() {
        AccessibilityUtils.launchWithExpHandler {
            val ts = ScreenTextFinder().find(false)
            ts.forEach {
                mNodeQueue.add(it)
            }
            if (mNodeQueue.size == 0) {
//                "获取不到文字或者不支持当前页面，仅支持群聊界面"
            }
        }

    }

    open fun readMsg() {
        if (mNodeQueue.isNullOrEmpty()) {
            return
        }
        var viewNode = mNodeQueue.poll()
        var text = viewNode.text
        TTS.instance.setSpeakStateListener(object : TTS.SpeakStateListener {
            override fun onStart() {

            }

            override fun onDone() {
            }
        })
        TTS.instance.speak(text as String)

    }

    open fun clearMsg() {
        mNodeQueue.clear()
    }

    open fun scrollScreen() {
        if (!isCanScroll) {
            return
        }

        //仿滑动
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val path = Path()
            path.moveTo(500f, ScreenUtils.getScreenHeight() * 0.85f) //设置Path的起点
            path.quadTo(400f, 800f, 500f, ScreenUtils.getScreenHeight() * 0.15f)
            val builder = GestureDescription.Builder()
            val description = builder.addStroke(GestureDescription.StrokeDescription(path, 100L, 1000L)).build()

            //100L 第一个是开始的时间，第二个是持续时间
            baseService.dispatchGesture(description, object : AccessibilityService.GestureResultCallback() {
                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)

                }

                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)

                    // TODO: 我是真聪明， 翻页虽然结束，界面绘制虽然结束，但是获取的view不是最后结果，需要异步sleep 不然会阻塞主线程
                    GlobalScope.async {
                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                        putMsg()
                    }

//                    LogUtils.d(TAG + "=============翻页结束")
                }

            }, null)
        }
    }


}