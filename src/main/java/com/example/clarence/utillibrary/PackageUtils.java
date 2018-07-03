package com.example.clarence.utillibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by clarence on 3/27/16.
 */
public class PackageUtils {
    public static final String QQ = "com.tencent.mobileqq";
    public static final String WX = "com.tencent.mm";
    public static final String SINA = "com.sina.weibo";

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new PackageInfo();
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 判断手机是否安装某个应用
     *
     * @param packageName 应用包名
     *                    eg.
     *                    PackageUtils.WX
     *                    PackageUtils.QQ
     * @return true：安装，false：未安装
     */
    public static boolean isAppInstall(Context context, String packageName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (info == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
