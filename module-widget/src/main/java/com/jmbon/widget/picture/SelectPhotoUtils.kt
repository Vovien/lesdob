package com.jmbon.widget.picture

import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jmbon.widget.R
import com.luck.picture.lib.PictureExternalPreviewActivity
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.PictureSimpleVideoPlayActivity
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorUIStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import com.luck.picture.lib.tools.DoubleUtils
import java.util.regex.Pattern

object SelectPhotoUtils {

    /**
     * 单选模式
     *
     * @param enableCrop 裁切
     */
    fun selectSinglePic(
        pictureSelector: PictureSelector,
        enableCrop: Boolean,
        callBack: OnResultCallbackListener<LocalMedia>, x: Int = 1, y: Int = 1,
    ) {
        val windowAnimationStyle = PictureWindowAnimationStyle()
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out)
        // 进入相册 以下是例子：用不到的api可以不写
        pictureSelector
            .openGallery(PictureMimeType.ofImage())
            .imageSpanCount(3) // 每行显示个数
            .imageEngine(GlideEngine.createGlideEngine())

            .selectionMode(PictureConfig.SINGLE)
            .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
            .isPreviewImage(true)
            .isCamera(false)
            .isEnableCrop(enableCrop)
            .isPreviewEggs(false)
            .isCompress(true)
            .isSingleDirectReturn(true)
            .isPageStrategy(true)
            .withAspectRatio(x, y)
            .showCropGrid(false)
            .showCropFrame(false)
            .rotateEnabled(false)
            .isGif(false)
            .isOpenClickSound(false)
            .compressQuality(75)
            .minimumCompressSize(100)
            .setPictureWindowAnimationStyle(windowAnimationStyle)
            .forResult(callBack)
    }

    /**
     * 多模式
     *
     * @param list 已选列表
     */
    fun selectPic(
        pictureSelector: PictureSelector,
        list: List<LocalMedia?>?,
        maxSelect: Int,
        minSelect: Int,
        callBack: OnResultCallbackListener<LocalMedia>,
    ) {
        // 进入相册 以下是例子：用不到的api可以不写
        val windowAnimationStyle = PictureWindowAnimationStyle()
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out)
        pictureSelector
            .openGallery(PictureMimeType.ofImage()) //全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            //                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
            .imageEngine(GlideEngine.createGlideEngine())
            .imageSpanCount(3) // 每行显示个数
            .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
            .maxSelectNum(maxSelect) // 最大图片选择数量 int
            .minSelectNum(minSelect) // 最小选择数量 int
            .selectionMode( PictureConfig.MULTIPLE) // 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .isPreviewImage(true) // 是否可预览图片 true or false
            .isCamera(false) // 是否显示拍照按钮 true or false
            .isPageStrategy(true) // 分页加载
            //                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
            .isMaxSelectEnabledMask(true) // 选择数到了最大阀值列表是否启用蒙层效果
            .isCompress(true) // 是否压缩 true or false
            //                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            //                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
            .isGif(false) // 是否显示gif图片 true or false
            //                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
            //                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
            //                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
            //                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
            .isOpenClickSound(false) // 是否开启点击声音 true or false
            .selectionData(list) // 是否传入已选图片 List<LocalMedia> list
            .isPreviewEggs(false) // 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
            //                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
            //                .minimumCompressSize(100)// 小于100kb的图片不压缩
            //                .synOrAsy(true)//同步true或异步false 压缩 默认同步
            //                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
            //                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
            //                .isDragFrame(false)// 是否可拖动裁剪框(固定)
            .compressQuality(75)
            .minimumCompressSize(100)
            .setPictureWindowAnimationStyle(windowAnimationStyle)
            .forResult(callBack) //结果回调onActivityResult code
    }


    /**
     * 多模式
     *
     * @param list 已选列表
     */
    fun selectVideo(
        pictureSelector: PictureSelector,
        list: List<LocalMedia?>?,
        maxSelect: Int,
        minSelect: Int,
        showCamera: Boolean,
        callBack: OnResultCallbackListener<LocalMedia>,
    ) {
        val windowAnimationStyle = PictureWindowAnimationStyle()
        windowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out)
        pictureSelector
            .openGallery(PictureMimeType.ofVideo()) //全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            //                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
            .imageEngine(GlideEngine.createGlideEngine())
            .imageSpanCount(3) // 每行显示个数
            .setPictureUIStyle(PictureSelectorUIStyle.ofSelectTotalStyle())
            .selectionMode(if(maxSelect>1) PictureConfig.MULTIPLE else PictureConfig.SINGLE) // 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .isPreviewImage(false) // 是否可预览图片 true or false
            .isCamera(showCamera) // 是否显示拍照按钮 true or false
            .querySpecifiedFormatSuffix(PictureMimeType.ofMP4()) //                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
            .isMaxSelectEnabledMask(true) // 选择数到了最大阀值列表是否启用蒙层效果
            .maxVideoSelectNum(maxSelect) //                .minVideoSelectNum(minSelect)
            .maxSelectNum(maxSelect)
            .minSelectNum(minSelect) //                .enableCrop(false)// 是否裁剪 true or false
            .isCompress(true) // 是否压缩 true or false
            //                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            //                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
            .isGif(false) // 是否显示gif图片 true or false
            //                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
            //                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
            //                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
            //                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
            .isOpenClickSound(false) // 是否开启点击声音 true or false
            .selectionData(list) // 是否传入已选图片 List<LocalMedia> list
            .isPreviewEggs(false) // 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
            //                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
            //                .minimumCompressSize(100)// 小于100kb的图片不压缩
            //                .synOrAsy(true)//同步true或异步false 压缩 默认同步
            //                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
            //                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
            //                .isDragFrame(false)// 是否可拖动裁剪框(固定)
            .compressQuality(75)
            .minimumCompressSize(100)
            .setPictureWindowAnimationStyle(windowAnimationStyle)
            .forResult(callBack) //结果回调
    }


    /***
     * set preview image
     *
     * @param position
     * @param medias
     */
    fun externalPicturePreview(context: Context, position: Int, medias: ArrayList<LocalMedia>) {


        if (!DoubleUtils.isFastDoubleClick()) {
            medias.forEach {
                if (it.path.isHttpUrl() && (it.mimeType == PictureMimeType.MIME_TYPE_VIDEO || it.path.endsWith(
                        "mp4"))
                )
                    it.videoCover = "${it.path}?x-oss-process=video/snapshot,t_500,m_fast,ar_auto"
                else it.videoCover = it.path
            }
            val intent = Intent(context, PictureExternalPreviewActivity::class.java)
            intent.putExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, medias)
            intent.putExtra(PictureConfig.EXTRA_POSITION, position)
            context.startActivity(intent)
            //            activity.overridePendingTransition(R.anim.a5, 0);
        }
    }



    /***
     * set preview image
     *
     * @param position
     * @param medias
     */
    fun playVideo(context: Context,  medias: LocalMedia) {
        if (!DoubleUtils.isFastDoubleClick()) {
            val intent = Intent(context, PictureSimpleVideoPlayActivity::class.java)
            intent.putExtra("videoPath", medias.path)
            context.startActivity(intent)
            //            activity.overridePendingTransition(R.anim.a5, 0);
        }

    }

    private fun String?.isHttpUrl(): Boolean {
        return if (this.isNullOrEmpty()) {
            false
        } else {
            var isurl = false
            val regex = ("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                    + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)") //设置正则表达式
            val pat = Pattern.compile(regex.trim { it <= ' ' }) //比对
            val mat = pat.matcher(this.trim { it <= ' ' })
            isurl = mat.matches() //判断是否匹配
            if (isurl) {
                isurl = true
            }
            isurl
        }
    }
}