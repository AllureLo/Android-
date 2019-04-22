package com.callenld.demo.network;

import com.callenld.demo.callback.bean.ResultBean;
import com.lzy.okgo.callback.AbsCallback;

/**
 * @Author: Callenld
 * @Date: 19-4-22
 */
public interface ICommonService {
    /**
     * 图片上传
     *
     * @param callback 返回函数
     * @param url      链接地址
     */
    <T> void upload(AbsCallback<ResultBean<T>> callback, String url);

    /**
     * 图片删除
     *
     * @param key 图片key值
     */
    <T> void delete(AbsCallback<ResultBean<T>> callback, String key);
}
