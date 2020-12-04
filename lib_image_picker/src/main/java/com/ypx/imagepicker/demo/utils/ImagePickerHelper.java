package com.ypx.imagepicker.demo.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ypx.imagepicker.ImagePicker;
import com.ypx.imagepicker.R;
import com.ypx.imagepicker.bean.ImageItem;
import com.ypx.imagepicker.bean.MimeType;
import com.ypx.imagepicker.bean.PickerError;
import com.ypx.imagepicker.bean.SelectMode;
import com.ypx.imagepicker.bean.selectconfig.CropConfig;
import com.ypx.imagepicker.builder.MultiPickerBuilder;
import com.ypx.imagepicker.data.OnImagePickCompleteListener;
import com.ypx.imagepicker.data.OnImagePickCompleteListener2;
import com.ypx.imagepicker.presenter.IPickerPresenter;
import com.ypx.imagepicker.demo.style.RedBookPresenter;
import com.ypx.imagepicker.demo.style.WeChatPresenter;
import com.ypx.imagepicker.demo.style.custom.CustomImgPickerPresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Describe:
 * <p>图片选择工具</p>
 *
 * @author zhouhuan
 * @Date 2020/10/9
 */
public final class ImagePickerHelper {

    public ImagePickerHelper(Activity activity, GridLayout mGridLayout) {
        this.activity = activity;
        this.mGridLayout = mGridLayout;
        refreshGridLayout();
    }

    public ImagePickerHelper(Activity activity, GridLayout mGridLayout, Config config) {
        this.activity = activity;
        this.mGridLayout = mGridLayout;
        this.config = config;
        refreshGridLayout();
    }

    public ImagePickerHelper(Activity activity, GridLayout mGridLayout, OnResultCallBack onResultCallBack) {
        this.activity = activity;
        this.mGridLayout = mGridLayout;
        this.onResultCallBack = onResultCallBack;
        refreshGridLayout();
    }

    public ImagePickerHelper(Activity activity, GridLayout mGridLayout, OnResultCallBack onResultCallBack, Config config) {
        this.activity = activity;
        this.mGridLayout = mGridLayout;
        this.onResultCallBack = onResultCallBack;
        this.config = config;
        refreshGridLayout();
    }

    public ImagePickerHelper(ArrayList<ImageItem> picList, Activity activity, GridLayout mGridLayout, OnResultCallBack onResultCallBack) {
        this.picList = picList;
        this.activity = activity;
        this.mGridLayout = mGridLayout;
        this.onResultCallBack = onResultCallBack;
        refreshGridLayout();
    }

    public ImagePickerHelper(ArrayList<ImageItem> picList, Activity activity, GridLayout mGridLayout, OnResultCallBack onResultCallBack, Config config) {
        this.picList = picList;
        this.activity = activity;
        this.mGridLayout = mGridLayout;
        this.onResultCallBack = onResultCallBack;
        this.config = config;
        refreshGridLayout();
    }

    public static Builder with(Config config) {
        return new Builder(config);
    }

    private ArrayList<ImageItem> picList = new ArrayList<>();
    private Activity activity;
    private GridLayout mGridLayout;
    private OnResultCallBack onResultCallBack;
    private Config config;

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setOnResultCallBack(OnResultCallBack onResultCallBack) {
        this.onResultCallBack = onResultCallBack;
    }

    public interface OnResultCallBack {
        /**
         * 接口回调结果 主线程
         * 注意：剪裁回调里的ImageItem中getCropUrl()才是剪裁过后的图片地址
         *
         * @param items
         */
        void onResult(ArrayList<ImageItem> items);
    }

