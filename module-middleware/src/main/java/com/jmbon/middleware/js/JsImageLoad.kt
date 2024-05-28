package com.jmbon.middleware.js

import android.content.Context
import android.webkit.JavascriptInterface
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.utils.GsonUtil
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.js.ArticleAdv
import com.jmbon.middleware.bean.js.ExperienceBodyAdv
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.widget.data.NetSpeedEvent
import com.jmbon.widget.picture.SelectPhotoUtil
import com.jmbon.widget.picture.SelectPhotoUtils
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus


class JsImageLoad(private val mContext: Context) {
    @JavascriptInterface
    fun imageBrowsing(current: Int, images: String) {
        val imageList =
            GsonUtils.fromJson<ArrayList<String>>(images, GsonUtil.getListType(String::class.java))
        val list = arrayListOf<LocalMedia>()
        imageList.forEach {
            if (it.isNotEmpty()) {
                val media = LocalMedia(if (it == "error") "web_view_error" else it, 0, 0, "")
                list.add(media)
            }
        }
        SelectPhotoUtils.externalPicturePreview(ActivityUtils.getTopActivity(), current, list)
    }

    @JavascriptInterface
    fun showImage(images: String, current: Int) {
        val imageList =
            GsonUtils.fromJson<ArrayList<String>>(images, GsonUtil.getListType(String::class.java))
        val list = arrayListOf<LocalMedia>()
        imageList.forEach {
            if (it.isNotEmpty()) {
                val media = LocalMedia(if (it == "error") "web_view_error" else it, 0, 0, "")
                list.add(media)
            }
        }
        MainScope().launch {
            SelectPhotoUtils.externalPicturePreview(ActivityUtils.getTopActivity(), current, list)
        }
    }


    fun imageVideo(current: Int, images: String, videos: String) {
        val list = arrayListOf<LocalMedia>()
        images.split(",").forEach {
            if (!it.isNullOrEmpty()) {
                val media = LocalMedia(it, 0, 0, "")
                list.add(media)
            }
        }
        videos.split(",").forEach {
            if (!it.isNullOrEmpty()) {
                val media = LocalMedia(it, 0, 0, PictureMimeType.MIME_TYPE_VIDEO)
                list.add(media)
            }
        }

        SelectPhotoUtils.externalPicturePreview(ActivityUtils.getTopActivity(), current, list)
    }

    @JavascriptInterface
    fun playDetailVideo(
        url: String,
        cover: String,
        videoId: String,
        itemId: String,
        itemType: String
    ) {
        //如果不是转换后的指定格式视频，就普通播放
        if (videoId.isNullOrEmpty()) {
            SelectPhotoUtil.externalVideoPreview(ActivityUtils.getTopActivity(), url)
        } else {
            ARouter.getInstance().build("/video/details")
                .withString("aliyun_video_id", videoId)
                .withString("video_url", url)
                .withString("item_id", itemId)
                .withString("type", itemType)
                .navigation()
        }

    }

    @JavascriptInterface
    fun playVideo(url: String) {
        //播放视频监听播放速度
        EventBus.getDefault().post(NetSpeedEvent(5))
        SelectPhotoUtil.externalVideoPreview(ActivityUtils.getTopActivity(), url)
        //
//        val uri =
//            Uri.parse(url)
//        // 调用系统自带的播放器来播放流媒体视频
//        // 调用系统自带的播放器来播放流媒体视频
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.setDataAndType(uri, "video/mp4")
//        startActivity(intent)
    }

    @JavascriptInterface
    fun showVideo(url: String, poster: String, videoId: String, itemId: String, itemType: String) {
        //播放视频监听播放速度
        // EventBus.getDefault().post(NetSpeedEvent(5))
        SelectPhotoUtil.externalVideoPreview(ActivityUtils.getTopActivity(), url)
    }


    @JavascriptInterface
    fun adClick(jsonData: String) {
        val adv = GsonUtils.fromJson(jsonData, ExperienceBodyAdv::class.java)
        BannerHelper.onClick(
            CommonBanner(
                item_type = adv.itemType,
                identity = adv.identity
            )
        )
    }

    @JavascriptInterface
    fun advArticleClick(jsonData: String) {
        val adv = GsonUtils.fromJson(jsonData, ArticleAdv::class.java)
        BannerHelper.onClick(
            CommonBanner(
                item_type = adv.itemType,
                identity = adv.identity
            )
        )
    }

    companion object {
        private const val TAG = "JsImageLoad"
    }
}