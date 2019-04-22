package com.callenld.demo.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.callenld.demo.callback.AbstractJsonCallback;
import com.callenld.demo.callback.bean.ResultBean;
import com.callenld.demo.config.Constants;
import com.callenld.demo.utils.DialogUtil;
import com.callenld.demo.utils.SpUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * @author callenld
 */
@SuppressLint("Registered")
public class BaseApplication extends Application {
    private int screenWidth;
    private int screenHeight;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    /**
     * 微信
     */
    private static IWXAPI mWxApi;
    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();
        //okgo网络初始化协议
        OkGo.getInstance().init(this).setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constants.APP_ID);
    }

    /**
     * 登录
     *
     * @param token 登录令牌
     */
    public static void login(String token) {
        SpUtil.getInstance(mContext).setString("token", token);
    }

    /**
     * 退出
     */
    public static void logout(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        SpUtil.getInstance(mContext).remove("token");
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return getToken() != null;
    }

    /**
     * 获取登录令牌
     *
     * @return
     */
    public static String getToken() {
        return SpUtil.getInstance(mContext).getString("token", null);
    }

    /**
     *   
     *  判断微信客户端是否存在  
     *   
     *  @return true安装, false未安装  
     */
    public static boolean isWeChatAppInstalled() {
        if (mWxApi.isWXAppInstalled()) {
            return true;
        } else {
            // 获取packagemanager  
            final PackageManager packageManager = mContext.getPackageManager();
            // 获取所有已安装程序的包信息  
            List<PackageInfo> info = packageManager.getInstalledPackages(0);
            if (info != null) {
                for (int i = 0; i < info.size(); i++) {
                    String pn = info.get(i).packageName;
                    if ("com.tencent.mm".equalsIgnoreCase(pn)) {
                        return true;
                    }
                }
            }
            DialogUtil.getInstance(mContext).showToast("您还未安装微信客户端");
            return false;
        }
    }

    /**
     * 全局获取context
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 全局获取mWxApi
     *
     * @return
     */
    public static IWXAPI getwxApi() {
        return mWxApi;
    }

    public int getScreenWidth() {
        //获取屏幕宽高
        if (screenWidth != 0) {
            return screenWidth;
        }
        setScreen();
        return screenWidth;
    }

    public int getScreenHeight() {
        //获取屏幕宽高
        if (screenHeight != 0) {
            return screenHeight;
        }
        setScreen();
        return screenHeight;
    }

    private void setScreen() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        if (screenWidth > screenHeight) {
            int temp = screenHeight;
            screenHeight = screenWidth;
            screenWidth = temp;
        }
    }
}

