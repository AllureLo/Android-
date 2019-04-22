package com.callenld.demo.callback.bean;

import java.io.Serializable;

/**
 * @author callenld
 */
public class BaseResultBean implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    /**
     * 错误码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    public ResultBean toResult() {
        return new ResultBean(code, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
