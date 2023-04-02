package com.zong.call.service

import android.app.Notification
import android.content.ComponentName
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import android.util.Log
import com.zong.call.constant.IntentAction
import com.zong.call.db.appDb
import com.zong.call.db.entity.ModeBean
import com.zong.call.db.entity.NotifyBean
import com.zong.call.utils.InstalledAPPUtils.reportModeBean
import com.zong.call.utils.TTS
import com.zong.common.ext.ensureBackgroundThread
import com.zong.common.ext.runOnUI
import com.zong.common.ext.startService
import com.zong.common.utils.MMKVUtil
import splitties.init.appCtx

/**
 * 状态栏信息监听服务类
 */
class NotifyService : NotificationListenerService() {
    private val TAG = "NotifyService"

    private var packageName: String = ""
    private var title = ""
    private var content: String = ""

    companion object {
        fun toggleNotificationListenerService() {
            val pm: PackageManager = appCtx.packageManager
            pm.setComponentEnabledSetting(
                ComponentName(appCtx, NotifyService::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            pm.setComponentEnabledSetting(
                ComponentName(appCtx, NotifyService::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        }

    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {}
    override fun onNotificationRemoved(sbn: StatusBarNotification) {}
    override fun onNotificationPosted(sbn: StatusBarNotification, rankingMap: RankingMap) {
        Log.e("AAA", "=2==onNotificationPosted   ID :"
                + sbn.getId() + "\t"
                + sbn.getNotification().tickerText + "\t"
                + sbn.getPackageName());
        if (sbn.isOngoing) {
            return
        }
        sbn?.notification?.extras?.apply {
            packageName = sbn.packageName
            title = getString(Notification.EXTRA_TITLE, "")
            // 获取通知内容
            content = getString(Notification.EXTRA_TEXT, "")
        }
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(title)) {
            return
        }

//        val modeBean: ModeBean = reportModeBean
        /**
         * 1.当前模式选中
         * 2.在播报时间内 ，，，日期
         * 3.能查到播报app
         * 4.关键字过滤
         */
        /**
         * 1.当前模式选中
         * 2.在播报时间内 ，，，日期
         * 3.能查到播报app
         * 4.关键字过滤
         */
        var modeBean = appDb.modeDao.getAllByFilter(packageName)
        if (modeBean != null) {
            modeBean.appList?.let { apps ->
                apps.forEach {
                    if (it.packageName == packageName) {
                        if (!isConstantWords(content) && modeBean.isReportTime()) {
                            TTS.instance.speak(it.appName + "播报" + content)
                        }
                        appDb.notifyDao.insert(NotifyBean.toNotifyBean(it, title, content))
                        startService<ForegroundService>() {
                            action = IntentAction.updateNotify
                        }
                    }
                }
            }

        }

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification, rankingMap: RankingMap) {}

    //黑名单
    private fun isConstantWords(text: String): Boolean {
        val words: String = MMKVUtil.getString("words")
        if (TextUtils.isEmpty(words)) {
            return false
        }
        val split = words.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (s in split) {
            if (text.contains(s)) {
                return true
            }
        }
        return false
    }


}