package com.callenld.demo.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.callenld.demo.R;

public class NavigationItem extends LinearLayout{
    /**
     * 粗细，默认为3dp
     */
    private static final int lineSize = 3;
    private static final int mItemPadding = 12;

    private Context context;

    private String mTitle;
    /**
     * 字体大小，默认为13sp
     */
    private float mTitleSize = 13;

    /**
     * 分割线
     */
    private View line;
    private LinearLayout layout;
    /**
     * 标题
     */
    private TextView title;

    /**
     * 标题字体颜色
     */
    private int titleColorBefore;
    /**
     * 标题字体颜色（选中状态）
     */
    private int titleColorAfter;

    private int backColorBefore = Color.parseColor("#F3F5F7");
    private int backColorAfter = Color.parseColor("#FFFFFF");

    private int dp2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public NavigationItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setAttributes(context, attrs);
    }

    /**
     * 获得自定义属性
     *
     * @param attrs
     */
    private void setAttributes(Context context, AttributeSet attrs) {
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.navigation);
        mTitleSize = typedArray.getDimension(R.styleable.navigation_titleSize, 13);
        this.context = context;
    }

    public NavigationItem build(int orientation) {
        setBackgroundColor(backColorBefore);
        switch (orientation) {
            case HORIZONTAL :
                setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
                setOrientation(VERTICAL);
                addView(setTitle());
                addLine(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(lineSize)));
                addView(setLine(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(1))));
                break;
            case VERTICAL :
                setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                setOrientation(HORIZONTAL);
                addLine(new LayoutParams(dp2px(lineSize), LayoutParams.MATCH_PARENT));
                addLayout().addView(setTitle());
                addLayout().addView(setLine(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(1))));
                addView(setLine(new LayoutParams(dp2px(1), LayoutParams.MATCH_PARENT)));
                break;
            default:
        }
        return this;
    }

    public NavigationItem setTitle(String title){
        this.mTitle = title;
        return this;
    }

    public NavigationItem setTileColor(int titleColorBefore, int titleColorAfter) {
        this.titleColorBefore = titleColorBefore;
        this.titleColorAfter = titleColorAfter;
        return this;
    }

    public void setStatus(boolean isSelected) {
        line.setVisibility(isSelected ? VISIBLE : INVISIBLE);
        title.setTextColor(isSelected ? titleColorAfter : titleColorBefore);
        this.setBackgroundColor(isSelected ? backColorAfter : backColorBefore);
    }

    private void addLine(LayoutParams params) {
        line = new View(context);
        line.setLayoutParams(params);
        line.setBackgroundColor(titleColorAfter);
        line.setVisibility(INVISIBLE);
        addView(line);
    }

    private LinearLayout addLayout() {
        if (layout == null) {
            layout = new LinearLayout(context);
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
            params.gravity = Gravity.CENTER;
            layout.setLayoutParams(params);
            layout.setOrientation(VERTICAL);
            addView(layout);
        }
        return layout;
    }

    private TextView setTitle() {
        title = new TextView(context);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        title.setPadding(0, dp2px(mItemPadding), 0, dp2px(mItemPadding));
        title.setLayoutParams(params);
        title.setText(mTitle);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTitleSize);
        title.setTextColor(titleColorBefore);
        return title;
    }

    private View setLine(LayoutParams params) {
        View view = new View(context);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor("#F6F6F6"));
        return view;
    }
}