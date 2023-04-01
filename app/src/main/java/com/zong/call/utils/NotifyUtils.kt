package com.zong.call.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import com.blankj.utilcode.util.RomUtils
import com.zong.call.service.ForegroundService

object NotifyUtils {
    private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

    //开启其中某个功能时，需要打开通知栏
    fun open(activity: Context) {
        if (isEnabledNotify(activity)) {
            if (RomUtils.isVivo() || RomUtils.isOppo()) {
                if (NotifyOPPOUtil.isOPPONotificationEnabled(activity)) {
                    startService(activity)
                }
            } else {
                startService(activity)
            }
        }

    }

    private fun startService(context: Context) {
        ForegroundService.start(context)
    }

    fun closeNotify(context: Context) {
        ForegroundService.stop(context)
    }

    fun requestPermission(activity: Activity, requestCode: Int) {
//        SpeakUtils.newInstance().speak("需要授予语音王通知栏权限")
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * 判断是否已打开服务
     *
     * @return true(打开) false(关闭)
     */
    fun isEnabledNotify(context: Context): Boolean {
        val pkgName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver,
            ENABLED_NOTIFICATION_LISTENERS)
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).toTypedArray()
            for (name in names) {
                val cn = ComponentName.unflattenFromString(name)
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }


}

