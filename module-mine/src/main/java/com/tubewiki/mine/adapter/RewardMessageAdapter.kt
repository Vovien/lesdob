package com.tubewiki.mine.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setUserNickColor
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.RewardMessageData
import com.tubewiki.mine.databinding.ItemRewardMessageLayoutBinding
import com.jmbon.widget.VerticalImageSpan

class RewardMessageAdapter :
    BindingQuickAdapter<RewardMessageData.Reward, ItemRewardMessageLayoutBinding>() {

    var isAllRead = false

    init {
        addChildClickViewIds(R.id.tv_wallet, R.id.iv_avatar, R.id.tv_name)

    }

    override fun convert(holder: BaseBindingHolder, item: RewardMessageData.Reward) {
        holder.getViewBinding<ItemRewardMessageLayoutBinding>().apply {
            var bitmap: Bitmap? = null
            var spannableStringBuilder: SpannableStringBuilder? = null


            if (item.cateType == 1) {
                //文章
                textDescription.visibility = View.GONE
                bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.ic_message_article)
                spannableStringBuilder = SpannableStringBuilder("  ${item.data.title}")

                tvReward.text = "打赏了你的文章"
            } else if (item.cateType == 3) {
                //回答
                textDescription.visibility = View.VISIBLE
                bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.ic_message_question)
                spannableStringBuilder = SpannableStringBuilder("  ${item.data.questionContent}")
                textDescription.text = item.data.answerContent
                textDescription.text = "${item.data.user.userName}: ${item.data.answerContent}"
                tvReward.text = "打赏了你的回答"
            }
            val span = VerticalImageSpan(context, bitmap)
            spannableStringBuilder?.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            textTitle.text = spannableStringBuilder

            tvDate.text = DateFormatUtil.getStringByFormat(
                item.addTime * 1000L,
                DateFormatUtil.dateFormatYMD
            )

            tvName.text = item.user.userName
            tvName.setUserNickColor(R.color.color_262626, item.user.isCancel)
            viewPoint.visibility = if (!item.isRead && !isAllRead) View.VISIBLE else View.INVISIBLE

            ivAvatar.loadCircle(item.user.avatarFile)

            //设置金额
            val amountSpannableStr =
                SpannableStringBuilder("${item.money}元")
            amountSpannableStr.setSpan(
                RelativeSizeSpan(14f / 20),
                amountSpannableStr.length - 1,
                amountSpannableStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvAmount.text = amountSpannableStr

            //设置寄语
            if (TextUtils.isEmpty(item.description)) {
                tvRewardWord.visibility = View.GONE
            } else {
                tvRewardWord.visibility = View.VISIBLE
                val rewardSpannableStr =
                    SpannableStringBuilder("寄语: ${item.description}")
                rewardSpannableStr.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.color_7F7F7F)),
                    0,
                    3,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvRewardWord.text = rewardSpannableStr
            }
        }
    }
}