package com.callenld.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

public class LaunchModeUtil {
    /**
     * 启动-模式,首次安装-首次启动
     */
    public static final int LMODE_NEW_INSTALL = 1;

    /**
     * 覆盖安装-首次启动
     */
    public static final int LMODE_UPDATE = 2;

    /**
     * 已安装-二次启动
     */
    public static final int LMODE_AGAIN = 3;

    /**
     * 启动-模式
     */
    private int launchMode = LMODE_AGAIN;

    private Context context;

    @SuppressLint("StaticFieldLeak")
    private static LaunchModeUtil instance;

    private LaunchModeUtil(Context context) {
        this.context = context;
    }

    public static LaunchModeUtil getInstance(Context context) {
        if (instance == null) {
            instance = new LaunchModeUtil(context);
        }
        return instance;
    }

    // -------启动状态------------------------------------------------------------

    /**
     * 标记-打开app,用于产生-是否首次打开
     */
    private void markOpenApp() {
        String lastVersion = SpUtil.getInstance(context).getString("lastVersion", null);
        String thisVersion = VersionUtil.getAppVersionName(context);

        // 首次启动
        if (TextUtils.isEmpty(lastVersion)) {
            launchMode = LMODE_NEW_INSTALL;
            SpUtil.getInstance(context).setString("lastVersion", thisVersion);
        }
        // 更新
        else if (!thisVersion.equals(lastVersion)) {
            launchMode = LMODE_UPDATE;
            SpUtil.getInstance(context).setString("lastVersion", thisVersion);
        }
        // 二次启动(版本未变)
        else {
            launchMode = LMODE_AGAIN;
        }
    }

    /**
     * 首次打开,新安装、覆盖(版本号不同)
     *
     * @return
     */
    public boolean isFirstOpen() {
        markOpenApp();
        return launchMode != LMODE_AGAIN;
    }
}
