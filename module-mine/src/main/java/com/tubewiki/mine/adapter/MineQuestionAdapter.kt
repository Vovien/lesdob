package com.tubewiki.mine.adapter

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.databinding.ItemMineQuestionsBinding
import com.jmbon.widget.TagSpan

/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 我的提问adapter
 * version: 1.0
 */
class MineQuestionAdapter :
    BindingQuickAdapter<Question, ItemMineQuestionsBinding>() {


    override fun convert(holder: BaseBindingHolder, item: Question) {
        holder.getViewBinding<ItemMineQuestionsBinding>().apply {


            if (item.isReward > 0) {

                val tagStr = "悬赏¥${item.reward_money}"
                val titleStr = tagStr + item.questionContent
                var spannableStr: SpannableStringBuilder? = null
                spannableStr = SpannableStringBuilder(titleStr)

                // 设置 tag span
                spannableStr.setSpan(
                    TagSpan(tagStr, context),
                    titleStr.indexOf(tagStr),
                    titleStr.indexOf(tagStr) + tagStr.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
                tvTitle.text = spannableStr

            } else {
                llReward.visibility = View.GONE
                tvTitle.text = item.questionContent
            }



            tvFollower.text = "${item.focusCount} 关注"
            tvAnswer.text = "${item.answerCount}回答"

            tvDate.text = DateFormatUtil.getStringByFormat(
                item.add_time * 1000L,
                DateFormatUtil.datePointFormatYMD
            )

            root.setOnClickListener {
                ARouter.getInstance().build("/question/activity/ask_detail")
                    .withInt(TagConstant.QUESTION_ID, item.questionId)
                    .withBoolean(TagConstant.FROM_MINE_PERSON_PAGE, true)
                    .navigation()
            }
        }

    }
}
