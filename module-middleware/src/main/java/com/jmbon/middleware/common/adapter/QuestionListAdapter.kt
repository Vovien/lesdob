package com.jmbon.middleware.common.adapter

import android.annotation.SuppressLint
import android.view.View
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.databinding.ItemQuestionListBinding
import com.jmbon.middleware.utils.*


/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 关注问题空页面推荐和热门适配器
 * version: 1.0
 */
class QuestionListAdapter :
    BindingQuickAdapter<Question, ItemQuestionListBinding>() {


    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: Question) {
        holder.getViewBinding<ItemQuestionListBinding>().apply {

            if (item.type == 2) {
                clHot.visible()
                clNormal.gone()

                tvTitle2.text =
                    if (item.customTitle.isNullOrEmpty()) item.questionContent else item.customTitle
                tvDesc.text = "${item.questionDetail}"
                tvAnswerNum.text = "${item.answerCount}"
                tvBrowsingNum.text = "${item.views} 人正在看"
            } else {
                clNormal.visible()
                clHot.gone()
                tvTitle.text =
                    if (item.customTitle.isNullOrEmpty()) item.questionContent.setKeyHighLight(item.highlight) else item.customTitle
                item.answer.let {
                    civHead.loadCircle(it.user.avatarFile)
                    tvUserName.text = it.user.userName
                    tvUserName.setUserNickColor(R.color.color_262626, it.user.isCancel)
                    tvContent.text = it.answerContent

                    if (it.user.categoryTitle.isNullOrEmpty() && it.user.jobName.isNullOrEmpty()) {
                        tvUserTag.visibility = View.GONE
                        viewVer.visibility = View.GONE
                    } else {
                        tvUserTag.text =
                            if (it.user.categoryTitle.isNullOrEmpty()) it.user.jobName else it.user.categoryTitle
                        tvUserTag.visibility = View.VISIBLE
                        viewVer.visibility = View.VISIBLE
                    }

                    bottomView.tvPraiseAmount.text = "${it.giveCount} 赞"
                    bottomView.tvCommentAmount.text = "${it.commentCount} 评论"
                    bottomView.tvDate.text = "${item.views} 浏览"

                    //展示答案图片或者视频
                    if (it.videos.isNotNullEmpty() || it.images.isNotNullEmpty()) {
                        tvContent.minLines = 3
                        ivCover.visibility = View.VISIBLE
                        if (it.videos.isNotNullEmpty()) {
                            ivCover.loadRadius(
                                it.videos[0],
                                8f.dp(),
                                R.drawable.icon_question_answer_placeholder
                            )
                            ivVideo.visibility = View.VISIBLE

                            if (it.answerContent.replace("\u200c", "").isBlank()) {
                                tvContent.text = "[视频]"
                            }
                        } else {
                            GlideUtil.getInstance().loadUrlRadiusHD(
                                ivCover, it.images[0],
                                8f.dp(),
                                R.drawable.icon_question_answer_placeholder
                            )
                            ivVideo.visibility = View.GONE
                            if (it.answerContent.replace("\u200c", "").isBlank()) {
                                tvContent.text = "[图片]"
                            }
                        }
                    } else {
                        ivCover.visibility = View.GONE
                        ivVideo.visibility = View.GONE
                        tvContent.minLines = 0
                    }
                }
            }
        }
    }
}