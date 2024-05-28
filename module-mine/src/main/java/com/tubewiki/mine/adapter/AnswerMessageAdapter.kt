package com.tubewiki.mine.adapter

import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setUserNickColor
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.AnswerMessageData
import com.tubewiki.mine.databinding.ItemAnswerMessageLayoutBinding
import com.jmbon.widget.VerticalImageSpan

class AnswerMessageAdapter :
    BindingQuickAdapter<AnswerMessageData.Answer, ItemAnswerMessageLayoutBinding>() {
    var isAllRead = false

    init {
        addChildClickViewIds(R.id.iv_avatar)
        addChildClickViewIds(R.id.tv_name)
        addChildClickViewIds(R.id.tv_answer)
        addChildClickViewIds(R.id.ll_title)

    }


    override fun convert(holder: BaseBindingHolder, item: AnswerMessageData.Answer) {

        holder.getViewBinding<ItemAnswerMessageLayoutBinding>().apply {
            ivAvatar.loadCircle(item.user.avatarFile)
            tvDate.text = DateFormatUtil.getStringByFormat(
                item.addTime * 1000L,
                DateFormatUtil.dateFormatYMD
            )


            tvName.text = item.user.userName
            tvName.setUserNickColor(R.color.color_262626, item.user.isCancel)
            viewPoint.visibility = if (!item.isRead && !isAllRead) View.VISIBLE else View.INVISIBLE
            tvAnswer.text = item.answserContent

            var bitmap =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_message_question)
            var spannableStringBuilder =
                SpannableStringBuilder("  ${item.questionContent}")
            val span = VerticalImageSpan(context, bitmap)
            spannableStringBuilder.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            textTitle.text = spannableStringBuilder
        }
    }
}