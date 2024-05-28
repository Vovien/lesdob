package com.tubewiki.mine.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.LayerDrawable
import android.util.Log
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.coverToTenThousand
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.loadWebpGifImgUrl
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ItemMineCollectionVideoBinding


/**
 * desc   : 视频/视频收藏adapter
 * version: 1.0
 */
class MineCollectionVideoAdapter(var personUid: Int) :
    BindingQuickAdapter<VideoDetail.VideoData, ItemMineCollectionVideoBinding>() {


    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: VideoDetail.VideoData) {
        holder.getViewBinding<ItemMineCollectionVideoBinding>().apply {
            Log.d(
                "convert",
                "onBindViewHolder>>${holder.adapterPosition}: ${holder.itemView.hashCode()} "
            )
//            //自己的个人主页显示播放量，他人个人主页和收藏页面显示点赞量
//            if (personUid != 0) {
//                if (personUid == Constant.getUserId()) {
//                    tvCount.text = item.playCount.coverToTenThousand()
//                    ivZan.setImageResource(R.drawable.icon_video_play_count_flag)
//                } else {
//                    tvCount.text = item.giveCount.coverToTenThousand()
//                    ivZan.setImageResource(R.drawable.icon_video_like)
//                }
//
//            } else {
//                tvCount.text = item.giveCount.coverToTenThousand()
//                ivZan.setImageResource(R.drawable.icon_video_like)
//            }

            if (item.dynamicCover.isNullOrEmpty()) {
                ivCover.load(item.cover, R.drawable.icon_video_image_placeholder)
            } else {
                ivCover.loadWebpGifImgUrl(
                    item.dynamicCover,
                    R.drawable.icon_video_image_placeholder
                )
            }

        }
    }
}