    private void weChatPick(int count) {
        boolean isCustom = isCustom();
        boolean isShowOriginal = isShowOriginal();
        Set<MimeType> mimeTypes = getMimeTypes();
        int selectMode = getSelectMode();
        boolean isCanPreviewVideo = isCanPreviewVideo();
        boolean isShowCamera = isShowCamera();
        boolean isPreviewEnable = isPreviewEnable();
        boolean isVideoSinglePick = isVideoSinglePick();
        boolean isSinglePickWithAutoComplete = isSinglePickWithAutoComplete();
        boolean isSinglePickImageOrVideoType = isSinglePickImageOrVideoType();
        ArrayList<ImageItem> resultList = getPicList();
        boolean isCheckLastImageList = isCheckLastImageList();
        boolean isCheckShieldList = isCheckShieldList();

        IPickerPresenter presenter = isCustom ? config.customImgPickerPresenter : config.weChatPresenter;
        ImagePicker.withMulti(presenter)//指定presenter
                .setMaxCount(count)//设置选择的最大数
                .setColumnCount(4)//设置列数
                .setOriginal(isShowOriginal)
                .mimeTypes(mimeTypes)//设置要加载的文件类型，可指定单一类型
                // .filterMimeType(MimeType.GIF)//设置需要过滤掉加载的文件类型
                .setSelectMode(selectMode)
                .setDefaultOriginal(false)
                .setPreviewVideo(isCanPreviewVideo)
                .showCamera(isShowCamera)//显示拍照
                .showCameraOnlyInAllMediaSet(true)
                .setPreview(isPreviewEnable)//是否开启预览
                .setVideoSinglePick(isVideoSinglePick)//设置视频单选
                .setSinglePickWithAutoComplete(isSinglePickWithAutoComplete)
                .setSinglePickImageOrVideoType(isSinglePickImageOrVideoType)//设置图片和视频单一类型选择
                .setMaxVideoDuration(120000L)//设置视频可选取的最大时长
                .setMinVideoDuration(3000L)
                .setSingleCropCutNeedTop(true)
                //设置上一次操作的图片列表，下次选择时默认恢复上一次选择的状态
                .setLastImageList(isCheckLastImageList ? resultList : null)
                //设置需要屏蔽掉的图片列表，下次选择时已屏蔽的文件不可选择
                .setShieldList(isCheckShieldList ? resultList : null)
                .pick(activity, new OnImagePickCompleteListener2() {
                    @Override
                    public void onPickFailed(PickerError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onImagePickComplete(ArrayList<ImageItem> items) {
                        //图片选择回调，主线程
                        notifyImageItemsCallBack(items);
                    }
                });
    }

    private void redBookPick(int count) {
        boolean isShowCamera = isShowCamera();
        Set<MimeType> mimeTypes = getMimeTypes();
        ArrayList<ImageItem> resultList = getPicList();
        boolean isVideoSinglePick = isVideoSinglePick();
        boolean isSinglePickWithAutoComplete = isSinglePickWithAutoComplete();

        ImagePicker.withCrop(config.redBookPresenter)//设置presenter
                .setMaxCount(count)//设置选择数量
                .showCamera(isShowCamera)//设置显示拍照
                .setColumnCount(4)//设置列数
                .mimeTypes(mimeTypes)//设置需要加载的文件类型
                // .filterMimeType(MimeType.GIF)//设置需要过滤掉的文件类型
                .assignGapState(false)
                .setFirstImageItem(resultList.size() > 0 ? resultList.get(0) : null)//设置上一次选中的图片
                .setVideoSinglePick(isVideoSinglePick)//设置视频单选
                .setSinglePickWithAutoComplete(isSinglePickWithAutoComplete)
                .setMaxVideoDuration(120000L)//设置可选区的最大视频时长
                .setMinVideoDuration(3000L)
                .pick(activity, new OnImagePickCompleteListener2() {
                    @Override
                    public void onPickFailed(PickerError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onImagePickComplete(ArrayList<ImageItem> items) {
                        //图片剪裁回调，主线 程
                        //注意：剪裁回调里的ImageItem中getCropUrl()才是剪裁过后的图片地址
                        notifyImageItemsCallBack(items);
                    }
                });
    }

    private void pickAndCrop() {
        boolean isCustom = isCustom();
        Set<MimeType> mimeTypes = getMimeTypes();
        boolean isShowCamera = isShowCamera();
        int minMarginProgress = getMinMarginProgress();
        boolean isGap = isGap();
        int cropGapBackgroundColor = getCropGapBackgroundColor();
        int cropRatioX = getCropRatioX();
        int cropRatioY = getCropRatioY();
        boolean isNeedCircle = isNeedCircle();

        IPickerPresenter presenter = isCustom ? config.customImgPickerPresenter : config.weChatPresenter;
        MultiPickerBuilder builder = ImagePicker.withMulti(presenter)//指定presenter
                .setColumnCount(4)//设置列数
                .mimeTypes(mimeTypes)//设置要加载的文件类型，可指定单一类型
                // .filterMimeType(MimeType.GIF)//设置需要过滤掉加载的文件类型
                .setSingleCropCutNeedTop(true)
                .showCamera(isShowCamera)//显示拍照
                .cropSaveInDCIM(false)
                .cropRectMinMargin(minMarginProgress)
                .cropStyle(isGap ? CropConfig.STYLE_GAP : CropConfig.STYLE_FILL)
                .cropGapBackgroundColor(cropGapBackgroundColor)
                .setCropRatio(cropRatioX, cropRatioY);
        if (isNeedCircle) {
            builder.cropAsCircle();
        }
        builder.crop(activity, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                //图片选择回调，主线程
                notifyImageItemsCallBack(items);
            }
        });
    }

