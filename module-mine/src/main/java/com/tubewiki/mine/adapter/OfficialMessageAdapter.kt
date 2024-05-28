package com.tubewiki.mine.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.TextHighLight
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.OfficialMessageData
import com.tubewiki.mine.databinding.ItemOfficialMessageLayoutBinding
import com.jmbon.widget.VerticalImageSpan

class OfficialMessageAdapter :
    BindingQuickAdapter<OfficialMessageData.Official, ItemOfficialMessageLayoutBinding>() {
    var isAllRead = false

    init {
        addChildClickViewIds(R.id.tv_wallet)
    }

    override fun convert(holder: BaseBindingHolder, item: OfficialMessageData.Official) {
        holder.getViewBinding<ItemOfficialMessageLayoutBinding>().apply {
            tvRewardWord.visibility = View.GONE
            tvDate.visibility = View.GONE
            tvDate2.visibility = View.GONE
            tvAmount.visibility = View.GONE
            viewLine1.visibility = View.GONE
            viewLine2.visibility = View.GONE
            viewLine3.visibility = View.GONE
            tvWallet.visibility = View.GONE
            llTitle.visibility = View.GONE
            llTitleSecond.visibility = View.GONE
            ivVideo.gone()
            ivVideoPlay.gone()
            groupVideo2.visibility = View.GONE


            //后台没返回user，说明是官方账号
            ivAvatar.loadCircle(item.avatarFile)
            tvName.text = item.username

            tvDate.text = DateFormatUtil.getStringByFormat(
                item.time * 1000L,
                DateFormatUtil.dateFormatYMD
            )

            tvDate2.text = DateFormatUtil.getStringByFormat(
                item.time * 1000L,
                DateFormatUtil.dateFormatYMD
            )


            viewPoint.visibility = if (!item.isRead && !isAllRead) View.VISIBLE else View.INVISIBLE
            when (item.type) {
                //悬赏采纳
                OfficialMessageData.typeRewardBack,
                OfficialMessageData.typeAdoptReward -> {
                    //悬赏金过期退还
                    //问题的悬赏
                    llTitle.visibility = View.VISIBLE
                    tvAmount.visibility = View.VISIBLE
                    viewLine1.visibility = View.VISIBLE
                    tvWallet.visibility = View.VISIBLE
                    tvDate2.visibility = View.VISIBLE
                    //设置金额
                    val amountSpannableStr =
                        SpannableStringBuilder("${item.amount}元")
                    amountSpannableStr.setSpan(
                        RelativeSizeSpan(14f / 20),
                        amountSpannableStr.length - 1,
                        amountSpannableStr.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvAmount.text = amountSpannableStr
                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null

                    if (item.answerId == 0 && item.answerContent.isNullOrEmpty()) {
                        //只有问题，没有回答评论，说明是赏金过期 或者是问题被删除导致赏金退还
                        tvReward.text = TextHighLight.setStringHighLight(
                            item.description,
                            item.descriptionHighlight.toTypedArray(),
                            R.color.color_FF5A5F
                        )
                        textDescription.visibility = View.GONE

                    } else {
                        //有回答有评论说明是回答被采纳
                        tvReward.text = TextHighLight.setStringHighLight(
                            item.description,
                            item.descriptionHighlight.toTypedArray(),
                            R.color.color_FF5A5F
                        )
                        textDescription.visibility = View.VISIBLE
                        textDescription.text =
                            "${item.answerUsername}: ${
                                item.answerContent.replace(
                                    "&nbsp;",
                                    " "
                                )
                            }"
                    }
                    bitmap =
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.ic_message_question
                        )
                    spannableStringBuilder =
                        SpannableStringBuilder("  ${item.title}")
                    val span = VerticalImageSpan(context, bitmap)
                    spannableStringBuilder.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    textTitle.text = spannableStringBuilder

                    root.setOnClickListener {
                        //过期退还进入详情页，如果是问题被删除退还就进入违规详情页
                        if ("reward_back" == item.type && !item.description.contains("删除")) {
                            ARouter.getInstance().build("/question/activity/ask_detail")
                                .withInt(TagConstant.QUESTION_ID, item.questionId)
                                .withInt(TagConstant.EXAMINE_ID, item.examineId)
                                .navigation()
                        } else if ("adopt_reward" == item.type) {
                            ARouter.getInstance().build("/question/activity/answer_detail")
                                .withInt(TagConstant.QUESTION_ID, item.questionId)
                                .withInt(TagConstant.ANSWER_ID, item.answerId)
                                .withInt(TagConstant.EXAMINE_ID, item.examineId)
                                .navigation()
                        } else {
                            ARouter.getInstance().build("/mine/message/violate_question")
                                .withInt(TagConstant.QUESTION_ID, item.questionId)
                                .navigation()
                        }
                    }
                }

                OfficialMessageData.typeArticleOfferReward -> {
                    //文章的打赏
                    llTitle.visibility = View.VISIBLE
                    tvAmount.visibility = View.VISIBLE
                    viewLine1.visibility = View.VISIBLE
                    tvWallet.visibility = View.VISIBLE
                    tvDate2.visibility = View.VISIBLE
                    //设置金额
                    val amountSpannableStr =
                        SpannableStringBuilder("${item.amount}元")
                    amountSpannableStr.setSpan(
                        RelativeSizeSpan(14f / 20),
                        amountSpannableStr.length - 1,
                        amountSpannableStr.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvAmount.text = amountSpannableStr
                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null
                    //没有问题，但是是悬赏类型，说明是文章打赏
                    tvReward.text = TextHighLight.setStringHighLight(
                        item.description,
                        item.descriptionHighlight.toTypedArray(),
                        R.color.color_FF5A5F
                    )
                    bitmap =
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.ic_message_article
                        )
                    spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                    val span = VerticalImageSpan(context, bitmap)
                    spannableStringBuilder.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    textTitle.text = spannableStringBuilder
                    root.setOnClickListener {

                        ArouterUtils.toArticleDetailsActivity(item.id)
                    }
                }
                // answer_offer_reward
                OfficialMessageData.typeAnswerOfferReward -> {
                    //回答的打赏,和采纳悬赏一致
                    llTitle.visibility = View.VISIBLE
                    tvAmount.visibility = View.VISIBLE
                    viewLine1.visibility = View.VISIBLE
                    tvWallet.visibility = View.VISIBLE
                    tvDate2.visibility = View.VISIBLE
                    //设置金额
                    val amountSpannableStr =
                        SpannableStringBuilder("${item.amount}元")
                    amountSpannableStr.setSpan(
                        RelativeSizeSpan(14f / 20),
                        amountSpannableStr.length - 1,
                        amountSpannableStr.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvAmount.text = amountSpannableStr
                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null

                    //有问题，有评论，说明是问题回答被采纳
                    tvReward.text = item.description.setKeyHighLight(item.descriptionHighlight)
                    bitmap =
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.ic_message_question
                        )
                    spannableStringBuilder =
                        SpannableStringBuilder("  ${item.title}")
                    textDescription2.text =
                        "${item.answerUsername}: ${
                            item.answerContent.replace(
                                "&nbsp;",
                                " "
                            )
                        }"

                    val span = VerticalImageSpan(context, bitmap)
                    spannableStringBuilder.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    textTitle.text = spannableStringBuilder
                    root.setOnClickListener {
                        ARouter.getInstance().build("/question/activity/answer_detail")
                            .withInt(TagConstant.QUESTION_ID, item.questionId)
                            .withInt(TagConstant.ANSWER_ID, item.answerId)
                            .withInt(TagConstant.EXAMINE_ID, item.examineId)
                            .navigation()
                    }
                }

                OfficialMessageData.typeDeleteArticle,
                OfficialMessageData.typeDeleteQuestion,
                OfficialMessageData.typeDeleteQuestionAnswer,
                OfficialMessageData.typeDeleteVideo -> {
                    tvDate.visibility = View.VISIBLE

                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null

                    var tvText: TextView? = null
                    //删除操作 - 违规
                    when (item.type) {
                        OfficialMessageData.typeDeleteQuestion -> {
                            //删除违规问题
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder =
                                SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/violate_question")
                                    .withInt(TagConstant.QUESTION_ID, item.questionId)
                                    .navigation()
                            }
                        }
                        OfficialMessageData.typeDeleteArticle -> {
                            //删除违规文章
                            tvDate.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            llTitle.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_article
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/violate_article")
                                    .withInt(TagConstant.ARTICLE_ID, item.id)
                                    .navigation()
                            }
                        }
                        OfficialMessageData.typeDeleteQuestionAnswer -> {
                            //删除违规问题回答
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.VISIBLE
                            tvText = textTitle
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            textDescription.text =
                                "${item.answerUsername}: ${
                                    item.answerContent.replace(
                                        "&nbsp;",
                                        " "
                                    )
                                }"

                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/violate_answer_comment")
                                    .withString(TagConstant.TYPE, "question_answer")
                                    .withString(TagConstant.PARAMS, item.title)
                                    .withInt(TagConstant.QUESTION_ID, item.questionId)
                                    .withInt(TagConstant.ANSWER_ID, item.answerId)
                                    .withInt(TagConstant.QUESTION_CHECK_ID, item.examineId)
                                    .navigation()
                            }
                        }


                        OfficialMessageData.typeDeleteVideo -> {
                            //删除违规视频
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            textTitle.text = item.title
                            ivVideo.visible()
                            ivVideoPlay.visible()
                            ivVideo.loadRadius(item.videoCover.ifEmpty { item.video }, 8f.dp())
                            //动态修改video高度
                            var layoutParams = ivVideo.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = 42f.dp()
                            layoutParams.width = 56f.dp()
                            layoutParams.topMargin = 0
                            ivVideo.layoutParams = layoutParams
                            root.setOnClickListener {
                                ARouter.getInstance()
                                    .build("/mine/message/modify_violate_video")
                                    .withString(TagConstant.PARAMS, item.video)
                                    .withString(TagConstant.PARAMS2, item.videoCover)
                                    .withString(TagConstant.TYPE, "video")
                                    .navigation()
                            }
                        }
                    }

                    bitmap?.let {
                        val span = VerticalImageSpan(context, it)
                        spannableStringBuilder?.setSpan(
                            span,
                            0,
                            2,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                        tvText?.text = spannableStringBuilder
                    }

                }

                OfficialMessageData.typeReportAnswerSuccess,
                OfficialMessageData.typeReportAnswerFail,
                OfficialMessageData.typeReportQuestionFail,
                OfficialMessageData.typeReportQuestionSuccess -> {

                    when (item.type) {
                        //问题的修改
                        OfficialMessageData.typeReportQuestionFail,
                        OfficialMessageData.typeReportQuestionSuccess -> {
                            tvDate.visibility = View.VISIBLE
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            var bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            var spannableStringBuilder =
                                SpannableStringBuilder("  ${item.title}")

                            val span = VerticalImageSpan(context, bitmap)
                            spannableStringBuilder.setSpan(
                                span,
                                0,
                                2,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                            textTitle.text = spannableStringBuilder

                            root.setOnClickListener {
                                if (item.type == OfficialMessageData.typeReportQuestionFail) {
                                    ARouter.getInstance()
                                        .build("/mine/message/check_fail_question")
                                        .withInt(TagConstant.QUESTION_CHECK_ID, item.examineId)
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withString(
                                            TagConstant.TYPE,
                                            item.type
                                        )
                                        .navigation()
                                } else {
                                    ARouter.getInstance().build("/question/activity/ask_detail")
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withInt(TagConstant.EXAMINE_ID, item.examineId)
                                        .navigation()
                                }
                            }

                        }
                        OfficialMessageData.typeReportAnswerSuccess,
                        OfficialMessageData.typeReportAnswerFail -> {
                            //回答修改，目前只有这一个
                            llTitle.visibility = View.VISIBLE
                            viewLine1.visibility = View.VISIBLE
                            tvDate.visibility = View.VISIBLE
                            textDescription.visibility = View.VISIBLE

                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            val bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            val spannableStringBuilder =
                                SpannableStringBuilder("  ${item.title}")
                            textDescription.text =
                                "${item.answerUsername}: ${
                                    item.answerContent.replace(
                                        "&nbsp;",
                                        " "
                                    )
                                }"

                            val span = VerticalImageSpan(context, bitmap)
                            spannableStringBuilder.setSpan(
                                span,
                                0,
                                2,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                            textTitle.text = spannableStringBuilder
                            root.setOnClickListener {
                                if (item.type == OfficialMessageData.typeReportAnswerSuccess) {
                                    ARouter.getInstance()
                                        .build("/question/activity/answer_detail")
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withInt(TagConstant.ANSWER_ID, item.answerId)
                                        .navigation()
                                } else {
                                    //审核不通过就跳转审核结果页
                                    ARouter.getInstance()
                                        .build("/mine/message/chek_fail_answer")
                                        .withString(TagConstant.TYPE, item.type)
                                        .withInt(TagConstant.QUESTION_CHECK_ID, item.examineId)
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withInt(TagConstant.ANSWER_ID, item.answerId)
                                        .navigation()
                                }

                            }

                        }

                    }

                }
                OfficialMessageData.typeTortArticle,
                OfficialMessageData.typeTortAnswer,
                OfficialMessageData.typeTortQuestion,
                OfficialMessageData.typeTortVideo -> {
                    tvDate.visibility = View.VISIBLE

                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null

                    var tvText: TextView? = null
                    //侵权操作 
                    when (item.type) {
                        OfficialMessageData.typeTortQuestion -> {
                            //删除违规问题
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder =
                                SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/delict_report")
                                    .withInt(TagConstant.PARAMS, item.questionId)
                                    .navigation()
                            }
                        }
                        OfficialMessageData.typeTortArticle -> {
                            //删除违规文章
                            tvDate.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            llTitle.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_article
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/delict_report")
                                    .withInt(TagConstant.PARAMS, item.id)
                                    .navigation()
                            }
                        }
                        OfficialMessageData.typeTortVideo -> {
                            //删除侵权视频
                            tvDate.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            llTitle.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            textTitle.text = item.title
                            ivVideo.visible()
                            ivVideoPlay.visible()

                            ivVideo.loadRadius(item.videoCover.ifEmpty { item.video }, 8f.dp())

                            var layoutParams = ivVideo.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = 42f.dp()
                            layoutParams.width = 56f.dp()
                            layoutParams.topMargin = 0
                            ivVideo.layoutParams = layoutParams

                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/delict_report")
                                    .withInt(TagConstant.PARAMS, item.id)
                                    .navigation()
                            }
                        }
                        OfficialMessageData.typeTortAnswer -> {
                            //删除违规问题回答
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.VISIBLE
                            tvText = textTitle
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            textDescription.text =
                                "${item.answerUsername}: ${
                                    item.answerContent.replace(
                                        "&nbsp;",
                                        " "
                                    )
                                }"

                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/delict_report")
                                    .withInt(TagConstant.PARAMS, item.answerId)
                                    .navigation()
                            }
                        }
                    }

                    bitmap?.let {
                        val span = VerticalImageSpan(context, it)
                        spannableStringBuilder?.setSpan(
                            span,
                            0,
                            2,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                        tvText?.text = spannableStringBuilder
                    }
                }
                OfficialMessageData.typeAnswerVideoExamineFail,
                OfficialMessageData.typeAnswerVideoExamineSuccess,
                OfficialMessageData.typeQuestionVideoExamineFail,
                OfficialMessageData.typeQuestionVideoExamineSuccess -> {
                    //审核
                    tvDate.visibility = View.VISIBLE

                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null

                    var tvText: TextView? = null
                    when (item.type) {
                        //
                        OfficialMessageData.typeAnswerVideoExamineFail,
                        OfficialMessageData.typeAnswerVideoExamineSuccess -> {
                            ivVideo.visible()
                            ivVideoPlay.visible()
                            ivVideo.loadRadius(item.videoCover.ifEmpty { item.video }, 8f.dp())

                            var layoutParams = ivVideo.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = 60f.dp()
                            layoutParams.width = 80f.dp()
                            layoutParams.topMargin = 0
                            ivVideo.layoutParams = layoutParams

                            //回答视频审核
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.VISIBLE
                            tvText = textTitle
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            textDescription.text =
                                "${item.answerUsername}: ${
                                    item.answerContent.replace(
                                        "&nbsp;",
                                        " "
                                    )
                                }"

                            root.setOnClickListener {
                                if (item.type == OfficialMessageData.typeAnswerVideoExamineSuccess) {
                                    ARouter.getInstance().build("/question/activity/answer_detail")
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withInt(TagConstant.ANSWER_ID, item.answerId)
                                        .withInt(TagConstant.EXAMINE_ID, item.examineId)
                                        .navigation()
                                } else {

                                    ARouter.getInstance()
                                        .build("/mine/message/modify_violate_answer")
                                        .withString(TagConstant.TYPE, item.type)
                                        .withString(TagConstant.PARAMS, item.title)
                                        .withInt(
                                            TagConstant.ANSWER_ID,
                                            item.answerId
                                        ).withInt(
                                            TagConstant.QUESTION_CHECK_ID,
                                            item.examineId
                                        ).withInt(
                                            TagConstant.QUESTION_ID,
                                            item.questionId
                                        )
                                        .withString(TagConstant.TYPE, "answer_video")
                                        .navigation()

                                }
                            }
                        }
                        OfficialMessageData.typeQuestionVideoExamineFail,
                        OfficialMessageData.typeQuestionVideoExamineSuccess -> {
                            //问题视频审核
                            ivVideo.visible()
                            ivVideoPlay.visible()
                            ivVideo.loadRadius(item.videoCover.ifEmpty { item.video }, 8f.dp())
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE

                            var layoutParams = ivVideo.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = 42f.dp()
                            layoutParams.width = 56f.dp()
                            layoutParams.topMargin = 0
                            ivVideo.layoutParams = layoutParams

                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder =
                                SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                if (item.type == OfficialMessageData.typeQuestionVideoExamineSuccess) {
                                    ARouter.getInstance().build("/question/activity/ask_detail")
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withInt(TagConstant.EXAMINE_ID, item.examineId)
                                        .navigation()
                                } else {
//                                    ARouter.getInstance()
//                                        .build("/mine/message/check_fail_question")
//                                        .withInt(
//                                            TagConstant.QUESTION_ID,
//                                            item.id
//                                        )
//                                        .withString(TagConstant.TYPE, item.type)
//                                        .navigation()

                                    ARouter.getInstance()
                                        .build("/mine/message/modify_violate_question")
                                        .withInt(
                                            TagConstant.QUESTION_ID,
                                            item.questionId
                                        ).withInt(
                                            TagConstant.QUESTION_CHECK_ID,
                                            item.examineId
                                        )
                                        .withString(TagConstant.TYPE, "question_video")
                                        .navigation()
                                }


                            }
                        }
                    }

                    val span = VerticalImageSpan(context, bitmap)
                    spannableStringBuilder?.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    tvText?.text = spannableStringBuilder
                }
                OfficialMessageData.typeAnswerImageExamineFail,
                OfficialMessageData.typeAnswerImageExamineSuccess,
                OfficialMessageData.typeQuestionImageExamineFail,
                OfficialMessageData.typeQuestionImageExamineSuccess -> {
                    //审核
                    tvDate.visibility = View.VISIBLE

                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null

                    var tvText: TextView? = null
                    when (item.type) {
                        //回答图片审核
                        OfficialMessageData.typeAnswerImageExamineFail,
                        OfficialMessageData.typeAnswerImageExamineSuccess -> {
                            ivVideo.visible()
                            ivVideoPlay.gone()
                            ivVideo.loadRadius(item.image, 8f.dp())

                            var layoutParams = ivVideo.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = 60f.dp()
                            layoutParams.width = 80f.dp()
                            layoutParams.topMargin = 0
                            ivVideo.layoutParams = layoutParams

                            //回答视频审核
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.VISIBLE
                            tvText = textTitle
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            textDescription.text =
                                "${item.answerUsername}: ${
                                    item.answerContent.replace(
                                        "&nbsp;",
                                        " "
                                    )
                                }"

                            root.setOnClickListener {
                                if (item.type == OfficialMessageData.typeAnswerImageExamineSuccess) {
                                    ARouter.getInstance().build("/question/activity/answer_detail")
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withInt(TagConstant.ANSWER_ID, item.answerId)
                                        .withInt(TagConstant.EXAMINE_ID, item.examineId)
                                        .navigation()
                                } else {
                                    ARouter.getInstance()
                                        .build("/mine/message/modify_violate_answer")
                                        .withString(TagConstant.TYPE, item.type)
                                        .withString(TagConstant.PARAMS, item.title)
                                        .withInt(
                                            TagConstant.ANSWER_ID,
                                            item.answerId
                                        ).withInt(
                                            TagConstant.QUESTION_CHECK_ID,
                                            item.examineId
                                        ).withInt(
                                            TagConstant.QUESTION_ID,
                                            item.questionId
                                        )
                                        .withString(TagConstant.TYPE, "answer_image")
                                        .navigation()

                                }
                            }
                        }
                        OfficialMessageData.typeQuestionImageExamineFail,
                        OfficialMessageData.typeQuestionImageExamineSuccess -> {
                            //问题图片审核
                            ivVideo.visible()
                            ivVideoPlay.gone()
                            ivVideo.loadRadius(item.image, 8f.dp())
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE

                            var layoutParams = ivVideo.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = 42f.dp()
                            layoutParams.width = 56f.dp()
                            layoutParams.topMargin = 0
                            ivVideo.layoutParams = layoutParams

                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder =
                                SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                if (item.type == OfficialMessageData.typeQuestionImageExamineSuccess) {
                                    ARouter.getInstance().build("/question/activity/ask_detail")
                                        .withInt(TagConstant.QUESTION_ID, item.questionId)
                                        .withInt(TagConstant.EXAMINE_ID, item.examineId)
                                        .navigation()
                                } else {
                                    ARouter.getInstance()
                                        .build("/mine/message/modify_violate_question")
                                        .withInt(
                                            TagConstant.QUESTION_ID,
                                            item.questionId
                                        ).withInt(
                                            TagConstant.QUESTION_CHECK_ID,
                                            item.examineId
                                        )
                                        .withString(TagConstant.TYPE, "question_image")
                                        .navigation()
                                }


                            }
                        }
                    }

                    val span = VerticalImageSpan(context, bitmap)
                    spannableStringBuilder?.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    tvText?.text = spannableStringBuilder
                }
                OfficialMessageData.typeFeedbackAnswer,
                OfficialMessageData.typeFeedbackArticle,
                OfficialMessageData.typeFeedbackQuestion,
                OfficialMessageData.typeFeedbackVideo -> {
                    tvDate.visibility = View.VISIBLE

                    var bitmap: Bitmap? = null
                    var spannableStringBuilder: SpannableStringBuilder? = null

                    var tvText: TextView? = null
                    //删除操作 - 违规
                    when (item.type) {
                        OfficialMessageData.typeFeedbackArticle -> {
                            //删除违规文章
                            tvDate.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            llTitle.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_article
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/handle_report_result")
                                    .withInt(TagConstant.PARAMS, item.id)
                                    .navigation()
                            }
                        }

                        OfficialMessageData.typeFeedbackVideo -> {
                            //投诉反馈视频
                            tvDate.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            llTitle.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            textTitle.text = item.title

                            ivVideo.visible()
                            ivVideoPlay.visible()

                            ivVideo.loadRadius(item.videoCover.ifEmpty { item.video }, 8f.dp())
                            var layoutParams = ivVideo.layoutParams as ConstraintLayout.LayoutParams
                            layoutParams.height = 42f.dp()
                            layoutParams.width = 56f.dp()
                            layoutParams.topMargin = 0
                            ivVideo.layoutParams = layoutParams

                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/handle_report_result")
                                    .withInt(TagConstant.PARAMS, item.id)
                                    .navigation()
                            }
                        }
                        OfficialMessageData.typeFeedbackAnswer -> {
                            //删除违规问题回答
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.VISIBLE
                            tvText = textTitle
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder = SpannableStringBuilder("  ${item.title}")
                            textDescription.text =
                                "${item.answerUsername}: ${
                                    item.answerContent.replace(
                                        "&nbsp;",
                                        " "
                                    )
                                }"

                            root.setOnClickListener {
                                ARouter.getInstance().build("/mine/message/handle_report_result")
                                    .withInt(TagConstant.PARAMS, item.id)
                                    .navigation()

                            }
                        }
                        OfficialMessageData.typeFeedbackQuestion -> {
                            //删除违规问题
                            llTitle.visibility = View.VISIBLE
                            viewLine3.visibility = View.VISIBLE
                            textDescription.visibility = View.GONE
                            tvReward.text =
                                item.description.setKeyHighLight(item.descriptionHighlight)
                            tvText = textTitle
                            bitmap =
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.ic_message_question
                                )
                            spannableStringBuilder =
                                SpannableStringBuilder("  ${item.title}")
                            root.setOnClickListener {
                                ARouter.getInstance()
                                    .build("/mine/message/handle_report_result")
                                    .withInt(TagConstant.PARAMS, item.id)
                                    .navigation()
                            }
                        }
                    }

                    bitmap?.let {

                        val span = VerticalImageSpan(context, it)
                        spannableStringBuilder?.setSpan(
                            span,
                            0,
                            2,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                        tvText?.text = spannableStringBuilder
                    }
                }
            }
        }
    }
}
