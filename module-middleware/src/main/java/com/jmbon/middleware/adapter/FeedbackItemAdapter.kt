package com.jmbon.middleware.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.GeneralConfBean
import com.jmbon.middleware.bean.ShareCircleBean
import com.jmbon.middleware.databinding.ItemFeedbackItemLayoutBinding
import com.jmbon.middleware.databinding.ItemShareCircleLayoutBinding
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius

/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   :
 * version: 1.0
 */
class FeedbackItemAdapter :
    BindingQuickAdapter<GeneralConfBean.Tag, ItemFeedbackItemLayoutBinding>() {

    var selectedPos = 0

    override fun convert(holder: BaseBindingHolder, item: GeneralConfBean.Tag) {
        holder.getViewBinding<ItemFeedbackItemLayoutBinding>().apply {

            if (selectedPos == holder.adapterPosition) {
                tvTitle.setBackgroundResource(R.drawable.shape_feedback_selected_bg)
            } else {
                tvTitle.setBackgroundResource(R.drawable.shape_feedback_not_selected_bg)
            }

            tvTitle.text = item.labelName

        }
    }

}