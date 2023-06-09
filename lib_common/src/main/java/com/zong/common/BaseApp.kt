package com.zong.common

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import cat.ereza.customactivityoncrash.config.CaocConfig


open class BaseApp : Application(), ViewModelStoreOwner, Thread.UncaughtExceptionHandler {

    companion object {
        private lateinit var instance: BaseApp
    }

    private val appViewModelStore by lazy {
        ViewModelStore()
    }


    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }

    private var curtActivity: Activity? = null

    fun setCurtActivity(activity: Activity) {
        curtActivity = activity
    }

    fun getCurtActivity() = curtActivity

    private val activityList = arrayListOf<Activity>()

    /**
     * 关闭所有
     */
    fun finishAll() {
        activityList.forEach {
            if (it.window.exitTransition == null) {
                it.finish()
            } else {
                it.finishAfterTransition()
            }
        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        initErrorPage()
    }

    private fun initErrorPage() {
        //防止项目崩溃，崩溃后打开错误界面
//        CaocConfig.Builder.create()
//            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
//            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
//            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
//            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
//            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
//            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
//            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
////            .restartActivity(SplashActivity::class.java) // 重启的activity
////            .errorActivity(ErrorActivity::class.java) //发生错误跳转的activity
//            .apply()
    }


    override fun uncaughtException(p0: Thread, e: Throwable) {
        e.printStackTrace()
    }


}