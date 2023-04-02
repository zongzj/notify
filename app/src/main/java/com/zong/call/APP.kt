package com.zong.call

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.drake.net.NetConfig
import com.drake.net.cookie.PersistentCookieJar
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setRequestInterceptor
import com.zong.call.constant.Api
import com.zong.call.constant.AppConst.channelId
import com.zong.call.net.converter.SerializationConverter
import com.zong.call.net.interceptor.GlobalHeaderInterceptor
import com.zong.call.service.ForegroundService
import com.zong.common.BaseApp
import com.zong.common.utils.MMKVUtil
import me.yokeyword.fragmentation.Fragmentation
import okhttp3.Cache
import splitties.systemservices.notificationManager
import java.util.concurrent.TimeUnit

class APP : BaseApp() {

    companion object {
        val instance = Holder.holder

    }

    private object Holder {
        val holder = APP()
    }

    override fun onCreate() {
        super.onCreate()
        MMKVUtil.init(this)
        Fragmentation()
//        initNet()
        createNotificationChannels()
        ForegroundService.start(this)
    }

    private fun Fragmentation() {
        Fragmentation.builder() // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(false) // 实际场景建议.debug(BuildConfig.DEBUG)
            /**
             * 可以获取到[me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning]
             * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
             */
            .handleException {
                // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                // Bugtags.sendException(e);
            }
            .install()
    }

    private fun initNet() {
        NetConfig.initialize(Api.HOST, this) {

            // 超时设置
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)

            // 本框架支持Http缓存协议和强制缓存模式
            cache(Cache(cacheDir, 1024 * 1024 * 128)) // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小

            // LogCat是否输出异常日志, 异常日志可以快速定位网络请求错误
            setDebug(BuildConfig.DEBUG)

            // AndroidStudio OkHttp Profiler 插件输出网络日志
            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))

            // 添加持久化Cookie管理
            cookieJar(PersistentCookieJar(this@APP))

            // 通知栏监听网络日志
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    ChuckerInterceptor.Builder(this@APP)
                        .collector(ChuckerCollector(this@APP))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
            }

            // 添加请求拦截器, 可配置全局/动态参数
            setRequestInterceptor(GlobalHeaderInterceptor())

            // 数据转换器
            setConverter(SerializationConverter())

            // 自定义全局加载对话框
//            setDialogFactory {
//                BubbleDialog(it, "加载中....")
//            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            channelId,
            getString(R.string.keep_live),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(false)
            enableVibration(false)
            setSound(null, null)
        }
        //向notification manager 提交channel
        notificationManager.createNotificationChannels(listOf(channel))

    }
}