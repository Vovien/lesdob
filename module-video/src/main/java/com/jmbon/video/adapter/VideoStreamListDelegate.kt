package com.jmbon.video.adapter

import android.view.ViewGroup
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.SizeUtil
import com.jmbon.middleware.adapter.EventViewBindingDelegate
import com.jmbon.middleware.bean.event.CircleChangedEvent
import com.jmbon.middleware.utils.*
import com.jmbon.video.R
import com.jmbon.video.bean.VideoStream
import com.jmbon.video.databinding.ItemVideoListVideoBinding
import kotlin.math.max
import kotlin.math.min


/**
 * 视频流、广告列表的代理
 */
class VideoStreamListDelegate(val adClick: (item: VideoStream.VideoStreamResult, position: Int) -> Unit) :
    EventViewBindingDelegate<CircleChangedEvent, VideoStream.VideoStreamResult, ItemVideoListVideoBinding>() {
    var halfWidth = 0
    var maxHeight = 0f
    var miniHeight = 0f

    init {
        halfWidth = SizeUtil.getWidth() / 2 - 24f.dp()
        maxHeight = halfWidth * 1.5f
        miniHeight = halfWidth * 0.75f
    }

    override fun onBindViewHolder(
        holder: ViewBindingViewHolder<ItemVideoListVideoBinding>,
        item: VideoStream.VideoStreamResult,
    ) {
        holder.binding.apply {
            var cover = ""
            var playCount = ""
            val height = getItemHeight(item)
            if (item.type == "flow_video") {
                if (item.flowVido.title.isEmpty())
                    textVideoDesc.gone()
                else textVideoDesc.visible()
                textVideoDesc.text = item.flowVido.title
                textAD.gone()
                imageAvatar.visible()
                textName.visible()
                val user = item.flowVido.user
                textName.text = user.userName
                imageAvatar.loadCircle(user.avatarFile)
                cover = item.flowVido.cover
                playCount = item.flowVido.fakePlayCount.coverToTenThousand()
                root.setOnSingleClickListener({
                    adClick(item, holder.layoutPosition)
                })
            } else {
                // 广告
                if (item.adv.originality.isEmpty())
                    textVideoDesc.gone()
                else textVideoDesc.visible()
                // 高度计算
                textVideoDesc.text = item.adv.originality

                if (item.adv.label.isNotNullEmpty()) {
                    textAD.visible()
                } else {
                    textAD.gone()
                }
                textAD.text = item.adv.label
                imageAvatar.gone()
                textName.gone()
                cover =
                    if (item.adv.resources.isNotNullEmpty()) item.adv.resources[0].src else item.adv.covers
                playCount = item.adv.views.coverToTenThousand()
                root.setOnSingleClickListener({
                    adClick(item, holder.layoutPosition)
                })
            }
            textPlayCount.text = playCount
            if (imageCover.tag == null || imageCover.tag != height) {
                val imageParams = imageCover.layoutParams
                imageParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                imageParams.height = height
                imageCover.layoutParams = imageParams
                imageCover.tag = height
            }
            if (cover.isNotEmpty()) {
                imageCover.setBackgroundResource(0)

                cover.urlToWep.load(imageCover)
            } else {
                imageCover.setBackgroundResource(R.color.ColorF5F5)
                imageCover.setImageDrawable(null)
            }
        }
    }

    override fun setEventData(
        it: CircleChangedEvent,
        item: Any,
        viewBinding: ItemVideoListVideoBinding,
    ) {
        if (it.type == CircleChangedEvent.JOIN) {
            if (item is VideoStream.VideoStreamResult) {
                item.circle.forEach { circle ->
                    if (it.id.toInt() == circle.id) {
                        circle.isJoin = it.isJoin
                    }
                }
            }
        }
    }

    private fun getItemHeight(item: VideoStream.VideoStreamResult): Int {
        return if (item.imageHeight > 0) {
            item.imageHeight
        } else {
            if (item.type == "flow_video") {
                getItemHeight(item, item.flowVido.height, item.flowVido.width)
            } else {
                if (item.adv.resources.isNotNullEmpty()) {
                    getItemHeight(item, item.adv.resources[0].height, item.adv.resources[0].width)
                } else {
                    item.imageHeight = (miniHeight.toInt()..maxHeight.toInt()).random()
                    item.imageHeight
                }
            }
        }
    }

    private fun getItemHeight(
        item: VideoStream.VideoStreamResult,
        height: Float,
        width: Float,
    ): Int {
        if (height == 0f || width == 0f) {
            item.imageHeight = (miniHeight.toInt()..maxHeight.toInt()).random()
        } else {
            var imageHeight = (height / width) * halfWidth
            imageHeight = min(max(imageHeight, miniHeight), maxHeight)
            item.imageHeight = imageHeight.toInt()
        }
        return item.imageHeight
    }

}