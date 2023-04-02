package com.zong.call.service

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AppUtils
import com.zong.call.R
import com.zong.call.constant.AppConst
import com.zong.call.constant.AppConst.FOREGROUND_ID
import com.zong.call.constant.Constant
import com.zong.call.constant.IntentAction
import com.zong.call.db.appDb
import com.zong.call.ext.notificationManager
import com.zong.call.ui.app.ReportHistoryActivity
import com.zong.call.utils.DateUtil
import com.zong.call.utils.TTS
import com.zong.common.ext.servicePendingIntent
import com.zong.common.utils.MMKVUtil
import splitties.init.appCtx

class ForegroundService : Service() {
    private val TAG = ForegroundService::class.java.simpleName
    private val notificationBuilder by lazy {
        var mainPendingIntent: PendingIntent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mainPendingIntent = PendingIntent.getActivity(this, 0, getStartAppIntent(this), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            mainPendingIntent = PendingIntent.getActivity(this, 0, getStartAppIntent(this), PendingIntent.FLAG_ONE_SHOT)
        }

        NotificationCompat.Builder(this, AppConst.channelId)
            .setSmallIcon(R.mipmap.logo)
            .setOngoing(true)
            .setContentTitle(getString(R.string.keep_live))
            .setAutoCancel(false)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(mainPendingIntent)
            .setCustomContentView(getContentView(this))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    }

    companion object {

        fun start(context: Context) {
            ContextCompat.startForegroundService(context, context.serviceIntent())
            context.bindToService {
                startForeground(FOREGROUND_ID, notificationBuilder.build())
                NotifyService.toggleNotificationListenerService()
            }
        }

        fun stop(context: Context) {
            context.bindToService { stopSelf() }
        }

        private fun Context.serviceIntent() = Intent(this, ForegroundService::class.java)

        private fun Context.bindToService(block: ForegroundService.() -> Unit) {
            val connection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                    block((binder as DirectBinder).service)
                    unbindService(this)
                }

                override fun onServiceDisconnected(name: ComponentName) = Unit
            }

            bindService(serviceIntent(), connection, Context.BIND_AUTO_CREATE)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(FOREGROUND_ID, notificationBuilder.build())
//        }
        Log.d(TAG, "onStartCommand")
        intent?.action?.let {
            when (it) {
                IntentAction.notify -> {
                    startForeground(FOREGROUND_ID, notificationBuilder.build())
                }
                IntentAction.stop -> {
                    TTS.instance.stop()
                }
                IntentAction.updateNotify -> {
//                    upNotification()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?) = DirectBinder(this)

    class DirectBinder(val service: ForegroundService) : Binder()

    private fun getStartAppIntent(context: Context): Intent? {
        val intent: Intent? = context.packageManager
            .getLaunchIntentForPackage(AppUtils.getAppPackageName())
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        return intent
    }

    private var mRemoteViews: RemoteViews? = null

    /**
     * 获取自定义通知栏view
     *
     * @return
     */
    private fun getContentView(context: Context): RemoteViews? {
        mRemoteViews = RemoteViews(context.packageName, R.layout.layout_notify)
        mRemoteViews?.let {
            it.setTextViewText(R.id.tv_mode_name, titleInfo)
            it.setTextViewText(R.id.tv_info, todayReportCount())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                it.setOnClickPendingIntent(R.id.iv_stop_read, servicePendingIntent<ForegroundService>(IntentAction.stop))
                it.setOnClickPendingIntent(R.id.iv_history, PendingIntent.getActivity(context, 0, Intent(appCtx, ReportHistoryActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))

            } else {
                it.setOnClickPendingIntent(R.id.iv_stop_read, servicePendingIntent<ForegroundService>(IntentAction.stop))
                it.setOnClickPendingIntent(R.id.iv_history, PendingIntent.getActivity(context, 0, Intent(appCtx, ReportHistoryActivity::class.java), PendingIntent.FLAG_ONE_SHOT))

            }

        }
        return mRemoteViews
    }

    fun upNotification() {
        mRemoteViews?.let {
            it.setTextViewText(R.id.tv_mode_name, titleInfo)
            it.setTextViewText(R.id.tv_info, todayReportCount())
            appCtx.notificationManager!!.notify(FOREGROUND_ID, notificationBuilder.build())
        }
    }

    //目前不是今日播报
    fun todayReportCount(): String {
        return "【今日播报" + appDb.notifyDao.loadNotifyBeanDate(DateUtil.getCurrentYYDate()).size + "次】"
    }

    private val titleInfo: String
        private get() = "【" + MMKVUtil.getString(Constant.MODE_NAME) + "模式】播报守护中"
}

