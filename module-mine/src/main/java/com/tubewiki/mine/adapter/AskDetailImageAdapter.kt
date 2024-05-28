package com.tubewiki.mine.adapter

import android.view.View
import androidx.core.view.setPadding
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ItemImageLayoutBinding
import com.jmbon.widget.picture.SelectPhotoUtils
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import java.util.*

/**
 * @author : leimg
 * time   : 2021/4/13
 * desc   :
 * version: 1.0
 */
class AskDetailImageAdapter : BindingQuickAdapter<LocalMedia, ItemImageLayoutBinding>() {

    var limitImage = listOf<String>()

    override fun convert(holder: BaseBindingHolder, item: LocalMedia) {
        holder.getViewBinding<ItemImageLayoutBinding>().apply {
            if (PictureMimeType.isHasVideo(item.mimeType)) {
                ivVideo.visibility = View.VISIBLE
            } else {
                ivVideo.visibility = View.GONE
            }
            if (limitImage.contains(item.path)) {
                ivPic.setPadding(6f.dp())
                ivPic.loadRadius(item.path, 4f.dp(), R.drawable.icon_topic_placeholder)
            } else {
                ivPic.setPadding(0f.dp())
                ivPic.loadRadius(item.path, 8f.dp(), R.drawable.icon_topic_placeholder)
            }

            root.setOnClickListener {
                SelectPhotoUtils.externalPicturePreview(
                    context, holder.adapterPosition,
                    data as ArrayList<LocalMedia>
                )
            }
        }
    }
}
