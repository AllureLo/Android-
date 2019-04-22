package com.callenld.demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 *
 * @author Callenld
 * @date 2018/10/30
 */

public class VersionUtil {
    /**
     * 获取versionName
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取versionId
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {



        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 获取apk程序信息
     *
     * @param context
     * @param path apk path
     */
    public static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }


    /**
     * 下载的apk和当前程序版本比较
     *
     * @param context
     * @param apkInfo apk file's packageInfo
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    public static boolean compareApk(Context context, PackageInfo apkInfo) {
        if (apkInfo == null) {
            return false;
        }
        if (!apkInfo.versionName.equals(getAppVersionName(context))) {
            return true;
        }
        return false;

    }
}
