package com.callenld.demo.network.impl;

import com.callenld.demo.callback.bean.ResultBean;
import com.callenld.demo.network.ICommonService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;

import java.io.File;

/**
 * @Author: Callenld
 * @Date: 19-4-22
 */
public class CommonService implements ICommonService {
    @Override
    public <T> void upload(AbsCallback<ResultBean<T>> callback, String url) {
        OkGo.<ResultBean<T>>post("https://app.teambook.cc/lindoor/common/upload")
                .tag(this)
                .params("file", new File(url))
                .execute(callback);
    }

    @Override
    public <T> void delete(AbsCallback<ResultBean<T>> callback, String url) {
        OkGo.<ResultBean<T>>post("https://app.teambook.cc/lindoor/common/delete")
                .tag(this)
                .params("url", url)
                .execute(callback);
    }
}
