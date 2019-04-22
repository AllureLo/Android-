package com.callenld.demo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * @author callenld
 */
public class NetUtil {
    public static void isNetConnected(Context context) {
        // 获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            DialogUtil.getInstance(context).showToast("当前网络状态不可用!请检查网络");
        }
    }
}
