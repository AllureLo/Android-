package com.callenld.demo.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.callenld.demo.adapter.GridImageAdapter;
import com.callenld.demo.callback.AbstractJsonCallback;
import com.callenld.demo.callback.bean.ResultBean;
import com.callenld.demo.manager.FullyGridLayoutManager;
import com.callenld.demo.network.ICommonService;
import com.callenld.demo.network.impl.CommonService;
import com.callenld.demo.view.GraphicsView;
import com.callenld.demo.view.PictureView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.request.base.Request;

import java.util.*;

import com.callenld.demo.R;

/***
 * ━━━━ Code is far away from ━━━━━━
 *    ()      ()
 *    ()     ()
 *    ()    ()
 *   ┏┛┻━━━┛┻━┓
 *   ┃   ━━   ┃
 *   ┃ ┳┛  ┗┳ ┃
 *   ┃   ┻    ┃
 *   ┗━┓   ┏━━┛
 *     ┃   ┃
 *     ┃   ┗━━━┓
 *     ┃       ┣┓
 *     ┃       ┏┛
 *     ┗┓┓┏━┳┓┏┛
 *      ┃┫┫ ┃┫┫
 *      ┗┻┛ ┗┻┛
 * ━━━━ bug with the more protecting ━━━
 * <p/>
 * Created by Chen.
 * @author callenld
 */
@SuppressLint("Registered")
public class PictureSelectorActivity extends AppCompatActivity {
    private List<String> album = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();

    private PopupWindow pop;
    private GridImageAdapter adapter;
    private int maxSelectNum;
    private Map<Integer, Object> imageViews;

    private int chooseRequest = 188;

    private ICommonService commonService = new CommonService();

    protected void pictureSelector(RecyclerView listView, int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        adapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String pictureType = media.getPictureType();
                int mediaType = PictureMimeType.pictureToVideo(pictureType);
                switch (mediaType) {
                    case 1:
                        PictureSelector.create(PictureSelectorActivity.this).externalPicturePreview(position, selectList);
                        break;
                    case 2:
                        // 预览视频
                        PictureSelector.create(PictureSelectorActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case 3:
                        // 预览音频
                        PictureSelector.create(PictureSelectorActivity.this).externalPictureAudio(media.getPath());
                        break;
                    default:
                        break;
                }
            }
        });
        adapter.setOnDeleteClickListener((position, v) -> album.remove(position));

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        listView.setLayoutManager(manager);
        listView.setAdapter(adapter);
    }

    protected void pictureSelector(PictureView pictureView) {
        pictureSelector(pictureView, null, null);
    }

    @SuppressLint("UseSparseArrays")
    protected void pictureSelector(PictureView pictureView, Integer x, Integer y) {
        if (pictureView.getInvalid()) {
            return;
        }
        this.maxSelectNum = 1;
        if (this.imageViews == null) {
            this.imageViews = new HashMap<>(16);
        }
        this.imageViews.put(++chooseRequest, pictureView);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        showPop(chooseRequest, x, y);
    }

    @SuppressLint("UseSparseArrays")
    protected void pictureSelector(GraphicsView graphicsView) {
        this.maxSelectNum = 1;
        if (this.imageViews == null) {
            this.imageViews = new HashMap<>(16);
        }
        this.imageViews.put(++chooseRequest, graphicsView);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        showPop(chooseRequest);
    }


    protected void addImage(List<String> urls, List<String> keys) {
        for (String url : urls) {
            LocalMedia media = new LocalMedia();
            media.setPath(url);
            selectList.add(media);
        }
        album.addAll(keys);
        adapter.notifyDataSetChanged();
    }

    protected String getUrl() {
        StringBuilder sb = new StringBuilder();
        for (String url : album) {
            sb.append(",");
            sb.append(url);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = () -> {
        //弹出选择和拍照的dialog
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        showPop();
    };

    private void showPop() {
        showPop(PictureConfig.CHOOSE_REQUEST, null, null);
    }

    private void showPop(int hashCode) {
        showPop(hashCode, null, null);
    }

    private void showPop(int hashCode, Integer x, Integer y) {
        View bottomView = View.inflate(this, R.layout.pictrue_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);
        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(() -> {
            WindowManager.LayoutParams layoutParam = getWindow().getAttributes();
            layoutParam.alpha = 1f;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(layoutParam);
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        View.OnClickListener clickListener = view -> {
            switch (view.getId()) {
                case R.id.tv_album:
                    if (x == null || y == null) {
                        PictureSelector.create(PictureSelectorActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum - selectList.size())
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .compress(true)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(hashCode);
                    } else {
                        PictureSelector.create(PictureSelectorActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum - selectList.size())
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .compress(true)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .enableCrop(true)
                                .withAspectRatio(x, y)
                                .freeStyleCropEnabled(true)
                                .forResult(hashCode);
                    }
                    break;
                case R.id.tv_camera:
                    if (x == null || y == null) {
                        PictureSelector.create(PictureSelectorActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .compress(true)
                                .cropCompressQuality(70)
                                .forResult(hashCode);
                    } else {
                        PictureSelector.create(PictureSelectorActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .compress(true)
                                .cropCompressQuality(70)
                                .enableCrop(true)
                                .withAspectRatio(x, y)
                                .freeStyleCropEnabled(true)
                                .forResult(hashCode);
                    }
                    break;
                case R.id.tv_cancel:
                    break;
                    default:
                    break;
            }
            closePopupWindow();
        };
        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    private void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode >= PictureConfig.CHOOSE_REQUEST) {
            // 图片选择结果回调
            final List<LocalMedia> images = PictureSelector.obtainMultipleResult(data);
            for (LocalMedia media : images) {
                String url = media.isCompressed() ? media.getCompressPath() : media.isCut() ? media.getCutPath() : media.getPath();

                commonService.upload(new AbstractJsonCallback<ResultBean<String>>() {
                    @Override
                    public void onStart(Request request) {
                        Object object = imageViews.get(requestCode);
                        if (object instanceof PictureView) {
                            ((PictureView) object).start();
                        }
                    }

                    @Override
                    public void onSuccess(ResultBean<String> resultBean) {
                        Object object = imageViews.get(requestCode);
                        if (object == null && adapter != null) {
                            selectList.add(media);
                            // 例如 LocalMedia 里面返回三种path
                            // 1.media.getPath(); 为原图path
                            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                            adapter.notifyDataSetChanged();

                            album.add(resultBean.getData());
                        } else if (object instanceof PictureView) {
                            PictureView pictureView = (PictureView) object;
                            pictureView.setPicture(resultBean.getData());
                            pictureView.setOnDeleteListener(
                                    url -> commonService.delete(new AbstractJsonCallback<ResultBean<Void>>(){
                                        @Override
                                        public void onSuccess(ResultBean<Void> result) {

                                        }
                                    }, url)
                            );
                            imageViews.remove(requestCode);
                        } else if (object instanceof GraphicsView) {
                            GraphicsView graphicsView = (GraphicsView) object;
                            graphicsView.setUrl(resultBean.getData());
                            imageViews.remove(requestCode);
                        }
                    }
                }, url);
            }
        }
    }
}