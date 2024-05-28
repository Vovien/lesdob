package com.tubewiki.mine.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ItemFollowQuestionsBinding

/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 我的提问adapter
 * version: 1.0
 */
class FollowQuestionAdapter :
    BindingQuickAdapter<Question, ItemFollowQuestionsBinding>() {

    init {
        addChildClickViewIds(R.id.sb_focus_on)
    }

    override fun convert(holder: BaseBindingHolder, item: Question) {
        holder.getViewBinding<ItemFollowQuestionsBinding>().apply {
            tvTitle.text = item.questionContent
            tvAnswer.text = "${item.answerCount}个回答"

            sbFocusOn.isSelected = true

            root.setOnClickListener {
                ARouter.getInstance().build("/question/activity/ask_detail")
                    .withInt(TagConstant.QUESTION_ID, item.questionId)
                    .navigation()
            }
        }

    }
}
