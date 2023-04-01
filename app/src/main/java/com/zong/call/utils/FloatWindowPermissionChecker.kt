package com.zong.call.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat

/**
 * author: zzj
 * time: 2023/3/12
 * deprecated :
 * 1.通知栏内容权限
 * 2.悬浮窗权限
 */
object FloatWindowPermissionChecker {
    private const val TAG = "FloatPermissionChecker"

    //无法跳转的提示，应当根据不同的Rom给予不同的提示，比如Oppo应该提示去手机管家里开启，这里偷懒懒得写了
    private const val TOAST_HINT = "无法跳转至权限设置页面，请手动设置或向我们反馈"
    private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

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
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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

    /**
     * 悬浮窗权限判断
     *
     * @param context 上下文
     * @return [ true, 有权限 ][ false, 无权限 ]
     */
    @SuppressLint("ObsoleteSdkInt")
    fun checkAlertWindowsPermission(context: Context): Boolean {
        val hasPermission: Boolean
        hasPermission = if (Build.VERSION.SDK_INT < 19) {
            true
        } else if (Build.VERSION.SDK_INT < 23) {
            if (DeviceInfoUtil.isMiuiRom() || DeviceInfoUtil.isMeizuRom() || DeviceInfoUtil.isEmuiRom() || DeviceInfoUtil.is360Rom()) { // 特殊机型
                opPermissionCheck(context, 24)
            } else { // 其他机型
                true
            }
        } else { // 6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
            highVersionPermissionCheck(context)
        }
        return hasPermission
    }

    //悬浮窗
    fun startActivityForResult(activity: Activity, FLOAT_REQUEST_CODE: Int) {
        if (Build.VERSION.SDK_INT >= 23) { //6.0以上
            try {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivityForResult(intent, FLOAT_REQUEST_CODE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivityForResult(intent, FLOAT_REQUEST_CODE)
        }
    }

    /**
     * [19-23]之间版本通过[AppOpsManager]的权限判断
     *
     * @param context 上下文
     * @param op
     * @return [ true, 有权限 ][ false, 无权限 ]
     */
    private fun opPermissionCheck(context: Context, op: Int): Boolean {
        try {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val clazz: Class<*> = AppOpsManager::class.java
            val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
            return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
        } catch (ignored: Exception) {
        }
        return false
    }

    /**
     * Android 6.0 版本及之后的权限判断
     *
     * @param context 上下文
     * @return [ true, 有权限 ][ false, 无权限 ]
     */
    private fun highVersionPermissionCheck(context: Context): Boolean {
        if (DeviceInfoUtil.isMeizuRom()) { // 魅族6.0的系统单独适配
            return opPermissionCheck(context, 24)
        }
        try {
            val clazz: Class<*> = Settings::class.java
            val canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context::class.java)
            return canDrawOverlays.invoke(null, context) as Boolean
        } catch (ignored: Exception) {
        }
        return false
    }

    fun tryJumpToPermissionPage(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (DeviceInfoUtil.isMiuiRom()) {
                applyMiuiPermission(context)
            } else if (DeviceInfoUtil.isEmuiRom()) {
                applyHuaweiPermission(context)
            } else if (DeviceInfoUtil.isVivoRom()) {
                applyVivoPermission(context)
            } else if (DeviceInfoUtil.isOppoRom()) {
                applyOppoPermission(context)
            } else if (DeviceInfoUtil.is360Rom()) {
                apply360Permission(context)
            } else if (DeviceInfoUtil.isVivoRom()) {
                applyVivoPermission(context)
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
        } else {
            if (DeviceInfoUtil.isMeizuRom()) {
                applyMeizuPermission(context)
            } else {
                applyCommonPermission(context)
            }
        }
    }

    private fun startActivitySafely(intent: Intent, context: Context): Boolean {
        return if (isIntentAvailable(intent, context)) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            true
        } else {
            false
        }
    }

    private fun isIntentAvailable(intent: Intent?, context: Context): Boolean {
        return intent != null && context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size > 0
    }

