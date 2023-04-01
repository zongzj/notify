package com.zong.call.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.lang.Exception

object NotifyOPPOUtil {
    /**
     * 跳转到权限设置
     *
     * @param activity
     */
    fun toPermissionSetting(activity: Activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            toSystemConfig(activity)
        } else {
            try {
                toApplicationInfo(activity)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(activity, "异常了", Toast.LENGTH_SHORT).show()
                toSystemConfig(activity)
            }
        }
    }

    /**
     * 应用信息界面
     *
     * @param activity
     */
    fun toApplicationInfo(activity: Activity) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        localIntent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(localIntent)
    }

    /**
     * 系统设置界面
     *
     * @param activity
     */
    fun toSystemConfig(activity: Activity) {
        try {
            val intent = Intent(Settings.ACTION_SETTINGS)
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun requestSettings(activity: Activity) {
        try {
            if (isSettingsEnabled(activity)) {
                return
            }
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivityForResult(intent, 4)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isSettingsEnabled(context: Context?): Boolean {
        return if (context == null) {
            false
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            false
        }
    }

    fun isOPPONotificationEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context.applicationContext).areNotificationsEnabled()
    }

    fun openOPPOPush(activity: Activity) {

        if (Build.VERSION.SDK_INT >= 33) {
            var checkPermission =
            ContextCompat.checkSelfPermission(activity, "android.permission.POST_NOTIFICATIONS");
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                //动态申请
                ActivityCompat.requestPermissions(activity,  arrayOf("android.permission.POST_NOTIFICATIONS"), 100);
            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            intent.putExtra("app_package", activity.packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, activity.applicationInfo.uid)
            activity.startActivity(intent)
        } else {
            toPermissionSetting(activity)
        }
    }
}