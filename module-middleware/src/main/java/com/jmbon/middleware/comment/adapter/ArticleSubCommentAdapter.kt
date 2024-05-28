package com.jmbon.middleware.comment.adapter

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.ktx.*
import com.jmbon.middleware.R
import com.jmbon.middleware.comment.bean.CommentList
import com.jmbon.middleware.comment.event.SubCommentEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.ItemCommentsLayoutBinding
import com.jmbon.middleware.databinding.ItemSubCommentsLayoutBinding
import com.jmbon.middleware.globalRequest.GlobalRequest
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.Action
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
class ArticleSubCommentAdapter(
    val askAnswerId: Int,
    val topPosition: Int,
    val type: String,
    private val itemReplyClick: ((Int, RecyclerView, View, SubCommentEvent) -> Unit)?
) :
    BaseCommentAdapter<CommentList.Comment, ItemSubCommentsLayoutBinding>() {
    init {
        addChildClickViewIds(R.id.text_send_like)
    }


    override fun convert(holder: BaseBindingHolder, item: CommentList.Comment) {
        holder.getViewBinding<ItemSubCommentsLayoutBinding>().apply {
            imageUser.loadCircle(item.owner.avatarFile)
            textTime.text =
                if (item.addTime == 0L) "刚刚" else DateFormatUtil.getFriendlyTimeSpanByNow2(item.addTime * 1000)

            //textCommentsContent.text = (item.answerContent ?: "") + "||" + item.answerId
            textSubCommentsContent.text = (item.answerContent ?: "")
            setLikeBtn(item)
            textSendLike.filterQuickCall({
                Action {
                    item.isGiven = !item.isGiven
                    item.giveCount = if (item.isGiven) item.giveCount + 1 else item.giveCount - 1
                    setLikeBtn(item)
                }.logInToIntercept()
            }, {
                GlobalRequest.answerGivenCollect(
                    item.answerId,
                    GlobalRequest.TYPE_LIKE,
                    item.isGiven
                )
            }, {
                item.giveCount = it.count
                setLikeBtn(item)
            })

            textUserName.setUserNickColor(R.color.color_7F7F7F, item.owner.isCancel)
            textUserName.text = item.owner.userName ?: ""
            // 根据 answerID 来判断，
            // 先与一级 ID 比较 一致 则不显示
            if (askAnswerId != item.replyId) {
                textReplyName.setUserNickColor(R.color.color_7F7F7F, item.receiver?.isCancel ?: 0)
                textReplyName.text = item.receiver?.userName ?: ""
                textReplyName.visibility = View.VISIBLE
            } else textReplyName.visibility = View.GONE

            if (textReplyName.isGone) {
                if (type.startsWith(Constant.TYPE_ARTICLE) || type.startsWith(Constant.TYPE_VIDEO)) {
                    if (item.uid == item.articlePublishedUid || item.uid == item.videoPublishedUid || item.uid == item.articlePublishedUid) {
                        textUserLabel.visible()
                        // 文章作者
                        textUserLabel.setLabel(3)
                    } else textUserLabel.gone()
                } else {
                    if (item.uid == item.questionPublishedUid || item.uid == item.answerPublishedUid) {
                        textUserLabel.visible()
                        if (item.uid == item.questionPublishedUid && item.uid == item.answerPublishedUid)
                            textUserLabel.setLabel(1)
                        else {
                            // 提问者
                            if (item.uid == item.questionPublishedUid)
                                textUserLabel.setLabel(2)
                            // 回答者
                            if (item.uid == item.answerPublishedUid)
                                textUserLabel.setLabel(1)
                        }
                    } else textUserLabel.gone()
                }
            } else textUserLabel.gone()

            // image
            imageUser.setOnSingleClickListener({
                if (item.user.isCancel == 1) {
                    R.string.user_has_log_off.showToast()
                    return@setOnSingleClickListener
                }
                if (item.isPreview)
                    return@setOnSingleClickListener
                item.uid.toUserCenter()
            })
            textUserName.setOnSingleClickListener({
                if (item.user.isCancel == 1) {
                    R.string.user_has_log_off.showToast()
                    return@setOnSingleClickListener
                }
                if (item.isPreview)
                    return@setOnSingleClickListener
                item.uid.toUserCenter()
            })

            replySubContent.setOnClickListener {
                if (item.isPreview)
                    return@setOnClickListener
                itemReplyClick?.let {
                    it(
                        holder.adapterPosition, recyclerView, root,
                        SubCommentEvent(
                            item.answerId,
                            topPosition,
                            item.topAnswerId,
                            item.secondAnswerId,
                            item.owner,
                            item.owner.userName ?: "",
                            type, item
                        )
                    )
                }
            }

            commentLongClickReport(item)
        }
    }

    private fun ItemSubCommentsLayoutBinding.setLikeBtn(item: CommentList.Comment) {
        textSendLike.text = 0.coerceAtLeast(item.giveCount).coverToTenThousand()
        textSendLike.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            if (item.isGiven) R.drawable.icon_comment_likeed else R.drawable.icon_comment_like,
            0,
            0
        )
        textSendLike.setTextColor(context.resources.getColor(if (item.isGiven) R.color.color_currency else R.color.color_BFBFBF))
    }

    /**
     * 长按评论举报
     */
    private fun ItemSubCommentsLayoutBinding.commentLongClickReport(item: CommentList.Comment) {
        replySubContent.setOnLongClickListener {
            if (item.isPreview) {
                return@setOnLongClickListener false
            }

            showCallDialog(item)
            return@setOnLongClickListener true

        }

    }
}