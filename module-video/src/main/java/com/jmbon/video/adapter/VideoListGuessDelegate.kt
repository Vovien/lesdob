package com.jmbon.video.adapter

import android.graphics.Typeface
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.ViewBindingDelegate
import com.jmbon.middleware.utils.ViewBindingViewHolder
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.video.bean.VideoStream
import com.jmbon.video.databinding.ItemVideoListGuessBinding

/**
 * 猜你喜欢
 */
class VideoListGuessDelegate :
    ViewBindingDelegate<VideoStream.VideoStreamResult, ItemVideoListGuessBinding>() {


    override fun onBindViewHolder(
        holder: ViewBindingViewHolder<ItemVideoListGuessBinding>,
        item: VideoStream.VideoStreamResult,
    ) {
        holder.binding.apply {
//            textTitle.typeface = Typeface.createFromAsset(this.root.context.assets, "fonts/roboto_black.ttf")
            val textList = arrayListOf<TextView>()
            textList.add(textInfo1.root)
            textList.add(textInfo2.root)
            textList.add(textInfo3.root)
            textList.add(textInfo4.root)
            item.recommendLabel.forEachIndexed { index, recommendVideo ->
                textList[index].apply {
                    text = recommendVideo.longLabel
                    setOnSingleClickListener({
                        ARouter.getInstance().build("/video/list")
                            .withString(TagConstant.CATEGORY_NAME, recommendVideo.label)
                            .withInt(TagConstant.CATEGORY_ID, recommendVideo.id)
                            .navigation()
                    })
                }
            }
        }
    }
}
