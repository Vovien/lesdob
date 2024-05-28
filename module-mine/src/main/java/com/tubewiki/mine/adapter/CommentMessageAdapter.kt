package com.tubewiki.mine.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.CommentMessageData
import com.tubewiki.mine.databinding.ItemCommentMessageLayoutBinding
import com.jmbon.widget.VerticalImageSpan

class CommentMessageAdapter :
    BindingQuickAdapter<CommentMessageData.Comment, ItemCommentMessageLayoutBinding>() {
    var isAllRead = false

    init {
        addChildClickViewIds(R.id.iv_avatar, R.id.tv_name)
    }

    override fun convert(holder: BaseBindingHolder, item: CommentMessageData.Comment) {
        holder.getViewBinding<ItemCommentMessageLayoutBinding>().apply {
            ivAvatar.loadCircle(item.user.avatarFile)
            tvDate.text = DateFormatUtil.getStringByFormat(
                item.addTime * 1000L,
                DateFormatUtil.dateFormatYMD
            )

            tvName.text = item.user.userName
            tvName.setUserNickColor(R.color.color_262626, item.user.isCancel)
            viewPoint.visibility = if (!item.isRead && !isAllRead) View.VISIBLE else View.INVISIBLE
            tvRewardWord.text = item.answerContent


            if (item.data.comment != null && item.data.comment.answerId != 0) {
                //评论二级评论
                llTitle.visibility = View.GONE
                llTitleSecond.visibility = View.VISIBLE
                viewLine1.visibility = View.GONE
                viewLine2.visibility = View.VISIBLE
                groupVideo.gone()
                groupVideo2.gone()

                val commentSpannableStr =
                    SpannableStringBuilder("你的评论: ${item.data.comment.answerContent}")
                commentSpannableStr.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.color_7F7F7F)),
                    0,
                    5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvSelfComment.text = commentSpannableStr

                initTitle(item, true)

                tvReward.text = "回复了你的评论"

            } else {
                //评论非二级评论
                llTitle.visibility = View.VISIBLE
                llTitleSecond.visibility = View.GONE
                viewLine1.visibility = View.VISIBLE
                viewLine2.visibility = View.GONE
                groupVideo.gone()
                groupVideo2.gone()

                initTitle(item, false)
                if (item.type == 1) {
                    tvReward.text = "评论了你的文章"
                } else if (item.type == 2) {
                    tvReward.text = "评论了你的回答"
                } else if (item.type == 3) {
                    tvReward.text = "评论了你的视频"
                }
            }

        }
    }

    private fun ItemCommentMessageLayoutBinding.initTitle(
        item: CommentMessageData.Comment,
        isSecond: Boolean
    ) {
        var bitmap: Bitmap? = null
        var spannableStringBuilder: SpannableStringBuilder? = null

        if (item.type == 1) {
            //文章
            if (isSecond) {
                textDescription2.gone()
                textDescription.visible()
            } else {
                textDescription.gone()
                textDescription2.visible()
            }
            bitmap =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_message_article)
            spannableStringBuilder = SpannableStringBuilder("  ${item.data.title}")
        } else if (item.type == 3) {
            //视频
            if (isSecond) {
                textDescription2.gone()
                textDescription.gone()
                groupVideo2.visible()
                groupVideo.gone()

                ivVideo2.loadRadius(item.data.cover, 8f.dp())
            } else {
                textDescription.gone()
                textDescription2.gone()
                groupVideo.visible()
                groupVideo2.gone()
                ivVideo.loadRadius(item.data.cover, 8f.dp())
            }

            spannableStringBuilder = SpannableStringBuilder("${item.data.title}")
        } else if (item.type == 2) {
            //问题
            if (isSecond) {
                textDescription.gone()
                textDescription2.visible()
            } else {
                textDescription.visible()
                textDescription2.gone()
            }
            bitmap =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_message_question)
            spannableStringBuilder =
                SpannableStringBuilder("  ${item.data.questionContent}")
            textDescription.text =
                "${item.data.answer.user.userName}: ${item.data.answer.answerContent}"
            textDescription2.text =
                "${item.data.answer.user.userName}: ${item.data.answer.answerContent}"
        }
        if (bitmap != null) {
            val span = VerticalImageSpan(context, bitmap)
            spannableStringBuilder?.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        if (isSecond) {
            textTitle2.text = spannableStringBuilder
        } else {
            textTitle.text = spannableStringBuilder
        }
    }
}