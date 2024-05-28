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
import com.tubewiki.mine.bean.MutuallyMessageData
import com.tubewiki.mine.databinding.ItemCommentMessageLayoutBinding
import com.jmbon.widget.VerticalImageSpan

class MutuallyMessageAdapter :
    BindingQuickAdapter<MutuallyMessageData.Message, ItemCommentMessageLayoutBinding>() {
    var isAllRead = false

    init {
        addChildClickViewIds(R.id.iv_avatar, R.id.tv_name)
    }

    override fun convert(holder: BaseBindingHolder, item: MutuallyMessageData.Message) {
        holder.getViewBinding<ItemCommentMessageLayoutBinding>().apply {

            ivAvatar.loadCircle(item.user.avatarFile)
            tvDate.text = DateFormatUtil.getStringByFormat(
                item.addTime * 1000L,
                DateFormatUtil.dateFormatYMD
            )

            tvName.text = item.user.userName
            tvName.setUserNickColor(R.color.color_262626, item.user.isCancel)
            viewPoint.visibility = if (!item.isRead && !isAllRead) View.VISIBLE else View.INVISIBLE
            tvRewardWord.visibility = View.GONE

            if ((item.data.comment != null && item.data.comment?.answerId != 0) ||
                (item.data.answer.type == 3 && item.dataType == "answer")
            ) {
                //评论二级评论

                llTitle.visibility = View.GONE
                llTitleSecond.visibility = View.VISIBLE
                viewLine1.visibility = View.GONE
                viewLine2.visibility = View.VISIBLE
                val content =
                    if (item.data.comment != null) item.data.comment?.answerContent
                        ?: "" else item.data.answer.answerContent ?: ""
                val commentSpannableStr =
                    SpannableStringBuilder(
                        "你的评论: ${
                            content.replace(
                                "&nbsp;",
                                " "
                            )
                        }"
                    )
                commentSpannableStr.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.color_7F7F7F)),
                    0,
                    5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvSelfComment.text = commentSpannableStr

                initTitle(item, true)

                tvReward.text = if (item.type == 1) "点赞了你的评论" else "收藏了你的评论"

            } else {
                //评论非二级评论
                llTitle.visibility = View.VISIBLE
                llTitleSecond.visibility = View.GONE
                viewLine1.visibility = View.VISIBLE
                viewLine2.visibility = View.GONE

                initTitle(item, false)
//                if (item.data.questionId != 0) {
//                    tvReward.text = if (item.type == 1) "点赞了你的回答" else "收藏了你的回答"
//                } else {
//                    tvReward.text = if (item.type == 1) "点赞了你的文章" else "收藏了你的文章"
//                }

                when (item.dataType) {
                    "answer" -> tvReward.text = if (item.type == 1) "点赞了你的回答" else "收藏了你的回答"
                    "article" -> tvReward.text = if (item.type == 1) "点赞了你的文章" else "收藏了你的文章"
                    "video" -> tvReward.text = if (item.type == 1) "点赞了你的视频" else "收藏了你的视频"
                    else -> tvReward.text = if (item.type == 1) "点赞了你的回答" else "收藏了你的回答"
                }
            }

        }
    }

    private fun ItemCommentMessageLayoutBinding.initTitle(
        item: MutuallyMessageData.Message,
        isSecond: Boolean
    ) {
        var bitmap: Bitmap? = null
        var spannableStringBuilder: SpannableStringBuilder? = null
        groupVideo2.gone()
        groupVideo.gone()

        when (item.dataType) {
            "answer" -> {
                //1 文章 2问题 3 视频
                if (item.data.answer.type == 3) {
                    if (isSecond) {
                        textDescription2.visibility = View.GONE
                        textDescription.visibility = View.GONE
                        //  textTitle2.text = item.data.title
                        groupVideo2.visible()
                        ivVideo2.loadRadius(item.data.cover, 8f.dp())

                    } else {
                        textDescription.visibility = View.GONE
                        textDescription.visibility = View.GONE
                        // textTitle.text = item.data.title
                        groupVideo.visible()
                        ivVideo.loadRadius(item.data.cover, 8f.dp())
                    }
                    spannableStringBuilder =
                        SpannableStringBuilder("${item.data.title}")
                } else if (item.data.answer.type == 2) {
                    bitmap =
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.ic_message_question
                        )
                    spannableStringBuilder =
                        SpannableStringBuilder("  ${item.data.questionContent}")
                    //问题
                    if (isSecond) {
                        textDescription2.visibility = View.VISIBLE
                        textDescription2.text =
                            "${item.data.answer.user.userName}: ${
                                item.data.answer.answerContent.replace(
                                    "&nbsp;",
                                    " "
                                )
                            }"
                    } else {
                        textDescription.visibility = View.VISIBLE
                        textDescription.text =
                            "${item.data.answer.user.userName}: ${
                                item.data.answer.answerContent.replace(
                                    "&nbsp;",
                                    " "
                                )
                            }"
                    }

                } else {
                    //文章
                    if (isSecond) {
                        textDescription2.visibility = View.GONE
                    } else {
                        textDescription.visibility = View.GONE
                    }
                    bitmap =
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.ic_message_article
                        )
                    spannableStringBuilder = SpannableStringBuilder("  ${item.data.title}")
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
            "article" -> {
                //文章
                if (isSecond) {
                    textDescription2.visibility = View.GONE
                } else {
                    textDescription.visibility = View.GONE
                }
                bitmap =
                    BitmapFactory.decodeResource(context.resources, R.drawable.ic_message_article)
                spannableStringBuilder = SpannableStringBuilder("  ${item.data.title}")
                val span = VerticalImageSpan(context, bitmap)
                spannableStringBuilder.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                if (isSecond) {
                    textTitle2.text = spannableStringBuilder
                } else {
                    textTitle.text = spannableStringBuilder
                }
            }
            "video" -> {
                if (isSecond) {
                    textDescription2.visibility = View.GONE
                    textDescription.visibility = View.GONE
                    textTitle2.text = item.data.title
                    groupVideo2.visible()
                    ivVideo2.loadRadius(item.data.cover, 8f.dp())

                } else {
                    textDescription.visibility = View.GONE
                    textDescription.visibility = View.GONE
                    textTitle.text = item.data.title
                    groupVideo.visible()
                    ivVideo.loadRadius(item.data.cover, 8f.dp())
                }


            }
        }


    }
}