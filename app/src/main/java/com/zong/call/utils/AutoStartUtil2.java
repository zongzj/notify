package com.zong.call.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.zong.call.BuildConfig;
import com.zong.call.R;


/**
 * 跳转自启动页面解决方案
 *
 * @author zhj
 */
public class AutoStartUtil2 {

    public static void open(Context context) {
        switch (DeviceInfo.INSTANCE.getMANUFACTURER()) {
            case "Xiaomi":
                openXiaomi(context);
                break;
            case "vivo":
                openVivo(context);
                break;
            case "OPPO":
                openOppo(context);
                break;
            case "HUAWEI":
                openHuawei(context);
                break;
            case "Meizu":
                openMeizu(context);
                break;
            case "samsung":
                openSamsung(context);
                break;
            default:
        }
    }

    public static void openHuawei(Context context) {
        ComponentName componentName = null;
        int sdkVersion = Build.VERSION.SDK_INT;

        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //跳自启动管理
            if (sdkVersion >= 28) {//9:已测试
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/com.huawei.systemmanager.mainscreen.MainScreenActivity");//跳自启动管理
            } else if (sdkVersion >= 26) {//8：已测试
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.appcontrol.activity.StartupAppControlActivity");
            } else if (sdkVersion >= 23) {//7.6：已测试
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");
            } else if (sdkVersion >= 21) {//5
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/com.huawei.permissionmanager.ui.MainActivity");
            }
            //componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");//锁屏清理
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {
            //跳转失败
        }

    }

    private static void openHour() {
    }

    public static void openVivo(Context context) {
        Intent localIntent;
        try {
            if (((Build.MODEL.contains("Y85")) && (!Build.MODEL.contains("Y85A"))) || (Build.MODEL.contains("vivo Y53L"))) {
                localIntent = new Intent();
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity");
                localIntent.putExtra("packagename", context.getPackageName());
                localIntent.putExtra("tabId", "1");
                context.startActivity(localIntent);
            } else {
                localIntent = new Intent();
                localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity");
                localIntent.setAction("secure.intent.action.softPermissionDetail");
                localIntent.putExtra("packagename", context.getPackageName());
                context.startActivity(localIntent);
            }
        } catch (Exception e) {
            // 否则跳转到应用详情
            localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            localIntent.setData(uri);
            context.startActivity(localIntent);
        }

    }

    private static void openOppo(Context context) {
        Intent intent = new Intent();
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("pkg_name", context.getPackageName());
                intent.putExtra("app_name", context.getString(R.string.app_name));
                intent.putExtra("class_name", "com.welab.notificationdemo.MainActivity");
                ComponentName comp = new ComponentName("com.coloros.notificationmanager", "com.coloros" +
                        ".notificationmanager.AppDetailPreferenceActivity");
                intent.setComponent(comp);
                context.startActivity(intent);
            } catch (Exception e1) {
                // 否则跳转到应用详情
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        }

    }

    private static void openXiaomi(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName = null;
            componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            jumpAPPInfo(context);
        }

    }

    private static void jumpAPPInfo(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);

    }

    private static void openMeizu(Context context) {
        Intent intent = new Intent();
        ComponentName componentName = null;
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //跳自启动管理
            componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.SmartBGActivity");//跳转到后台管理页面
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity");//跳转到手机管家
                intent.setComponent(componentName);
                context.startActivity(intent);
            } catch (Exception e1) {
                //跳转失败
            }
        }

    }

    private static void openSamsung(Context context) {
        Intent intent = new Intent();
        ComponentName componentName = null;
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //跳自启动管理
            componentName = ComponentName.unflattenFromString("com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity");
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                componentName = new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity");
                intent.setComponent(componentName);
                context.startActivity(intent);
            } catch (Exception e1) {
                //跳转失败
            }
        }

    }

}