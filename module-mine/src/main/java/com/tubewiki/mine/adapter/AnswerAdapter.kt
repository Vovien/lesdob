package com.tubewiki.mine.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.databinding.ItemMineAnswerBinding

/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 我的回答adapter
 * version: 1.0
 */
class AnswerAdapter :
    BindingQuickAdapter<Question, ItemMineAnswerBinding>() {


    override fun convert(holder: BaseBindingHolder, item: Question) {
        holder.getViewBinding<ItemMineAnswerBinding>().apply {
            tvTitle.text = item.questionContent
            item.answer.let {

                if (it.answerContent.isNullOrEmpty()) {
                    if (it.images.isNotEmpty()) {
                        tvContent.text = "[图片]"
                    } else {
                        tvContent.text = "[视频]"
                    }
                } else {
                    tvContent.text = it.answerContent
                }

                bottomView.tvPraiseAmount.text = "${it.giveCount} 赞"
                bottomView.tvCommentAmount.text = "${it.commentCount} 评论"
                bottomView.tvDate.text = DateFormatUtil.getStringByFormat(
                    item.add_time * 1000L,
                    DateFormatUtil.datePointFormatYMD
                )

                root.setOnClickListener {
                    ARouter.getInstance().build("/question/activity/answer_detail")
                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                        .withInt(TagConstant.ANSWER_ID, item.answer.answerId)
                        .navigation()
                }
            }

        }
    }
}