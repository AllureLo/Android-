package com.callenld.demo.utils;

import com.callenld.demo.base.BaseApplication;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

public class WXEntryUtil {

    public static void wxLogin() {
        if(!BaseApplication.isWeChatAppInstalled()) {
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        //像微信发送请求
        BaseApplication.getwxApi().sendReq(req);
    }
}
