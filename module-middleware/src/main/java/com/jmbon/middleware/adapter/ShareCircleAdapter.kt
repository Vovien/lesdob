package com.jmbon.middleware.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.ShareCircleBean
import com.jmbon.middleware.databinding.ItemShareCircleLayoutBinding
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius

/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   :
 * version: 1.0
 */
class ShareCircleAdapter :
    BindingQuickAdapter<ShareCircleBean.Circle, ItemShareCircleLayoutBinding>() {
    override fun convert(holder: BaseBindingHolder, item: ShareCircleBean.Circle) {
        holder.getViewBinding<ItemShareCircleLayoutBinding>().apply {
            ivCover.loadRadius(
                item.covers,
                8f.dp(), R.drawable.icon_topic_placeholder
            )

            tvIntro.text = "简介：${item.description}"
            tvMessage.text = item.name

            tvAnswerTag.text = "${item.questionCount}问答"
            tvPersonTag.text = "${item.memberCount}圈友"

        }
    }

}