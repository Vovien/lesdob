package com.jmbon.widget.picture;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.jmbon.widget.R;
import com.luck.picture.lib.PictureExternalPreviewActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.PictureSimpleVideoPlayActivity;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.DoubleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhaoShulin on 2019-06-06 15:54.
 * <br>
 * Desc:PictureSelector 的选择器
 * <br>
 */
public class SelectPhotoUtil {

    public static PictureSelector create(Activity activity) {
        return PictureSelector.create(activity);
    }

    public static PictureSelector create(Fragment fragment) {
        return PictureSelector.create(fragment);
    }


    public static void selectPic(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect) {
        selectPic(pictureSelector, list, maxSelect, 0, PictureConfig.MULTIPLE, true);
    }

    public static void selectPic(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int requestCode) {
        selectPic(pictureSelector, list, maxSelect, 0, PictureConfig.MULTIPLE, true, requestCode);
    }

    public static void selectPicWithCompressPath(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int requestCode) {
        selectPicWithCompressPath(pictureSelector, list, maxSelect, 0, PictureConfig.MULTIPLE, true, requestCode);
    }

    public static void selectPic(Activity activity, List<LocalMedia> list, int maxSelect) {
        selectPic(PictureSelector.create(activity), list, maxSelect, 0, PictureConfig.MULTIPLE, true);
    }

    public static void selectPic(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int selectType, boolean showCamera) {
        selectPic(pictureSelector, list, maxSelect, 0, selectType, showCamera);
    }

    public static void selectPic(Activity activity, List<LocalMedia> list, int maxSelect, int minSelect, int selectType, boolean showCamera) {
        selectPic(PictureSelector.create(activity), list, maxSelect, minSelect, selectType, showCamera);
    }

    public static void selectVideo(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect) {
        selectVideo(pictureSelector, list, maxSelect, 0, PictureConfig.MULTIPLE, false);
    }

    public static void selectVideo(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int requestCode) {
        selectVideo(pictureSelector, list, maxSelect, 0, PictureConfig.MULTIPLE, false, requestCode);
    }


    /**
     * 多模式
     *
     * @param list 已选列表
     */
    public static void selectPic(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int minSelect, int selectType, boolean showCamera) {
        // 进入相册 以下是例子：用不到的api可以不写

        PictureWindowAnimationStyle windowAnimationStyle = new PictureWindowAnimationStyle();
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pictureSelector
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(3)// 每行显示个数
                .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
                .maxSelectNum(maxSelect)// 最大图片选择数量 int
                .minSelectNum(minSelect)// 最小选择数量 int
                .selectionMode(selectType)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isPageStrategy(true) // 分页加载
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
//                .enableCrop(false)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
//                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isOpenClickSound(false)// 是否开启点击声音 true or false
                .selectionData(list)// 是否传入已选图片 List<LocalMedia> list
                .isPreviewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .compressQuality(75)
                .minimumCompressSize(100)
                .setPictureWindowAnimationStyle(windowAnimationStyle)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    /**
     * 多模式
     *
     * @param list 已选列表
     */
    public static void selectPic(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int minSelect, int selectType, boolean showCamera, int rquestCode) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureWindowAnimationStyle windowAnimationStyle = new PictureWindowAnimationStyle();
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pictureSelector
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(3)// 每行显示个数

                .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
                .maxSelectNum(maxSelect)// 最大图片选择数量 int
                .minSelectNum(minSelect)// 最小选择数量 int
                .selectionMode(selectType)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isPageStrategy(true) // 分页加载
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
//                .enableCrop(false)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
//                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isOpenClickSound(false)// 是否开启点击声音 true or false
                .selectionData(list)// 是否传入已选图片 List<LocalMedia> list
                .isPreviewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .compressQuality(75)
                .minimumCompressSize(100)
                .setPictureWindowAnimationStyle(windowAnimationStyle)
                .forResult(rquestCode);//结果回调onActivityResult code
    }

