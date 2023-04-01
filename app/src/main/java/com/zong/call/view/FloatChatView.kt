package com.zong.call.view

import android.app.Activity
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import cn.vove7.andro_accessibility_api.ext.ScreenTextFinder
import cn.vove7.andro_accessibility_api.viewnode.ViewNode
import com.blankj.utilcode.util.ScreenUtils
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnFloatCallbacks
import com.zong.common.utils.LogUtils
import com.zong.call.R
import com.zong.call.utils.MMKVUtils
import com.zong.call.utils.TTS
import java.util.concurrent.LinkedBlockingQueue

object FloatChatView {
    private const val TAG = "FloatChatView"
    private var y = (ScreenUtils.getScreenHeight() - 600) / 3
    var isCanScroll = false

    val mNodeQueue: LinkedBlockingQueue<ViewNode> = LinkedBlockingQueue<ViewNode>()

    fun show(activity: Activity) {
        if (EasyFloat.isShow(TAG)) {
            return
        }
        EasyFloat.with(activity)
            .registerCallbacks(object : OnFloatCallbacks {
                override fun createdResult(isCreated: Boolean, msg: String?, view: View?) {
                    view?.findViewById<Button>(R.id.bt_quit)?.setOnClickListener {
                        MMKVUtils.sw_read_chat = false
                        close()
                    }
                    view?.findViewById<Button>(R.id.bt_stop)?.apply {
                        setOnClickListener {
                            isCanScroll = !isCanScroll
                            if (isCanScroll) {
                                this.text = "点击停止"
                                putMsg()

                            } else {
                                this.text = "点击开始"
                                clearMsg()

                            }
                        }
                    }

                }

                override fun dismiss() {

                }

                override fun drag(view: View, event: MotionEvent) {

                }

                override fun dragEnd(view: View) {

                }

                override fun hide(view: View) {

                }

                override fun show(view: View) {

                }

                override fun touchEvent(view: View, event: MotionEvent) {

                }
            })
            .setShowPattern(ShowPattern.ALL_TIME)
            .setTag(TAG)
            .setDragEnable(true)
            .setGravity(Gravity.END or Gravity.TOP)
            .setSidePattern(SidePattern.TOP)
//                .setLocation(ScreenUtils.getScreenWidth() - 75, y)
            .setLayout(R.layout.float_chat_view)
            .show()

    }

    open fun putMsg() {
        AccessibilityUtils.launchWithExpHandler {
            val ts = ScreenTextFinder().find(false)
            ts.forEach {
                it?.text?.apply {
                    LogUtils.d(TAG, it.text.toString())
                    mNodeQueue.add(it)
                }
            }
            if (mNodeQueue.size == 0) {
//                "获取不到文字或者不支持当前页面，仅支持群聊界面"
            }
            readMsg()
        }

    }

    open fun readMsg() {
        jobRead.start()
    }

  val jobRead  by lazy {
   AccessibilityUtils.launchWithExpHandler {

        while (true) {
            if (mNodeQueue.size > 0 && !TTS.instance.isSpeaking) {
                var viewNode = mNodeQueue.poll()
                var text = viewNode.text
                TTS.instance.setSpeakStateListener(object : TTS.SpeakStateListener {
                    override fun onStart() {
                    }
                    override fun onDone() {
                        //消息空了。滚屏，
                        //滚动多少，怎么计算 bounds
                        if (mNodeQueue.isEmpty()) {
                            LogUtils.d(TAG, "=都空了^_^=======翻页2");
                            AccessibilityUtils.scrollScreen(isCanScroll) {
                                putMsg()
                            }
                        } else {
                            readMsg()

                        }
                    }
                })
                TTS.instance.speak(text.toString())
            } else if (mNodeQueue.isEmpty()) {
                LogUtils.d(TAG, "=都空了^_^=======翻页2");
                AccessibilityUtils.scrollScreen(isCanScroll) {
                    putMsg()
                }
            }else{
                Thread.sleep(1000)
            }
            Thread.sleep(100)

        }
    }
  }

    open fun clearMsg() {
        mNodeQueue.clear()
    }

    fun close() {
        jobRead.cancel()
        clearMsg()
        EasyFloat.dismiss(TAG)
    }

}