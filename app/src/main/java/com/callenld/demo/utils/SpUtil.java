package com.callenld.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.alibaba.fastjson.TypeReference;
import com.callenld.demo.utils.bean.SpSaveBean;

/**
 *
 * @author callenld
 * @date 2018/5/3
 */
public class SpUtil {
    /**
     * 保存时间单位
     */
    public static final int TIME_SECOND = 1;
    public static final int TIME_MINUTES = 60*TIME_SECOND;
    public static final int TIME_HOUR = 60 *TIME_MINUTES;
    public static final int TIME_DAY = TIME_HOUR * 24;
    public static final int TIME_MOUTH = TIME_DAY * 30;

    /**
     * 不限制存放数据的数量
     */
    public static final int TIME_MAX = Integer.MAX_VALUE;
    public static final int DURATION_UNIT = 1000;

    private static final String FILE_NAME = "config";
    private SharedPreferences sp;
    private Editor editor;

    @SuppressLint("StaticFieldLeak")
    private static SpUtil instance;

    public static SpUtil getInstance(Context context) {
        if (null == instance) {
            instance = new SpUtil(context);
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    private SpUtil(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setString(String e, String value) {
        SpSaveBean<String> spSaveModel = new SpSaveBean<>(TIME_MAX,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public void setString(String e, String value, int saveTime) {
        SpSaveBean<String> spSaveModel = new SpSaveBean<>(saveTime,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public String getString(String e, String defValue){
        String json = sp.getString(e,"");
        if(!TextUtils.isEmpty(json)){
            SpSaveBean<String> spSaveModel = JsonUtil.fromJson(json, new TypeReference<SpSaveBean<String>>(){});
            if(isTimeOut(spSaveModel.getCurrentTime(),spSaveModel.getSaveTime())){
                return spSaveModel.getValue();
            }
        }
        return defValue;
    }

    public void setInt(String e, int value) {
        SpSaveBean<Integer> spSaveModel = new SpSaveBean<>(TIME_MAX,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public void setInt(String e, int value, int saveTime) {
        SpSaveBean<Integer> spSaveModel=new SpSaveBean<>(saveTime,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public Integer getInt(String e, int defValue){
        String json=sp.getString(e,"");
        if(!TextUtils.isEmpty(json)){
            SpSaveBean<Integer> spSaveModel = JsonUtil.fromJson(json, new TypeReference<SpSaveBean<Integer>>(){});
            if(isTimeOut(spSaveModel.getCurrentTime(),spSaveModel.getSaveTime())){
                return spSaveModel.getValue();
            }
        }
        return defValue;
    }

    public void setBoolean(String e, boolean value) {
        SpSaveBean<Boolean> spSaveModel=new SpSaveBean<>(TIME_MAX,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public void setBoolean(String e, boolean value, int saveTime) {
        SpSaveBean<Boolean> spSaveModel = new SpSaveBean<>(saveTime,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public boolean getBoolean(String e, boolean defValue){
        String json=sp.getString(e,"");
        if(!TextUtils.isEmpty(json)){
            SpSaveBean<Boolean> spSaveModel = JsonUtil.fromJson(json, new TypeReference<SpSaveBean<Boolean>>(){});
            if(isTimeOut(spSaveModel.getCurrentTime(),spSaveModel.getSaveTime())){
                return spSaveModel.getValue();
            }
        }
        return defValue;
    }

    public void setLong(String e, long value) {
        SpSaveBean<Long> spSaveModel = new SpSaveBean<>(TIME_MAX,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public void setLong(String e, long value, int saveTime) {
        SpSaveBean<Long> spSaveModel = new SpSaveBean<>(saveTime,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public long getLong(String e, long defValue){
        String json=sp.getString(e,"");
        if(!TextUtils.isEmpty(json)){
            SpSaveBean<Long> spSaveModel = JsonUtil.fromJson(json, new TypeReference<SpSaveBean<Long>>(){});
            if(isTimeOut(spSaveModel.getCurrentTime(),spSaveModel.getSaveTime())){
                return spSaveModel.getValue();
            }
        }
        return defValue;
    }

    public boolean isTimeOut(long saveCurrentTime,int saveTime){
        return  (System.currentTimeMillis()-saveCurrentTime)/DURATION_UNIT<saveTime;
    }

    public void set(String e, Object value, int saveTime) {
        SpSaveBean<Object> spSaveModel = new SpSaveBean<>(saveTime,value, System.currentTimeMillis());
        String json = JsonUtil.toJson(spSaveModel);
        editor.putString(e, json);
        editor.commit();
    }

    public void remove(String e) {
        editor.remove(e);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
