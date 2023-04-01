package com.zong.call.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/***
 * 跳转应用市场评分
 *
 */
public class MarketUtils {

    public static final String HUAWEI = "com.huawei.appmarket";//华为应用商店
    public static final String ANZHI = "com.hiapk.marketpho";//安智应用商店
    public static final String XIAOMI = "com.xiaomi.market";
    public static final String OPPO = "com.oppo.market";
    public static final String TENCENT = "com.tencent.android.qqdownloader";//应用宝商店
    public static final String BBK = "com.bbk.appstore";//vivo应用商店

    /**
     * 判断应用市场是否存在的办法
     * oppo查不到应用商店
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packageManager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        //用于存储所有已安装程序的包名
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pf = pinfo.get(i).packageName;
                pName.add(pf);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 过滤出已经安装的包名集合
     *
     * @param context
     * @param pkgs    待过滤包名集合
     * @return 已安装的包名集合
     */
    public static ArrayList<String> SelectedInstalledAPPs(Context context, ArrayList<String> pkgs) {
        ArrayList<String> empty = new ArrayList<String>();
        if (context == null || pkgs == null || pkgs.size() == 0)
            return empty;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                try {
                    installPkg = installedPkgs.get(i).applicationInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    empty.add(installPkg);
                    break;
                }

            }
        }
        return empty;
    }

    /**
     * 跳转到app详情界面
     *
     * @param appPkg App的包名
     *               //如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Activity activity, String appPkg) {
        String marketPkg = "";
        if (isAvilible(activity, HUAWEI)) {
            marketPkg = HUAWEI;
        } else if (isAvilible(activity, ANZHI)) {
            marketPkg = ANZHI;
        } else if (isAvilible(activity, BBK)) {
            marketPkg = BBK;
        } else if (isAvilible(activity, XIAOMI)) {
            marketPkg = XIAOMI;
        } else if (isAvilible(activity, OPPO)) {
            marketPkg = OPPO;
        } else if (isAvilible(activity, TENCENT)) {
            marketPkg = TENCENT;
        }
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "未安装应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }



}