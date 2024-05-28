package com.jmbon.middleware.comment.adapter

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.ktx.*
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.comment.DiffUtilCallBack
import com.jmbon.middleware.comment.bean.CommentList
import com.jmbon.middleware.comment.event.SubCommentEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.ItemCommentsLayoutBinding
import com.jmbon.middleware.globalRequest.GlobalRequest
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.Action
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
class ArticleCommentAdapter(
    val type: String,
    val openMore: ((Int) -> Unit)? = null,
    private val itemReplyClick: ((Int, RecyclerView, View, SubCommentEvent) -> Unit)? = null
) :
    BaseCommentAdapter<CommentList.Comment, ItemCommentsLayoutBinding>() {
    init {
        addChildClickViewIds(
            R.id.ll_more,
            R.id.text_send_like,
            R.id.cl_click_layout,
            R.id.text_user_name,
            R.id.image_user
        )
    }

    var itemClick: ((Int, Int) -> Unit)? = null
    var isSupportLongClick: Boolean = true

    private var askAnswerId = -1

    /**
     * 设置一个回答 ID 评论的回答ID,如果评论的是问题或文章，则该参数必须传0  评论谁传谁
     */
    fun setAskAnswerId(answerId: Int) {
        askAnswerId = answerId
    }

    var highlighted = -1
    var fullItem = false

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: CommentList.Comment) {
        holder.getViewBinding<ItemCommentsLayoutBinding>().apply {
            if (highlighted == holder.adapterPosition) {
                highlighted = -1
                val targetView = if (fullItem) root else clMainReply
                targetView.setBackgroundResource(R.color.color_currency_3)
                val colorAnim = ObjectAnimator.ofInt(
                    targetView, "backgroundColor",
                    ColorUtils.getColor(R.color.color_currency_3),
                    Color.TRANSPARENT,
                ).setDuration(1000)
                colorAnim.setEvaluator(ArgbEvaluator())
                targetView.postDelayed({
                    colorAnim.start()
                }, 1000)
            }

            //
            textUserName.setUserNickColor(
                R.color.color_7F7F7F,
                if (item.user.userName.isNullOrEmpty()) item.owner.isCancel else item.user.isCancel
            )
            textUserName.text =
                if (item.user.userName.isNullOrEmpty()) item.owner.userName else item.user.userName
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

            imageUser.loadCircle(if (item.user.avatarFile.isNullOrEmpty()) item.owner.avatarFile else item.user.avatarFile)

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
            textCommentsContent.text = (item.answerContent ?: "")
            textTime.text =
                if (item.addTime == 0L) "刚刚" else DateFormatUtil.getFriendlyTimeSpanByNow2(item.addTime * 1000)
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
                if (item.giveCount > 1 && it.count == 0) {
                    return@filterQuickCall
                }
                item.giveCount = it.count
                setLikeBtn(item)
            })

            progressDot.visibility = View.GONE
            dotProgress.stopAnimation()
            llMore.setOnClickListener {
                progressDot.visibility = View.VISIBLE
                dotProgress.startAnimation()
                llMore.visibility = View.GONE
                openMore?.let {
                    it(holder.adapterPosition)
                }
            }

            if (Constant.TYPE_ARTICLE_MAIN == type || Constant.TYPE_QUESTION_MAIN == type) {
                subCommentLayout.visibility = View.GONE
                textReply.visibility = View.GONE
            } else {
                textReply.visibility = View.VISIBLE

                if (item.secondAnswerCount > 0) {
                    val adapter =
                        ArticleSubCommentAdapter(
                            item.answerId,
                            holder.adapterPosition,
                            type, itemReplyClick
                        )
                    adapter.setOnItemChildClickListener { adapter, view, position ->
                        itemClick?.let { it(holder.adapterPosition, position) }
                    }
                    subCommentContent.adapter = adapter

                    subCommentContent.layoutManager = LinearLayoutManager(subCommentContent.context)
                    adapter.setDiffCallback(DiffUtilCallBack.replyCallBack())

                    if (item.secondAnswers.size > 2 && item.secondAnswerPage == 1) {
                        adapter.setDiffNewData(
                            item.secondAnswers.take(2).toMutableList()
                        )
                    } else {
                        adapter.setDiffNewData(item.secondAnswers)
                    }

                    //  adapter.setDiffNewData(item.secondAnswers)
                    subCommentLayout.visibility = View.VISIBLE
                    if (item.secondAnswerPage > 1) {
                        if (item.secondAnswerFinish) {
                            llMore.visibility = View.GONE
                        } else {
                            textOpenMore.text = "展开更多回复"
                            llMore.visibility = View.VISIBLE
                        }
                    } else {
                        if (item.secondAnswerCount > 2) {
                            textOpenMore.text = "展开${item.secondAnswerCount - adapter.data.size}条回复"
                            llMore.visibility = View.VISIBLE
                        } else {
                            llMore.visibility = View.GONE
                        }
                    }
                } else {
                    subCommentLayout.visibility = View.GONE
                    subCommentContent.removeAllViews()
                }
            }

            commentLongClickReport(item)
        }
    }

    private fun ItemCommentsLayoutBinding.setLikeBtn(item: CommentList.Comment) {
        textSendLike.text = "${0.coerceAtLeast(item.giveCount).coverToTenThousand()}"
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
    private fun ItemCommentsLayoutBinding.commentLongClickReport(item: CommentList.Comment) {
        if (!isSupportLongClick) {
            return
        }

        clClickLayout.setOnLongClickListener {
            if (item.isPreview) {
                return@setOnLongClickListener false
            }
            showCallDialog(item)
            return@setOnLongClickListener true

        }

    }

}