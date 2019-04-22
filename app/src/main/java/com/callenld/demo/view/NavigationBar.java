package com.callenld.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.callenld.demo.view.NavigationItem;

import com.callenld.demo.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationBar extends LinearLayout {
    private Context context;
    private AttributeSet attrs;

    public NavigationBar(Context context) {
        super(context);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attrs = attrs;
    }

    //////////////////////////////////////////////////
    //提供的api 并且根据api做一定的物理基础准备
    //////////////////////////////////////////////////

    private int containerId;

    private List<Class> classes = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private List<NavigationItem> navigationItems = new ArrayList<>();

    private int currentCheckedIndex;

    private int titleColorBefore = Color.parseColor("#999999");
    private int titleColorAfter = Color.parseColor("#5DC2D0");

    private List<String> data = new ArrayList<>();

    public NavigationBar setContainer(int containerId) {
        this.containerId = containerId;
        return this;
    }

    public NavigationBar setTitleColor(@ColorRes int before, @ColorRes int after) {
        this.titleColorBefore = ContextCompat.getColor(context, before);
        this.titleColorAfter = ContextCompat.getColor(context, after);
        return this;
    }

    public NavigationBar setIndex(int currentCheckedIndex) {
        this.currentCheckedIndex = currentCheckedIndex;
        return this;
    }

    public NavigationBar addItem(Class clazz, String title) {
        return addItem(clazz, title, null);
    }

    @SuppressLint("Assert")
    public NavigationBar addItem(Class clazz, String title, String data) {
        NavigationItem navigationItem = new NavigationItem(context, attrs);
        navigationItem.setTileColor(titleColorBefore, titleColorAfter)
                .setTitle(title)
                .build(getOrientation())
                .setOnClickListener(view -> {
                    assert view instanceof NavigationItem;
                    int target = navigationItems.indexOf(view);
                    switchFragment(target);
                    if (listener != null) {
                        listener.onClick(target);
                    }
                });
        this.classes.add(clazz);
        addView(navigationItem);
        if (!TextUtils.isEmpty(data)) {
            this.data.add(data);
        }
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void build() {
        for (Class clazz : classes) {
            try {
                Fragment fragment = (Fragment) clazz.newInstance();
                fragments.add(fragment);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        switchFragment(currentCheckedIndex);
    }

    protected OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //////////////////////////////////////////////////
    //碎片处理代码
    //////////////////////////////////////////////////
    private Fragment currentFragment;

    //注意 这里是只支持AppCompatActivity 需要支持其他老版的 自行修改
    private void switchFragment(int whichFragment) {
        Fragment fragment = fragments.get(whichFragment);

        int frameLayoutId = containerId;

        if (fragment != null) {
            if (data.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("data", data.get(whichFragment));
                fragment.setArguments(bundle);
            }
            FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (currentFragment != null) {
                    transaction.hide(currentFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (currentFragment != null) {
                    transaction.hide(currentFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            currentFragment = fragment;
            transaction.commit();
        }

        navigationItems.get(currentCheckedIndex).setStatus(false);
        navigationItems.get(whichFragment).setStatus(true);
        currentCheckedIndex = whichFragment;
        invalidate();
    }

    private void addView(NavigationItem navigationItem){
        super.addView(navigationItem);
        navigationItems.add(navigationItem);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}