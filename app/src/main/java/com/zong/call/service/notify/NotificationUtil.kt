package com.zong.call.service.notify

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.AppUtils
import com.zong.call.R

/**
 *  author: zzj
 *  time: 2023/3/6
 *  deprecated :
 */

object NotificationUtil {
    var notificationManager: NotificationManager? = null
    var notification: Notification? = null
    private const val notificationId = "1"
    private const val notificationName = "通知1"
    var FOREGROUND_ID = 1
    fun getNotification(context: Context): Notification? {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_LOW)
            notificationManager!!.createNotificationChannel(mChannel)
        }
        var builder: NotificationCompat.Builder? = null
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationCompat.Builder(context,notificationId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis()) //                    .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.logo)).getBitmap())
                .setContentTitle("珍惜时间") //远离奶头乐
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)) // //设置振动， 需要添加权限  <uses-permission android:name="android.permission.VIBRATE"/>
                .setDefaults(NotificationCompat.PRIORITY_LOW) //使用默认效果， 会根据手机当前环境播放铃声， 是否振动
                .setOngoing(true)
        } else {
            NotificationCompat.Builder(context, notificationId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("珍惜时间")
                .setDefaults(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
        }

        // 设置Notification的ChannelID,否则不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId)
        }
        notification = builder //                .setContentIntent(mainPendingIntent)
            .setAutoCancel(false).build()
        // TODO: 2021/9/25 下面这放开，有悬浮窗时，通知栏会提示，xxx显示在其他app上
//        notification.contentView = getContentView(context);
//        notificationManager.notify(NOTIFICATION_ID, notification);
        return notification
    }




}