    private fun showAlertToast(context: Context) {
        Toast.makeText(context, TOAST_HINT, Toast.LENGTH_LONG).show()
    }

    private fun applyCommonPermission(context: Context) {
        try {
            val clazz: Class<*> = Settings::class.java
            val field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION")
            val intent = Intent(field[null].toString())
            intent.data = Uri.parse("package:" + context.packageName)
            startActivitySafely(intent, context)
        } catch (e: Exception) {
            showAlertToast(context)
        }
    }

    private fun applyCoolpadPermission(context: Context) {
        val intent = Intent()
        intent.setClassName("com.yulong.android.seccenter", "com.yulong.android.seccenter.dataprotection.ui.AppListActivity")
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyLenovoPermission(context: Context) {
        val intent = Intent()
        intent.setClassName("com.lenovo.safecenter", "com.lenovo.safecenter.MainTab.LeSafeMainActivity")
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyZTEPermission(context: Context) {
        val intent = Intent()
        intent.action = "com.zte.heartyservice.intent.action.startActivity.PERMISSION_SCANNER"
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyLetvPermission(context: Context) {
        val intent = Intent()
        intent.setClassName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AppActivity")
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyVivoPermission(context: Context) {
        val intent = Intent()
        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.FloatWindowManager")
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyOppoPermission(context: Context) {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        intent.action = "com.oppo.safe"
        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity")
        if (!startActivitySafely(intent, context)) {
            intent.action = "com.color.safecenter"
            intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.floatwindow.FloatWindowListActivity")
            if (!startActivitySafely(intent, context)) {
                intent.action = "com.coloros.safecenter"
                intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity")
                if (!startActivitySafely(intent, context)) {
                    showAlertToast(context)
                }
            }
        }
    }

    private fun apply360Permission(context: Context) {
        val intent = Intent()
        intent.setClassName("com.android.settings", "com.android.settings.Settings\$OverlaySettingsActivity")
        if (!startActivitySafely(intent, context)) {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity")
            if (!startActivitySafely(intent, context)) {
                showAlertToast(context)
            }
        }
    }

    private fun applyMiuiPermission(context: Context) {
        val intent = Intent()
        intent.action = "miui.intent.action.APP_PERM_EDITOR"
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra("extra_pkgname", context.packageName)
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyMeizuPermission(context: Context) {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        intent.putExtra("packageName", context.packageName)
        if (!startActivitySafely(intent, context)) {
            showAlertToast(context)
        }
    }

    private fun applyHuaweiPermission(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            var comp = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity")
            intent.component = comp
            if (!startActivitySafely(intent, context)) {
                comp = ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity")
                intent.component = comp
                context.startActivity(intent)
            }
        } catch (e: SecurityException) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName("com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity")
            intent.component = comp
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName("com.Android.settings", "com.android.settings.permission.TabItem")
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            showAlertToast(context)
        }
    }

    private fun applySmartisanPermission(context: Context) {
        var intent = Intent("com.smartisanos.security.action.SWITCHED_PERMISSIONS_NEW")
        intent.setClassName("com.smartisanos.security", "com.smartisanos.security.SwitchedPermissions")
        intent.putExtra("index", 17) //有版本差异,不一定定位正确
        if (!startActivitySafely(intent, context)) {
            intent = Intent("com.smartisanos.security.action.SWITCHED_PERMISSIONS")
            intent.setClassName("com.smartisanos.security", "com.smartisanos.security.SwitchedPermissions")
            intent.putExtra("permission", arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW))
            if (!startActivitySafely(intent, context)) {
                showAlertToast(context)
            }
        }
    }

    /**
     * PERMISSION_GRANTED 0: 允许
     * PERMISSION_DENIED 1: 未允许
     *
     * @param activity
     * @param permission
     * @return
     */
    fun checkPermission(activity: Activity?, permission: List<String?>): Boolean {
        for (i in permission) {
            val hasPermission = ContextCompat.checkSelfPermission(activity!!, i!!)
            if (hasPermission != 0) {
                return false
            }
            break
        }
        return true
    }
}