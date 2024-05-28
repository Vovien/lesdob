package com.jmbon.video.adapter

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.isGone
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.SizeUtil
import com.jmbon.middleware.adapter.EventAdapter
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.utils.ShareAnimatorUtils.caseDetailAnimator
import com.jmbon.video.R
import com.jmbon.video.databinding.ItemVideoBinding


class VideoDetailsItemAdapter :
    EventAdapter<FocusChangedEvent, VideoDetail.VideoData, ItemVideoBinding>() {

    companion object {
        const val VIDEO_RESET = "video_reset"
        const val START_SHARE_ANIMATOR = "start_share_animator"
        const val CANCEL_SHARE_ANIMATOR = "cancel_share_animator"
    }

    var canPreLoader = true

    init {
        addChildClickViewIds(R.id.textAlbum)
        addChildClickViewIds(R.id.textReply)
        addChildClickViewIds(R.id.clComments)
        addChildClickViewIds(R.id.clCollection)
        addChildClickViewIds(R.id.textName)
        addChildClickViewIds(R.id.textDesc)
        addChildClickViewIds(R.id.adv_image_layout)
        // 分享
        addChildClickViewIds(R.id.clShare)
        addChildClickViewIds(R.id.imageAvatar)
        addChildClickViewIds(R.id.text_get_case)
    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: VideoDetail.VideoData) {
        holder.getViewBinding<ItemVideoBinding>().apply {

            textGetCase.text = item.button
            textGetCase.isVisible = item.type == "adv"
            textAlbum.gone()
            textReply.visible()

            seekBar.setPadding(0, seekBar.paddingTop, 0, seekBar.paddingBottom)
            seekBar.setProgress(0f)
            seekBar.max = 10000f //初始默认值设置很大
            seekBar.min = 0f

            item.user.apply {
                if (item.type == "adv") {
                    textName.text = "@${userName}"
                    textName.isVisible = true
                } else {
                    textName.isVisible = false
                }
                textName.requestLayout()
            }

            // 图片 设置大小
            if (item.height > 0 && item.width > 0) {
                // 计算大小
                imageThumb.layoutParams.height =
                    (SizeUtil.getWidth() / (item.width.toFloat() / item.height)).toInt()
            }
            GlideUtil.getInstance().loadImgUrl(imageThumb, item.playUrlMp4.videoUrlToWep, 0)

            // 提示
            // 存在风险类型【0：不存在风险，1：争议，2：谣言，3：广告，4：摘自】
//            when (item.riskType) {
//                1 -> textRiskTips.text = "[该内容存在争议，请谨慎辨别]"
//                2 -> textRiskTips.text = "[内容真实性存疑，为了避免对您造成误导，请谨慎甄别]"
//                3 -> textRiskTips.text = "[该内容涉及营销推广正在审核中，请谨慎参考]"
//                4 -> textRiskTips.text = "[摘自：${item.riskFromTitle}]"
//                else -> textRiskTips.gone()
//            }
            if (item.riskType != 0) {
                layoutRisk.visible()
                textRiskTips.text = item.riskFromTitle
            } else {
                layoutRisk.gone()
            }
            // 描述
            textDesc.text = item.description.ifEmpty { item.title }
            //时间
            textTime.gone()


            // 准备缓存 加载10秒
//            val playUrl = item.getAvailablePlayAddress()
//            if (canPreLoader && playUrl.isNotNullEmpty() && cachedVideoList.contains(playUrl)
//                    .not()
//            ) {
//                cachedVideoList.add(playUrl)
//                // 缓存
//                mediaLoader.load(playUrl, 1000 * 15)
//            }

            clBox.isVisible = item.advList.isNotEmpty()
            if (item.advList.isNotEmpty()) {
                val randomList = item.advList.shuffled()
                clBox.setData(randomList.map { it })
                clBox.setOnSingleClickListener({
                    val adv = clBox.getCurrentData()
                    adv?.apply {
                        BannerHelper.onClick(
                            CommonBanner(
                                item_type = this.itemType,
                                identity = identity
                            )
                        )
                    }
                })
            }
        }
    }

    private fun ItemVideoBinding.caseAnimator(item: VideoDetail.VideoData) {
        if (item.type == "adv") {
            caseDetailAnimator(textGetCase)
        }
    }

    override fun convert(
        holder: BaseBindingHolder,
        item: VideoDetail.VideoData,
        payloads: List<Any>,
    ) {
        holder.getViewBinding<ItemVideoBinding>().apply {
            if (payloads.isNotNullEmpty()) {
                when (payloads[0]) {

                    VIDEO_RESET -> {
                        if (imageThumb.isGone())
                            imageThumb.visible()
                        // seek
                        seekBar.setProgress(0f)
                    }


                    START_SHARE_ANIMATOR -> {
                        caseAnimator(item)
                    }

                    CANCEL_SHARE_ANIMATOR -> {
                        textGetCase.setBackgroundResource(R.drawable.bg_1affffff_with_radius_9dp)
                        ShareAnimatorUtils.cancelShareVideoAnimator(ivShare, tvShare)
                    }

                    else -> {

                    }
                }
            }
        }
    }


    private val resetConstraintSet: ConstraintSet = ConstraintSet()

    private fun ItemVideoBinding.reSetConnect() {
//        textSwitcher.visible()
        val params = this.clBox.layoutParams as ConstraintLayout.LayoutParams
        params.endToEnd = R.id.viewBaseLine
        this.clBox.layoutParams = params
    }


    override fun setEventData(
        it: FocusChangedEvent,
        item: VideoDetail.VideoData,
        viewBinding: ItemVideoBinding,
    ) {
    }
}