package com.callenld.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.callenld.demo.R;

public class PictureView extends FrameLayout {
    /**
     * 删除控件
     */
    private ImageView delete;

    /**
     * 图片
     */
    private ImageView picture;

    /**
     * 上传进度条
     */
    private ProgressBar progress;

    /**
     * 遮罩层
     */
    private ImageView view;

    /**
     * 图片源
     */
    private Drawable image;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 是否禁止上传
     */
    private boolean isInvalid = false;

    public PictureView(@NonNull Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public PictureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setAttributes(context, attrs);

        setPicture(context);
        setView(context);
        setDelete(context);
        setProgress(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public PictureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        setAttributes(context, attrs);

        setPicture(context);
        setView(context);
        setDelete(context);
        setProgress(context);
    }

    /**
     * 获得自定义属性
     *
     * @param attrs
     */
    private void setAttributes(Context context, AttributeSet attrs) {
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.picture);

        //获得默认图片
        image = typedArray.getDrawable(R.styleable.picture_src);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setView(Context context) {
        if (this.view == null) {
            this.view = new ImageView(context);
            this.view.setBackground(image);
            this.view.setAlpha(0.5f);

            this.view.setImageDrawable(ContextCompat.getDrawable(context, R.color.colorMask));
            this.view.setVisibility(GONE);
            this.addView(view);
        }
    }

    private void setDelete(Context context) {
        if (this.delete == null) {
            this.delete = new ImageView(context);
            //设置删除按钮大小和位置
            int size = getResources().getDimensionPixelSize(R.dimen.picture_delete);
            LayoutParams lp = new LayoutParams(size, size);
            lp.gravity = Gravity.END;
            this.delete.setLayoutParams(lp);
            //设置图片ScaleType属性-拉伸显示图片
            this.delete.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置删除按钮源
            this.delete.setImageResource(R.drawable.icon_delete);
            this.delete.setVisibility(GONE);
            this.addView(delete);

            //设置按钮响应事件
            this.delete.setOnClickListener(v -> {
                this.delete.setVisibility(GONE);
                this.picture.setImageDrawable(image);
                if (listener != null ) {
                    listener.onClick(this.url);
                }
            });
        }
    }

    private void setProgress(Context context) {
        if (this.progress == null) {
            this.progress = new ProgressBar(context, null, R.style.Picture_Progress_Style);
            //设置进度条宽高
            LayoutParams lp = new LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.picture_progress_width),
                    getResources().getDimensionPixelSize(R.dimen.picture_progress_height));
            lp.gravity = Gravity.CENTER;
            this.progress.setLayoutParams(lp);

            this.progress.setIndeterminate(true);
            //设置背景
            this.progress.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.drawable.shape_progress_bar));
            this.progress.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.shape_progress_bar));
            this.progress.setVisibility(GONE);
            this.addView(progress);
        }
    }

    private void setPicture(Context context) {
        if (this.picture == null) {
            this.picture = new ImageView(context);
            this.picture.setImageDrawable(image);
            //设置图片大小
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            this.picture.setLayoutParams(lp);
            //设置图片ScaleType属性-拉伸显示图片
            this.picture.setScaleType(ImageView.ScaleType.FIT_XY);
            this.addView(picture);
        }
    }

    public void setDeleteInvalid() {
        this.delete.setOnClickListener(null);
        this.isInvalid = true;
    }

    public void setPicture(String url) {
        if (this.picture != null) {
            Glide.with(getContext())
                    .load(url)
                    .apply(new RequestOptions()
                            .error(image)
                            .placeholder(image)
                            .centerCrop())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                    boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            finish(url);
                            return false;
                        }
                    })
                    .into(this.picture);
        }
    }

    public void start() {
        this.progress.setVisibility(VISIBLE);
        this.view.setVisibility(VISIBLE);
    }

    private void finish(String url) {
        this.url = url;
        this.delete.setVisibility(VISIBLE);
        this.progress.setVisibility(GONE);
        this.view.setVisibility(GONE);
    }

    public String getUrl() {
        return url;
    }

    public boolean getInvalid() {
        return isInvalid;
    }

    protected OnDeleteListener listener;

    public interface OnDeleteListener {
        /**
         * 删除响应
         * @param url
         */
        void onClick(String url);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.listener = listener;
    }
}