    private void autoCrop() {
        ArrayList<ImageItem> resultList = getPicList();
        int minMarginProgress = getMinMarginProgress();
        boolean isGap = isGap();
        int cropGapBackgroundColor = getCropGapBackgroundColor();
        int cropRatioX = getCropRatioX();
        int cropRatioY = getCropRatioY();
        boolean isNeedCircle = isNeedCircle();

        if (resultList.size() == 0) {
            Toast.makeText(activity, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
            return;
        }

        //配置剪裁属性
        CropConfig cropConfig = new CropConfig();
        cropConfig.setCropRatio(cropRatioX, cropRatioY);//设置剪裁比例
        cropConfig.setCropRectMargin(minMarginProgress);//设置剪裁框间距，单位px
        cropConfig.setCircle(isNeedCircle);//是否圆形剪裁
        cropConfig.setCropStyle(isGap ? CropConfig.STYLE_GAP : CropConfig.STYLE_FILL);
        cropConfig.setCropGapBackgroundColor(cropGapBackgroundColor);
        //用于恢复上一次剪裁状态
        //cropConfig.setCropRestoreInfo();
        ImagePicker.crop(activity, new WeChatPresenter(config), cropConfig, resultList.get(0).path, new OnImagePickCompleteListener2() {
            @Override
            public void onPickFailed(PickerError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                //剪裁回调，主线程
                notifyImageItemsCallBack(items);
            }
        });
    }

    private void takePhoto() {
        String imageName = System.currentTimeMillis() + "";
        boolean isCopyInDCIM = true;
        ImagePicker.takePhoto(activity, imageName, isCopyInDCIM, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                notifyImageItemsCallBack(items);
            }
        });
    }

    private void takePhotoAndCrop() {
        int minMarginProgress = getMinMarginProgress();
        boolean isGap = isGap();
        int cropGapBackgroundColor = getCropGapBackgroundColor();
        int cropRatioX = getCropRatioX();
        int cropRatioY = getCropRatioY();
        boolean isNeedCircle = isNeedCircle();
        boolean isCustom = isCustom();

        //配置剪裁属性
        CropConfig cropConfig = new CropConfig();
        cropConfig.setCropRatio(cropRatioX, cropRatioY);//设置剪裁比例
        cropConfig.setCropRectMargin(minMarginProgress);//设置剪裁框间距，单位px
        cropConfig.setCircle(isNeedCircle);//是否圆形剪裁
        cropConfig.setCropStyle(isGap ? CropConfig.STYLE_GAP : CropConfig.STYLE_FILL);
        cropConfig.setCropGapBackgroundColor(cropGapBackgroundColor);

        IPickerPresenter presenter = isCustom ? config.customImgPickerPresenter : config.weChatPresenter;
        ImagePicker.takePhotoAndCrop(activity, presenter, cropConfig, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                //剪裁回调，主线程
                notifyImageItemsCallBack(items);
            }
        });
    }

    private void preview(int pos) {
        final ArrayList<ImageItem> resultList = getPicList();
        IPickerPresenter presenter = isCustom() ? config.customImgPickerPresenter
                : config.weChatPresenter;

        //这一段是为了解决预览加载的是原图而不是剪裁的图片，做的兼融处理，实际调用请删除这一段
        ArrayList<String> list = new ArrayList<>();
        for (ImageItem imageItem : resultList) {
            if (imageItem.getCropUrl() != null && imageItem.getCropUrl().length() > 0) {
                list.add(imageItem.getCropUrl());
            } else {
                list.add(imageItem.path);
            }
        }

        ImagePicker.preview(activity, presenter, list, pos, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                //图片编辑回调，主线程
                resultList.clear();
                notifyImageItemsCallBack(items);
            }
        });
    }


    public static class Builder {
        private Config config;

        public Builder(Config config) {
            this.config = config;
        }

        public Config build() {
            config.weChatPresenter = new WeChatPresenter(config);
            config.redBookPresenter = new RedBookPresenter();
            config.customImgPickerPresenter = new CustomImgPickerPresenter();
            return config;
        }

        /**
         * 选择器-单选
         */
        public Builder setSingle(boolean mRbSingle) {
            config.mRbSingle = mRbSingle;
            config.mRbMulti = !mRbSingle;
            config.mRbCrop = !mRbSingle;
            config.mRbAutoCrop = !mRbSingle;
            config.mRbTakePhoto = !mRbSingle;
            config.mRbTakePhotoAndCrop = !mRbSingle;
            return this;
        }

        /**
         * 选择器-多选
         */
        public Builder setMulti(boolean mRbMulti) {
            config.mRbSingle = !mRbMulti;
            config.mRbMulti = mRbMulti;
            config.mRbCrop = !mRbMulti;
            config.mRbAutoCrop = !mRbMulti;
            config.mRbTakePhoto = !mRbMulti;
            config.mRbTakePhotoAndCrop = !mRbMulti;
            return this;
        }

        /**
         * 选择器-剪裁
         */
        public Builder setCrop(boolean mRbCrop) {
            config.mRbSingle = !mRbCrop;
            config.mRbMulti = !mRbCrop;
            config.mRbCrop = mRbCrop;
            config.mRbAutoCrop = !mRbCrop;
            config.mRbTakePhoto = !mRbCrop;
            config.mRbTakePhotoAndCrop = !mRbCrop;
            return this;
        }

        /**
         * 直接拍照
         */
        public Builder setTakePhoto(boolean mRbTakePhoto) {
            config.mRbSingle = !mRbTakePhoto;
            config.mRbMulti = !mRbTakePhoto;
            config.mRbCrop = !mRbTakePhoto;
            config.mRbAutoCrop = !mRbTakePhoto;
            config.mRbTakePhoto = mRbTakePhoto;
            config.mRbTakePhotoAndCrop = !mRbTakePhoto;
            return this;
        }

        /**
         * 直接裁剪
         */
        public Builder setAutoCrop(boolean mRbAutoCrop) {
            config.mRbSingle = !mRbAutoCrop;
            config.mRbMulti = !mRbAutoCrop;
            config.mRbCrop = !mRbAutoCrop;
            config.mRbAutoCrop = mRbAutoCrop;
            config.mRbTakePhoto = !mRbAutoCrop;
            config.mRbTakePhotoAndCrop = !mRbAutoCrop;
            return this;
        }

        /**
         * 直接拍照并剪裁
         */
        public Builder setTakePhotoAndCrop(boolean mRbTakePhotoAndCrop) {
            config.mRbSingle = !mRbTakePhotoAndCrop;
            config.mRbMulti = !mRbTakePhotoAndCrop;
            config.mRbCrop = !mRbTakePhotoAndCrop;
            config.mRbAutoCrop = !mRbTakePhotoAndCrop;
            config.mRbTakePhoto = !mRbTakePhotoAndCrop;
            config.mRbTakePhotoAndCrop = mRbTakePhotoAndCrop;
            return this;
        }

        /**
         * 红书选择器
         */
        public Builder setRedBook(boolean mRbRedBook) {
            config.mRbRedBook = mRbRedBook;
            config.mRbWeChat = !mRbRedBook;
            config.mRbCustom = !mRbRedBook;
            return this;
        }

        /**
         * 微信选择器
         */
        public Builder setWeChat(boolean mRbWeChat) {
            config.mRbRedBook = !mRbWeChat;
            config.mRbWeChat = mRbWeChat;
            config.mRbCustom = !mRbWeChat;
            return this;
        }

        /**
         * 自定义选择器
         */
        public Builder setCustom(boolean mRbCustom) {
            config.mRbRedBook = !mRbCustom;
            config.mRbWeChat = !mRbCustom;
            config.mRbCustom = mRbCustom;
            return this;
        }

        /**
         * 是否显示原图
         */
        public Builder setShowOriginal(boolean mCbShowOriginal) {
            config.mCbShowOriginal = mCbShowOriginal;
            return this;
        }

        /**
         * 大图预览时，过滤掉视频预览
         */
        public Builder setFilterVideoPreview(boolean mCbFilterVideoPreview) {
            config.mCbFilterVideoPreview = mCbFilterVideoPreview;
            return this;
        }

        /**
         * 关闭预览
         */
        public Builder setClosePreview(boolean mCbClosePreview) {
            config.mCbClosePreview = mCbClosePreview;
            return this;
        }

        /**
         * 显示拍照
         */
        public Builder setShowCamera(boolean mCbShowCamera) {
            config.mCbShowCamera = mCbShowCamera;
            return this;
        }

        /**
         * 只能选择一个视频
         */
        public Builder setVideoSingle(boolean mCbVideoSingle) {
            config.mCbVideoSingle = mCbVideoSingle;
            return this;
        }

        /**
         * 单选时,点击item直接回调,无需选中
         */
        public Builder setSingleAutoComplete(boolean mCbSingleAutoComplete) {
            config.mCbSingleAutoComplete = mCbSingleAutoComplete;
            return this;
        }

        /**
         * 大图预览时，过滤掉视频预览
         */
        public Builder setImageOrVideo(boolean mCbImageOrVideo) {
            config.mCbImageOrVideo = mCbImageOrVideo;
            return this;
        }

        /**
         * 记录已选择
         */
        public Builder setSave(boolean mRbSave) {
            config.mRbShield = !mRbSave;
            config.mRbSave = mRbSave;
            return this;
        }

        /**
         * 屏蔽已选择
         */
        public Builder setShield(boolean mRbShield) {
            config.mRbShield = mRbShield;
            config.mRbSave = !mRbShield;
            return this;
        }

        /**
         * 剪裁框间距 最大100
         */
        public Builder setShield(int mMarginSeekBar) {
            config.mMarginSeekBar = mMarginSeekBar;
            return this;
        }

        /**
         * cropX:1
         */
        public Builder setXSeekBar(int mXSeekBar) {
            config.mXSeekBar = mXSeekBar;
            return this;
        }

        /**
         * cropY:1
         */
        public Builder setYSeekBar(int mYSeekBar) {
            config.mYSeekBar = mYSeekBar;
            return this;
        }

        public Builder setGap(boolean mCbGap) {
            config.mCbGap = mCbGap;
            return this;
        }

        /**
         * 留白透明背景
         */
        public Builder setGapBackground(boolean mCbGapBackground) {
            config.mCbGapBackground = mCbGapBackground;
            return this;
        }

        /**
         * 开启圆形剪裁
         */
        public Builder setCircle(boolean mCbCircle) {
            config.mCbCircle = mCbCircle;
            return this;
        }

        /**
         * 类型 0 图片 1 视频
         */
        public Builder setMimeType(int mimeType) {
            config.mimeType = mimeType;
            return this;
        }

        /**
         * 最大可选择的照片数量
         */
        public Builder setMaxCount(int maxCount) {
            config.maxCount = maxCount;
            return this;
        }

        /**
         * 图片选择完是否调用自定义的多图剪裁页面(AlohaActivity)
         * @param isAutoJumpAlohaActivity
         * @return
         */
        public Builder setAutoJumpAlohaActivity(boolean isAutoJumpAlohaActivity) {
            config.isAutoJumpAlohaActivity = isAutoJumpAlohaActivity;
            return this;
        }

    }

    public static class Config implements Serializable {
        private WeChatPresenter weChatPresenter;
        private RedBookPresenter redBookPresenter;
        private CustomImgPickerPresenter customImgPickerPresenter;
        /**
         * 最大可选择的照片数量
         */
        private int maxCount = 9;
        /**
         * 选择器-单选
         */
        private boolean mRbSingle;
        /**
         * 选择器-多选
         */
        private boolean mRbMulti;
        /**
         * 选择器-剪裁
         */
        private boolean mRbCrop;
        /**
         * 直接拍照
         */
        private boolean mRbTakePhoto;
        /**
         * 直接裁剪
         */
        private boolean mRbAutoCrop;
        /**
         * 直接拍照并剪裁
         */
        private boolean mRbTakePhotoAndCrop;
        /**
         * 红书选择器
         */
        private boolean mRbRedBook;
        /**
         * 微信选择器
         */
        private boolean mRbWeChat;
        /**
         * 自定义选择器
         */
        private boolean mRbCustom;
        /**
         * 是否显示原图
         */
        private boolean mCbShowOriginal;
        /**
         * 大图预览时，过滤掉视频预览
         */
        private boolean mCbFilterVideoPreview;
        /**
         * 关闭预览
         */
        private boolean mCbClosePreview;
        /**
         * 显示拍照
         */
        private boolean mCbShowCamera;
        /**
         * 只能选择一个视频
         */
        private boolean mCbVideoSingle;
        /**
         * 单选时,点击item直接回调,无需选中
         */
        private boolean mCbSingleAutoComplete;
        /**
         * 大图预览时，过滤掉视频预览
         */
        private boolean mCbImageOrVideo;
        /**
         * 记录已选择
         */
        private boolean mRbSave;
        /**
         * 屏蔽已选择
         */
        private boolean mRbShield;
        /**
         * 剪裁框间距 最大100
         */
        private int mMarginSeekBar;
        /**
         * cropX:1
         */
        private int mXSeekBar;
        /**
         * cropY:1
         */
        private int mYSeekBar;
        /**
         * 开启留白剪裁
         */
        private boolean mCbGap;
        /**
         * 留白透明背景
         */
        private boolean mCbGapBackground;
        /**
         * 开启圆形剪裁
         */
        private boolean mCbCircle;
        /**
         * 类型 0 图片 1 视频
         */
        private int mimeType = 0;

        public int getMimeType() {
            return mimeType;
        }

        /**
         * 图片选择完是否调用自定义的多图剪裁页面(AlohaActivity)
         */
        private boolean isAutoJumpAlohaActivity;

        public boolean isAutoJumpAlohaActivity() {
            return isAutoJumpAlohaActivity;
        }
    }

    public Set<MimeType> getMimeTypes() {
        Set<MimeType> mimeTypes = new HashSet<>();
        if (config.mimeType == 0) {
            mimeTypes.addAll(MimeType.ofImage());
        } else if (config.mimeType == 1) {
            mimeTypes.addAll(MimeType.ofVideo());
        } else {
            mimeTypes.addAll(MimeType.ofAll());
        }
        return mimeTypes;
    }

    /**
     * 获取选择模式
     *
     * @return
     */
    public int getSelectMode() {
        if (config.mRbSingle) {
            return SelectMode.MODE_SINGLE;
        }

        if (config.mRbMulti) {
            return SelectMode.MODE_MULTI;
        }

        if (config.mRbCrop) {
            return SelectMode.MODE_CROP;
        }
        return SelectMode.MODE_MULTI;
    }

    /**
     * 刷新图片显示
     */
    private void refreshGridLayout() {
        if (mGridLayout == null) {
            throw new RuntimeException("mGridLayout is null");
        }
        mGridLayout.setVisibility(View.VISIBLE);
        mGridLayout.removeAllViews();
        int num = picList.size();
        final int picSize = (getScreenWidth() - dp(20)) / 4;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(picSize, picSize);
        if (num >= config.maxCount) {
            mGridLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < num; i++) {
                RelativeLayout view = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.a_layout_pic_select, null);
                view.setLayoutParams(params);
                view.setPadding(dp(5), dp(5), dp(5), dp(5));
                setPicItemClick(view, i);
                mGridLayout.addView(view);
            }
        } else {
            mGridLayout.setVisibility(View.VISIBLE);
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(params);
            imageView.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.add_pic));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(dp(5), dp(5), dp(5), dp(5));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPick();
                }
            });
            for (int i = 0; i < num; i++) {
                RelativeLayout view = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.a_layout_pic_select, null);
                view.setLayoutParams(params);
                view.setPadding(dp(5), dp(5), dp(5), dp(5));
                setPicItemClick(view, i);
                mGridLayout.addView(view);
            }
            mGridLayout.addView(imageView);
        }
    }

    private void setPicItemClick(RelativeLayout layout, final int pos) {
        ImageView ivPic = (ImageView) layout.getChildAt(0);
        ImageView ivClose = (ImageView) layout.getChildAt(1);
        displayImage(picList.get(pos), ivPic);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picList.remove(pos);
                refreshGridLayout();
            }
        });
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview(pos);
            }
        });
    }

    private void displayImage(ImageItem imageItem, ImageView imageView) {
        if (imageItem.getCropUrl() != null && imageItem.getCropUrl().length() > 0) {
            Glide.with(getActivity()).load(imageItem.getCropUrl()).into(imageView);
        } else {
            if (imageItem.getUri() != null) {
                Glide.with(getActivity()).load(imageItem.getUri()).into(imageView);
            } else {
                Glide.with(getActivity()).load(imageItem.path).into(imageView);
            }
        }
    }

    private Activity getActivity() {
        if (activity == null) {
            throw new RuntimeException("Activity is null");
        }
        return activity;
    }

    private int dp(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getActivity().getResources().getDisplayMetrics());
    }

    /**
     * 获得屏幕宽度
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public void startPick() {
        if (config.mRbCrop) {
            //剪裁
            pickAndCrop();
        } else if (config.mRbTakePhoto) {
            //直接拍照
            takePhoto();
        } else if (config.mRbAutoCrop) {
            //直接剪裁
            autoCrop();
        } else if (config.mRbTakePhotoAndCrop) {
            //直接拍照并剪裁
            takePhotoAndCrop();
        } else if (config.mRbSingle) {
            //类型选择 一个微信图片选择，一个是红书选择器
            if (config.mRbRedBook) {
                redBookPick(1);
            } else {
                weChatPick(1);
            }
        } else if (config.mRbRedBook) {
            if (config.mRbSave) {
                redBookPick(config.maxCount);
            } else {
                redBookPick(config.maxCount - picList.size());
            }
        } else if (config.mRbWeChat) {
            if (config.mRbSave) {
                weChatPick(config.maxCount);
            } else {
                weChatPick(config.maxCount - picList.size());
            }
        } else if (config.mRbCustom) {
            if (config.mRbSave) {
                weChatPick(config.maxCount);
            } else {
                weChatPick(config.maxCount - picList.size());
            }
        }
    }

    public ArrayList<ImageItem> getPicList() {
        return picList;
    }

    /**
     * 自定义选择器
     *
     * @return
     */
    public boolean isCustom() {
        return config.mRbCustom;
    }

    /**
     * 显示原图
     *
     * @return
     */
    public boolean isShowOriginal() {
        return config.mCbShowOriginal;
    }

    /**
     * 大图预览时，过滤掉视频预览
     *
     * @return
     */
    public boolean isCanPreviewVideo() {
        return !config.mCbFilterVideoPreview;
    }

    /**
     * 关闭预览
     *
     * @return
     */
    public boolean isPreviewEnable() {
        return !config.mCbClosePreview;
    }

    /**
     * 显示拍照
     *
     * @return
     */
    public boolean isShowCamera() {
        return config.mCbShowCamera;
    }

    /**
     * 只能选择一个视频
     *
     * @return
     */
    public boolean isVideoSinglePick() {
        return config.mCbVideoSingle;
    }

    /**
     * 单选时,点击item直接回调,无需选中
     *
     * @return
     */
    public boolean isSinglePickWithAutoComplete() {
        return config.mCbSingleAutoComplete;
    }

    /**
     * 大图预览时，过滤掉视频预览
     *
     * @return
     */
    public boolean isSinglePickImageOrVideoType() {
        return config.mCbImageOrVideo;
    }

    /**
     * 记录已选择
     *
     * @return
     */
    public boolean isCheckLastImageList() {
        return config.mRbSave;
    }

    /**
     * 屏蔽已选择
     *
     * @return
     */
    public boolean isCheckShieldList() {
        return config.mRbShield;
    }

    /**
     * 剪裁框间距
     *
     * @return
     */
    public int getMinMarginProgress() {
        return config.mMarginSeekBar;
    }

    /**
     * 开启留白剪裁
     *
     * @return
     */
    public boolean isGap() {
        return config.mCbGap;
    }

    /**
     * 留白透明背景
     *
     * @return
     */
    public int getCropGapBackgroundColor() {
        return config.mCbGapBackground ? Color.TRANSPARENT : Color.RED;
    }

    public int getCropRatioX() {
        return config.mXSeekBar;
    }

    public int getCropRatioY() {
        return config.mYSeekBar;
    }

    /**
     * 开启圆形剪裁
     *
     * @return
     */
    public boolean isNeedCircle() {
        return config.mCbCircle;
    }

    public void notifyImageItemsCallBack(ArrayList<ImageItem> imageItems) {
        //图片选择回调，主线程
        if (config.mRbSave) {
            picList.clear();
        }
        picList.addAll(imageItems);
        if (onResultCallBack != null) {
            onResultCallBack.onResult(picList);
        }
        refreshGridLayout();
    }


}
