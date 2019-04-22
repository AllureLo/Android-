package com.callenld.demo.callback;

import com.callenld.demo.MainActivity;
import com.callenld.demo.base.BaseApplication;
import com.callenld.demo.callback.bean.ResultBean;
import com.callenld.demo.utils.DialogUtil;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;
import okhttp3.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.SocketTimeoutException;


/**
 * ================================================
 * @author callenld
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * ================================================
 */
public abstract class AbstractJsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public AbstractJsonCallback() {
    }

    public AbstractJsonCallback(Type type) {
        this.type = type;
    }

    public AbstractJsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
        request.headers("token", BaseApplication.getToken());
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        T t = response.body();
        if (t instanceof ResultBean) {
            switch (((ResultBean) t).getCode()) {
                case 10000:
                    onSuccess(t);
                    break;
                case 20001:
                    DialogUtil.getInstance(BaseApplication.getContext()).showToast("登录失效，请重新登录");
                    BaseApplication.logout(MainActivity.class);
                    break;
                case 20000:
                    DialogUtil.getInstance(BaseApplication.getContext()).showToast(((ResultBean) t).getMsg());
                    onError();
                    break;
                default:
                    DialogUtil.getInstance(BaseApplication.getContext()).showToast("网络出错");
                    break;
            }
        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        Throwable exception = response.getException();
        if (exception instanceof SocketTimeoutException) {
            DialogUtil.getInstance(BaseApplication.getContext()).showToast("请求超时！");
        } else if (exception instanceof SocketException) {
            DialogUtil.getInstance(BaseApplication.getContext()).showToast("服务器异常！");
        }

    }

    /**
     * 成功返回
     *
     * @param result
     */
    public abstract void onSuccess(T result);

    /**
     * 错误返回
     */
    public void onError() {

    }
}