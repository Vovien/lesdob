package com.jmbon.middleware.adapter

import android.text.TextUtils
import android.view.View
import androidx.core.view.setPadding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.ItemSelectedPicVideoBinding
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia


/**
 * @author : wangzhen
 * time   : 2021/3/29
 * desc   : 添加图片视频适配器
 * version: 1.0
 */
class PicVideoAdapter :
    BaseQuickAdapter<LocalMedia, BaseViewHolder>(R.layout.item_selected_pic_video) {

    var limitImage = listOf<String>()


    override fun convert(holder: BaseViewHolder, item: LocalMedia) {
        val bind = ItemSelectedPicVideoBinding.bind(holder.itemView)
        if (item.duration > 0 || PictureMimeType.MIME_TYPE_VIDEO.equals(item.mimeType)) {
            bind.ivPlay.visibility = View.VISIBLE
        } else {
            bind.ivPlay.visibility = View.GONE
        }

        val path = if (TextUtils.isEmpty(item.compressPath)) item.path else item.compressPath

        //bind.ivAlbum.load(path)

        if (limitImage.contains(item.path)) {
            bind.ivAlbum.setPadding(6f.dp())
            bind.ivAlbum.loadRadius(path, 4f.dp(), R.drawable.icon_topic_placeholder)
        } else {
            bind.ivAlbum.setPadding(0f.dp())
            bind.ivAlbum.loadRadius(path, 8f.dp(), R.drawable.icon_topic_placeholder)
        }

    }
}