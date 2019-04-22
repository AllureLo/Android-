package com.callenld.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.callenld.demo.R;

/**
 * @author callenld
 */
public class DialogUtil {
    /**
     * 提示层有效时间
     */
    public final static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public final static int LENGTH_LONG = Toast.LENGTH_LONG;

    /**
     * 提示层
     */
    private Toast toast;

    /**
     * 全局上下文
     */
    public Context context;

    @SuppressLint("StaticFieldLeak")
    private static DialogUtil instance;

    /**
     * 如果有使用到showTost...相关的方法使用之前需要初始化该方法
     */
    public static DialogUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DialogUtil(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    private DialogUtil(Context context) {
        this.context = context;
    }

    /**
     * 提示框
     * @param sure
     * @param msg
     */
    public void showNormalDialog(DialogInterface.OnClickListener sure, String msg){
        showNormalDialog(sure, R.drawable.icon_tip, "提示", msg);
    }

    /**
     * 提示框
     * @param sure
     * @param icon
     * @param title
     * @param msg
     */
    public void showNormalDialog(DialogInterface.OnClickListener sure, @DrawableRes int icon, String title, String msg){
        showNormalDialog(sure, (dialog, which) -> {
            //...To-do
        }, icon, title, msg);
    }

    /**
     * 提示框
     * @param sure
     * @param cancel
     * @param icon
     * @param title
     * @param msg
     */
    public void showNormalDialog(DialogInterface.OnClickListener sure, DialogInterface.OnClickListener cancel, @DrawableRes int icon, String title, String msg){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setIcon(icon);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定", sure);
        normalDialog.setNegativeButton("取消", cancel);
        // 显示
        normalDialog.show();
    }

    /**
     * 提示框
     * @param msg
     */
    public void showToast(String msg){
        showToast(msg, LENGTH_SHORT);
    }

    /**
     * 提示框
     * @param msg
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg, int time){
        if (toast == null) {
            toast = Toast.makeText(context, msg, time);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 提示框-取消
     */
    public void cancel() {
        toast.cancel();
    }
}
