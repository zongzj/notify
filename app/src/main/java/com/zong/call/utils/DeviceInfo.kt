package com.zong.call.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.RomUtils
import com.zong.common.utils.LogUtils
import com.zong.common.utils.MMKVUtil
import java.lang.Exception

object DeviceInfo {
    private const val TAG = "DeviceInfo"
    var mode: String = ""
    var sn: String = ""
    var os: String = ""
    var MANUFACTURER: String = ""

    init {


        if (!RomUtils.isHuawei()) {
            sn = DeviceUtils.getUniqueDeviceId(true)
        }
        os = DeviceUtils.getSDKVersionName()
        if (!MMKVUtil.getString("BRAND").isNullOrEmpty()) {
            mode = MMKVUtil.getString("BRAND")
        } else {
            mode = Build.BRAND + "_" + Build.MODEL
            MMKVUtil.putString("BRAND", mode)
        }

        if (!MMKVUtil.getString("MANUFACTURER").isNullOrEmpty()) {
            MANUFACTURER = MMKVUtil.getString("MANUFACTURER")
        } else {
            MANUFACTURER = Build.MANUFACTURER
            MMKVUtil.putString("MANUFACTURER", MANUFACTURER)
        }
        LogUtils.d(TAG,"获取敏感权限一次")
    }

    fun getMyAndroidId(context: Context): String {
        var id = ""
        try {
            id = MMKVUtil.getString("androidId")
            if (TextUtils.isEmpty(id)) {
                id = Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)
                MMKVUtil.putString("androidId", id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sn = id
        return id
    }

}