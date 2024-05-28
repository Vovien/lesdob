package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.home.R
import com.tubewiki.home.bean.CommonQuestionBean
import com.tubewiki.home.databinding.ItemQuestionCategoryDetailLayoutBinding

/**
 *
 */
class QuestionCategoryDetailAdapter :
    BindingQuickAdapter<CommonQuestionBean.Topic, ItemQuestionCategoryDetailLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: CommonQuestionBean.Topic) {
        holder.getViewBinding<ItemQuestionCategoryDetailLayoutBinding>().apply {

            tvTitle.text = item.topicName
            tvNum.text = "${item.articleNum}篇文章"

        }
    }
}