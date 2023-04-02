package com.zong.call.bean

import android.app.usage.UsageStats

class InstalledApp  {
    var packageName: String = ""
    var appName: String = "66"
    var index: String = ""//字母下标
    var versionName: String = ""
    var firstInstallTime: Long = 0
    var lastUpdateTime: Long = 0
    var coreApp: Boolean = false
    var isUnLock: Boolean = false//是否解锁
    var isSelect: Boolean = false//
    var versionCode: Int = 0
    var icon: String = ""

    override fun toString(): String {
        return "InstalledApp(packageName='$packageName', appName='$appName', versionName='$versionName', firstInstallTime=$firstInstallTime, lastUpdateTime=$lastUpdateTime, coreApp=$coreApp, isSelect=$isSelect, versionCode=$versionCode, icon=$icon)"
    }


    companion object {
        var map = hashMapOf<String, InstalledApp>()//限制的app
        var usageStatsMap = mapOf<String, UsageStats>()//限制的app
    }
}


