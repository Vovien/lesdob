package com.tubewiki.mine.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.loadUrlRadius
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.FollowTopicData
import com.tubewiki.mine.databinding.ItemFollowTopicBinding

/**
 * time   : 2021/5/25
 * desc   : 我的关注-话题适配器
 * version: 1.0
 */
class FollowTopicAdapter :
    BindingQuickAdapter<FollowTopicData.Topic, ItemFollowTopicBinding>() {
    init {
        addChildClickViewIds(R.id.sb_focus_on)
    }

    override fun convert(holder: BaseBindingHolder, item: FollowTopicData.Topic) {
        holder.getViewBinding<ItemFollowTopicBinding>().apply {


            ivHeader.loadUrlRadius(item.topicPic, SizeUtils.dp2px(8f))
            tvTopicName.text = item.topicTitle
            tvAnswerAmount.text = "${item.questionCount} 问答"
            tvFollowAmount.text = "${item.focusCount} 关注"

            sbFocusOn.isSelected = true

            root.setOnClickListener {
                ARouter.getInstance().build("/home/activity/topic_details")
                    .withInt(TagConstant.TOPIC_ID, item.topicId)
                    .navigation()
            }
        }
    }
}