package com.jmbon.video.adapter

import android.graphics.Color
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.VideoAlbum
import com.jmbon.middleware.utils.Color
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.jmbon.video.R
import com.jmbon.video.databinding.ItemVidoeAlbumBinding

class VideoAlbumAdapter : BindingQuickAdapter<VideoAlbum.VideoAlbumItem, ItemVidoeAlbumBinding>() {

    var currentPlayPosition = -1

    override fun convert(holder: BaseBindingHolder, item: VideoAlbum.VideoAlbumItem) {
        holder.getViewBinding<ItemVidoeAlbumBinding>().apply {
            textVideoTitle.text = item.mainTitle
            textVideoDesc.text = item.description

            textVideoDuration.text = DateFormatUtil.secondToHour(item.duration)
            imageAlbumCover.loadRadius(item.cover, 8f.dp())
            if (currentPlayPosition == holder.adapterPosition) {
                root.setBackgroundColor(R.color.white10.Color)
            } else {
                root.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}