    /**
     * 多模式
     *
     * @param list 已选列表
     */
    public static void selectPicWithCompressPath(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int minSelect, int selectType, boolean showCamera, int rquestCode) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureWindowAnimationStyle windowAnimationStyle = new PictureWindowAnimationStyle();
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pictureSelector
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(3)// 每行显示个数
                .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
                .maxSelectNum(maxSelect)// 最大图片选择数量 int
                .minSelectNum(minSelect)// 最小选择数量 int
                .selectionMode(selectType)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isPageStrategy(true) // 分页加载
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
//                .enableCrop(false)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
//                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isOpenClickSound(false)// 是否开启点击声音 true or false
                .selectionData(list)// 是否传入已选图片 List<LocalMedia> list
                .isPreviewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .compressQuality(75)
                .minimumCompressSize(100)
                .setPictureWindowAnimationStyle(windowAnimationStyle)
                .forResult(rquestCode);//结果回调onActivityResult code
    }

    /**
     * 多模式
     *
     * @param list 已选列表
     */
    public static void selectVideo(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int minSelect, int selectType, boolean showCamera) {
        PictureWindowAnimationStyle windowAnimationStyle = new PictureWindowAnimationStyle();
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pictureSelector
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(3)// 每行显示个数
                .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
                .selectionMode(selectType)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(false)// 是否可预览图片 true or false
                .isCamera(showCamera)// 是否显示拍照按钮 true or false
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                .maxVideoSelectNum(maxSelect)
                .minVideoSelectNum(minSelect)
                .querySpecifiedFormatSuffix(PictureMimeType.ofMP4())
//                .enableCrop(false)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
//                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isOpenClickSound(false)// 是否开启点击声音 true or false
                .selectionData(list)// 是否传入已选图片 List<LocalMedia> list
                .isPreviewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .compressQuality(75)
                .minimumCompressSize(100)
                .setPictureWindowAnimationStyle(windowAnimationStyle)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    /**
     * 多模式
     *
     * @param list 已选列表
     */
    public static void selectVideo(PictureSelector pictureSelector, List<LocalMedia> list, int maxSelect, int minSelect, int selectType, boolean showCamera, int requestCode) {
        PictureWindowAnimationStyle windowAnimationStyle = new PictureWindowAnimationStyle();
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pictureSelector
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(3)// 每行显示个数
                .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
                .selectionMode(selectType)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(false)// 是否可预览图片 true or false
                .isCamera(showCamera)// 是否显示拍照按钮 true or false
                .querySpecifiedFormatSuffix(PictureMimeType.ofMP4())
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                .maxVideoSelectNum(maxSelect)
//                .minVideoSelectNum(minSelect)
                .maxSelectNum(maxSelect)
                .minSelectNum(1)
//                .enableCrop(false)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
//                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isOpenClickSound(false)// 是否开启点击声音 true or false
                .selectionData(list)// 是否传入已选图片 List<LocalMedia> list
                .isPreviewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .compressQuality(75)
                .minimumCompressSize(100)
                .setPictureWindowAnimationStyle(windowAnimationStyle)
                .forResult(requestCode);//结果回调onActivityResult code

    }


    public static void openCamera(PictureSelector pictureSelector) {
        PictureWindowAnimationStyle windowAnimationStyle = new PictureWindowAnimationStyle();
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pictureSelector
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
                .isCamera(true)
                .isCompress(true)
                .compressQuality(75)
                .minimumCompressSize(100)
                .setPictureWindowAnimationStyle(windowAnimationStyle)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public static void openCamera(PictureSelector pictureSelector, int resultcode) {
        PictureWindowAnimationStyle windowAnimationStyle = new PictureWindowAnimationStyle();
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pictureSelector
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
                .selectionMode(PictureConfig.SINGLE)
                .isCamera(true)
                .isCompress(true)
                .compressQuality(75)
                .minimumCompressSize(100)
                .setPictureWindowAnimationStyle(windowAnimationStyle)
                .forResult(resultcode);
    }


    /***
     * set preview image
     *
     * @param position
     * @param medias
     */
    public static void externalPicturePreview(Context context, int position, ArrayList<LocalMedia> medias) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent(context, PictureExternalPreviewActivity.class);
            intent.putExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, medias);
            intent.putExtra(PictureConfig.EXTRA_POSITION, position);
            context.startActivity(intent);
//            activity.overridePendingTransition(R.anim.a5, 0);
        }
    }


    /***
     * set preview video
     */
    public static void externalVideoPreview(Context context, String videoPath) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent(context, PictureSimpleVideoPlayActivity.class);
            intent.putExtra(PictureConfig.EXTRA_VIDEO_PATH, videoPath);
            context.startActivity(intent);
        }
    }
}
