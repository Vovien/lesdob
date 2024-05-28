package com.tubewiki.mine.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.AnswerBean
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ItemMineCollectionAnswerBinding


/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 关注问题空页面推荐和热门适配器
 * version: 1.0
 */
class MineCollectionAnswerAdapter :
    BindingQuickAdapter<Question, ItemMineCollectionAnswerBinding>() {


    var highlight = ""

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: Question) {
        holder.getViewBinding<ItemMineCollectionAnswerBinding>().apply {

            tvTitle.text = item.questionContent.setKeyHighLight(highlight)
            item.answer.let {
                civHead.loadCircle(it.user.avatarFile)
                tvUserName.text = it.user.userName
                tvUserName.setUserNickColor(R.color.color_7F7F7F,it.user.isCancel)

                if (it.answerContent.isNullOrEmpty() || it.answerContent.replace("\u200c", "")
                        .isBlank()
                ) {
                    tvContent.text =
                        if (it.answerContentHtml.isNotNullEmpty() && it.answerContentHtml.contains("mp4")) "[视频]" else "[图片]"
                } else {
                    tvContent.text = it.answerContent
                }


                if (TextUtils.isEmpty(it.category_title)) {
                    tvUserTag.visibility = View.GONE
                    viewVer.visibility = View.GONE
                } else {
                    tvUserTag.text = it.category_title
                    tvUserTag.visibility = View.VISIBLE
                    viewVer.visibility = View.VISIBLE
                }
                bottomView.tvPraiseAmount.text = "${it.giveCount} 赞"
                bottomView.tvCommentAmount.text = "${it.commentCount} 评论"
                bottomView.tvDate.text = DateFormatUtil.getFriendlyTimeSpanByNow(
                    it.addTime * 1000
                )

                //展示答案图片或者视频
                if (item.answer != null && it.resources.isNotNullEmpty()) {
                    ivCover.visibility = View.VISIBLE
                    ivCover.loadRadius(it.resources[0].src, 8f.dp())
                    if ("image" == it.resources[0].type) {
                        ivVideo.visibility = View.GONE
                    } else {
                        ivVideo.visibility = View.VISIBLE
                    }
                } else {
                    ivCover.visibility = View.GONE
                    ivVideo.visibility = View.GONE
                }
            }
            root.setOnClickListener {

//                ARouter.getInstance().build("/question/activity/answer_detail")
//                    .withInt(TagConstant.QUESTION_ID, item.questionId)
//                    .withInt(TagConstant.ANSWER_ID, item.answer!!.answerId)
//                    .navigation()

                var answer = AnswerBean(
                    answerId = item.answer.answerId,
                    answerContent = item.answer.answerContentHtml,
                    user = item.answer.user,
                    circleId = item.answer.circleId,
                    circle = item.answer.circle
                )
                ARouter.getInstance().build("/question/activity/answer_detail")
                    .withSerializable(TagConstant.QUESTION_DATA, item)
                    .withSerializable(TagConstant.ANSWER_DATA, answer)
                    .navigation()

            }
        }
    }
}