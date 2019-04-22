package com.callenld.demo.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.callenld.demo.view.TitleBar;

import com.callenld.demo.R;

public abstract class BaseActivity extends PictureSelectorActivity {

    public TitleBar toolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        //数据初始化
        Bundle bundle = getIntent().getExtras();
        bind(bundle == null ? new Bundle() : bundle);
        //初始化
        init();
        //数据渲染
        rendering();

    }

    /**
     * bundle数据提取
     * @param  bundle 数据
     */
    protected abstract void bind(Bundle bundle);

    /**
     * 数据操作
     */
    protected abstract void init();

    /**
     * 数据加载
     */
    protected abstract void rendering();

    public void renderEditText(EditText editText, String value) {
        if (!TextUtils.isEmpty(value)) {
            editText.setText(value);
        }
    }

    public void renderTextView(TextView textView, String value) {
        if (!TextUtils.isEmpty(value)) {
            textView.setText(value);
        }
    }

    public void renderImageView(ImageView imageView, String url, @DrawableRes int resId) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(url)
                    .apply(new RequestOptions()
                            .error(resId)
                            .placeholder(resId))
                    .into(imageView);
        }
    }

    public void renderImageView(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(url)
                    .into(imageView);
        }
    }

    public BaseActivity showActionBar() {
        return showActionBar(null);
    }

    public BaseActivity showActionBar(String title) {
        return showActionBar(title, R.color.colorBlack);
    }

    public BaseActivity showActionBar(@ColorRes int colorId) {
        return showActionBar(null, colorId);
    }

    public BaseActivity showActionBar(String title, @ColorRes int colorId) {
        if (toolbar == null) {
            toolbar = findViewById(R.id.toolbar);
            toolbar.setMyCenterTitle(title == null ? getTitle() : title);
            toolbar.setMyCenterTextColor(ContextCompat.getColor(BaseApplication.getContext(), colorId));
            setSupportActionBar(toolbar);
        } else { //找到 Toolbar 并且替换 Actionbar
            toolbar.setMyCenterTitle(title == null ? getTitle() : title);
        }
        return this;
    }

    public BaseActivity showHome() {
        return showHome(R.drawable.icon_back_black);
    }

    public BaseActivity showHome(@DrawableRes int drawbleId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(drawbleId);
        }
        return this;
    }

    public void setMySettingText(@StringRes int rId) {
        setMySettingText(getText(rId));
    }

    public void setMySettingText(CharSequence text) {
        setMySettingText(text, R.color.colorBlack);
    }

    public void setMySettingText(CharSequence text, @ColorRes int colorId) {
        toolbar.setMySettingText(text);
        toolbar.setMySettingTextColor(ContextCompat.getColor(BaseApplication.getContext(), colorId));
        toolbar.setSettingTextOnClickListener();
        setSettingOnClickListener();
    }

    public void setSettingOnClickListener() {
    }

    public void setSettingTextOnClickListener(TitleBar.setTingTextOnClickListener listener) {
        toolbar.setSettingTextOnClickListener(listener);
    }

    public void setMySettingIcon(@DrawableRes int drawbleId) {
        toolbar.setMySettingIcon(drawbleId);
        toolbar.setSettingIconOnClickListener();
        setSettingIconOnClickListener();
    }

    public void setSettingIconOnClickListener() {
    }

    public void setSettingIconOnClickListener(TitleBar.setSettingIconOnClickListener listener) {
        toolbar.setSettingIconOnClickListener(listener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
