package com.jmbon.middleware.common.adapter

import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.toDotYMD
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.toQuestionDetails
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.ItemFollowQuestionHotBinding
import com.jmbon.middleware.utils.*


/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 关注问题空页面推荐和热门适配器
 * version: 1.0
 */
class FollowQuestionRecommendAdapter(
    val isSearch: Boolean = false,
    val showType: Int = 0,
    val searchType: Int = 0, //1是banner搜索，默认0
    val isUseMinePage: Boolean = false,
    val isShareCircle: Boolean = false
) :
    BindingQuickAdapter<Question, ItemFollowQuestionHotBinding>() {

    var keyWords: String = ""


    override fun convert(holder: BaseBindingHolder, item: Question) {
        holder.getViewBinding<ItemFollowQuestionHotBinding>().apply {
            //动态设置id.否则AndResGuard资源混淆后id找不到
            answerContent.referencedIds = intArrayOf(R.id.ll_data, R.id.tv_content)
            emptyContent.referencedIds = intArrayOf(R.id.tv_answer, R.id.tv_write)
            //调整第一个距离
            if (holder.adapterPosition == 0) {
                root.setPadding(0, 0, 0, 0)
            } else {
                root.setPadding(0, SizeUtils.dp2px(16f), 0, 0)
            }


            if (keyWords.isNullOrEmpty()) {
                tvTitle.text = item.questionContent.setKeyHighLight(item.highlight)
            } else {
                tvTitle.text = item.questionContent.setKeyHighLight(keyWords)
            }
            //后台返回有回答的问题
            if (item.answer != null) {
                item.answer.let {
                    emptyContent.visibility = View.GONE
                    answerContent.visibility = View.VISIBLE
                    bottomView.clDate.visibility = View.VISIBLE

                    civHead.loadCircle(it.user.avatarFile)
                    tvUserName.text = it.user.userName
                    tvUserName.setUserNickColor(R.color.color_262626,it.user.isCancel)
                    if (it.answerContent.replace("\u200c", "").isBlank()) {
                        //video为空的情况
                        if (it.videos.isNotNullEmpty() || (it.answerContentHtml.isNotNullEmpty() && it.answerContentHtml.contains(
                                ".mp4"
                            ))
                        ) {
                            tvContent.text = "[视频]"
                        } else if (it.images.isNotNullEmpty()) {
                            tvContent.text = "[图片]"
                        } else {
                            tvContent.text = ""
                        }
                    } else {
                        tvContent.text = it.answerContent.replace("&nbsp;"," ").trim()
                    }


                    if (isUseMinePage) {
                        tvUserTag.visibility = View.GONE
                        viewVer.visibility = View.GONE
                        root.setPadding(20f.dp(), 16f.dp(), 20f.dp(), 0)
                    } else {
                        if (TextUtils.isEmpty(it.category_title)) {
                            tvUserTag.visibility = View.GONE
                            viewVer.visibility = View.GONE
                        } else {
                            tvUserTag.text = it.category_title
                            tvUserTag.visibility = View.VISIBLE
                            viewVer.visibility = View.VISIBLE
                        }
                    }
                    bottomView.tvPraiseAmount.text = "${it.giveCount.coverToTenThousand()} 赞"
                    bottomView.tvCommentAmount.text = "${it.commentCount.coverToTenThousand()} 评论"
                    bottomView.tvDate.text = it.addTime.toDotYMD()

                    //展示答案图片或者视频
                    if (item.answer != null && it.resources != null && it.resources.size != 0) {
                        ivCover.visibility = View.VISIBLE
                        ivCover.loadUrlRadius(
                            it.resources[0].src, 8f.dp(),
                            R.drawable.icon_question_answer_placeholder
                        )
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
            } else {
                //后台返回没有回答的问题
                emptyContent.visibility = View.VISIBLE
                bottomView.clDate.visibility = View.GONE
                answerContent.visibility = View.GONE
                ivCover.visibility = View.GONE
                ivVideo.visibility = View.GONE

            }
            root.setOnClickListener {
                if (item.answer != null) {
                    toQuestionDetails(
                        item.questionId,
                        HtmlTools.delHTMLTag(item.questionContent),
                        item.is_reply,
                        item.publishedUid,
                        item.answerCount,
                        item.isDel,
                        item.lock,
                        item.answer,
                        isShareCircle, isUseMinePage
                    )
                } else {
                    ARouter.getInstance().build("/question/activity/ask_detail")
                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                        .withBoolean(TagConstant.FROM_MINE_PERSON_PAGE, isUseMinePage)
                        .navigation()
                }

                if (isSearch) {
                    // 漏斗事件 只要点击即可
                    // banner 搜索点击
                    Constant.isInfoDetails = true
                    if (searchType == 0) {
                        MobAnalysisUtils.mInstance.sendEvent("Event_HomePage_SearchResults_List_Click")
                    } else {
                        MobAnalysisUtils.mInstance.sendEvent("Event_SecondarStatusPage_SearchResults_List_Click")
                    }
                    if (showType == 0) {
                        //首页搜索点击
                        if (searchType == 0) {
                            MobAnalysisUtils.mInstance.sendEvent(
                                "Event_HomePage_SearchResults_QAlist_Click",
                                mutableMapOf(Pair("Sort", "${holder.adapterPosition}"))
                            )
                        } else {
                            //banner搜索点击
                            MobAnalysisUtils.mInstance.sendEvent(
                                "Event_SecondarStatusPage_SearchResults_QAlist_Click",
                                mutableMapOf(Pair("Sort", "${holder.adapterPosition}"))
                            )
                        }
                    } else {
                        //综合点击
                        MobAnalysisUtils.mInstance.sendEvent(
                            "Event_HomePage_SearchResults_RelatedSearch_Click",
                            mutableMapOf(
                                Pair(
                                    "type",
                                    "question"
                                ), Pair(
                                    "itemId",
                                    "${item.questionId}"
                                )
                            )
                        )
                    }
                }


            }
        }
    }


}