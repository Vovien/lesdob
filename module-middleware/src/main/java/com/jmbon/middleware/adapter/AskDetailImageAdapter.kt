package com.jmbon.middleware.adapter

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.ItemImageLayoutBinding
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.jmbon.middleware.utils.videoUrlToWep
import com.jmbon.widget.picture.SelectPhotoUtils
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia

/**
 * @author : leimg
 * time   : 2021/4/13
 * desc   :
 * version: 1.0
 */
class AskDetailImageAdapter(var itemId: String = "", var description: String = "") :
    BindingQuickAdapter<LocalMedia, ItemImageLayoutBinding>() {

    var checkVideoUrl =
        "https://video.jmbon.com/5bb55275e59d4c1ab052549702571098/03657e820b804cfa96a670957730196f-2402226a01198aa3628bb931040a3a89-ld.mp4"
    var refuseVideoUrl =
        "https://video.jmbon.com/60a5ecc2056940769ec61bc12d302834/c6258b8a0a1d40a480b544b14107c50e-f41b11521662afc6706469d187f3e9b8-ld.mp4"

    override fun convert(holder: BaseBindingHolder, item: LocalMedia) {
        holder.getViewBinding<ItemImageLayoutBinding>().apply {

            if (PictureMimeType.isHasVideo(item.mimeType)) {
                ivPic.loadRadius(
                    item.path.videoUrlToWep,
                    8f.dp(),
                    R.drawable.icon_topic_placeholder
                )
                ivVideo.visibility = View.VISIBLE
                ivVideo.setImageResource(R.drawable.icon_video_play2)
            } else {
                ivPic.loadRadius(item.path, 8f.dp(), R.drawable.icon_topic_placeholder)
                ivVideo.visibility = View.GONE
            }
            root.setOnClickListener {
                if (PictureMimeType.isHasVideo(item.mimeType)) {
                    if (item.path.contains(checkVideoUrl) || item.path.contains(refuseVideoUrl)) {
                        //审核中或者审核拒绝的占位视频使用本地播放器
                        SelectPhotoUtils.playVideo(context, item)

                        return@setOnClickListener
                    }

                    //SelectPhotoUtils.playVideo(context, item)
                    ARouter.getInstance().build("/video/details")
                        .withString("aliyun_video_id", item.path.substringAfter("aliyunVideoId="))
                        .withString("video_url", item.path)
                        .withString("item_id", itemId)
                        .withString("description", description)
                        .withString("type", "2") //原内容类型【0:无类型，1：文章，2：问题，3：回答】
                        .navigation()

                } else {
                    SelectPhotoUtils.externalPicturePreview(
                        context, holder.layoutPosition,
                        data as ArrayList<LocalMedia>
                    )
                }

            }
        }
    }
}
