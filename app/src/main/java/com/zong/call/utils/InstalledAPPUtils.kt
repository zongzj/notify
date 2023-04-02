package com.zong.call.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Environment
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.Gson
import com.zong.call.BuildConfig
import com.zong.call.bean.InstalledApp
import com.zong.call.db.entity.ModeBean
import com.zong.call.constant.Constant
import com.zong.common.ext.ensureBackgroundThread
import com.zong.common.utils.LogUtils
import com.zong.common.utils.MMKVUtil
import java.io.File
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object InstalledAPPUtils {
    private const val TAG = "InstalledAPPUtils"

    //播报的模式
    var reportModeBean = ModeBean();
    var externalStorageDirectory: File? = null

    //    fun getAPPList(context: Context): MutableList<InstalledApp>? {
//        var installedAppList: MutableList<InstalledApp> = ArrayList()
//        var json: String = MMKVUtil.getString(Constant.INSTALL_APP)
//        if (json.isNotBlank()) {
//            LogUtils.d(TAG,"app${json}")
//            installedAppList = json.toBeanList()
//            var count = MMKVUtil.getInt(Constant.UNLOCK_NUM)
//            if (count>=3){
//                installedAppList.forEach {
//                    it.isUnLock=true
//                }
//            }
//
//        } else {
//            installedAppList = queryFromDevice(context)
//
//        }
//
//        return installedAppList
//    }
    fun getAPPList(context: Context, callback: (list: MutableList<InstalledApp>) -> Unit) {

        ensureBackgroundThread {
            callback.invoke(queryFromDevice(context))
        }
    }

    fun queryFromDevice(
        context: Context
    ): MutableList<InstalledApp> {
        var installedAppList: MutableList<InstalledApp> = ArrayList()
        var ai: ApplicationInfo
        externalStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var app: InstalledApp
        try {
            val pm = context.packageManager
            //            兼容安卓11
            //            val packageInfos: List<PackageInfo> = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES)
            //            LogUtils.d("getAppsInfo.size${AppUtils.getAppsInfo().size}")
            val intent = Intent("android.intent.action.MAIN", null as Uri?)
            intent.addCategory("android.intent.category.LAUNCHER")
            val queryIntentActivities: List<ResolveInfo> = pm.queryIntentActivities(intent, 0)
            var appName: String

            for (queryIntentActivity in queryIntentActivities) {
                val info: PackageInfo =
                    pm.getPackageInfo(queryIntentActivity.activityInfo.packageName, 0)
                appName = AppUtils.getAppName(info.packageName)
                app = InstalledApp()
                app.packageName = info.packageName
                app.appName = appName
                app.isSelect = false
                app.index = ToPinYin.getFirstAlpha(appName)
                app.versionCode = info.versionCode
                ai = info.applicationInfo
                app.icon = "${externalStorageDirectory.toString()}/${appName}" + ".png"
                IconUtil.saveIcon(ai.loadIcon(pm), externalStorageDirectory.toString(), appName)
                if (!info.packageName.equals(BuildConfig.APPLICATION_ID)) {//排除自己
                    installedAppList.add(app)
                }

//                LogUtils.d("app${app.toString()}")
            }

            installedAppList.sortBy { it.index }
            MMKVUtil.putString(Constant.INSTALL_APP, GsonUtils.toJson(installedAppList))

        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return installedAppList
    }

}

inline fun <reified T> String.toBeanList(): MutableList<T> =
    Gson().fromJson(this, ParameterizedTypeImpl(T::class.java))

class ParameterizedTypeImpl(val clz: Class<*>) : ParameterizedType {
    override fun getRawType(): Type = MutableList::class.java

    override fun getOwnerType(): Type? = null

    override fun getActualTypeArguments(): Array<Type> = arrayOf(clz)
}
