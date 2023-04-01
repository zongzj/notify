package com.zong.common.base.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.zong.common.ext.inflateBindingWithGeneric
import com.zong.common.ext.toastOnUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.anim.DefaultNoAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 *  author: zzj
 *  time: 2023/2/25
 *  deprecated :
 */
abstract class BaseActivity<VB : ViewBinding> : SupportActivity() , CoroutineScope by MainScope() {

    private var dark = true
    val TAG = this.javaClass.name
    protected lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateBindingWithGeneric(layoutInflater)
        binding?.let {
            setContentView(it.root)
        }
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //设定为竖屏
        }

        initIntent(intent)
        initUi()
        uiInteraction()
        observerOnUi()

    }

    /**
     * 全面屏展示 隐藏状态栏
     */
    protected fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val window = window
            val wl = window.attributes
            wl.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = wl
            val view = window.decorView
            view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /**
     * 全面屏展示 显示状态栏
     */
    protected fun showStatusBar() {

        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

    }

    /**
     * 获取intent 传递的数据
     */
    abstract fun initIntent(intent: Intent)

    /**
     * 初始化UI
     */
    abstract fun initUi()

    /**
     * UI交互
     */
    abstract fun uiInteraction()

    /**
     * 通过liveData 更新UI
     */
    abstract fun observerOnUi()
    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultNoAnimator()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    fun String.show() {
        toastOnUi(this)
    }

}
