package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.utils.TagConstant
import kotlinx.parcelize.Parcelize
import java.io.Serializable


/**
 * 问题bean
 */
@Parcelize
@Keep
data class Question(
    @SerializedName("focus_count")
    var focusCount: Int = 0,
    @SerializedName("user")
    var user: User = User(),
    @SerializedName("add_time")
    var add_time: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("update_time")
    var updateTime: Int = 0,
    @SerializedName("covers")
    var covers: String = "",
    @SerializedName("question_content_highlight")
    var highlight: ArrayList<String> = arrayListOf(),
    @SerializedName("draft")
    var draft: SketchData.Draft? = SketchData.Draft(),
    @SerializedName("images")
    var images: ArrayList<String>? = arrayListOf(),
    @SerializedName("is_focus")
    var is_focus: Boolean = false,
    @SerializedName("is_reply")
    var is_reply: Boolean = false,
    @SerializedName("my_reply_id")
    var myReplyId: Int = 0,
    @SerializedName("is_reward")
    var isReward: Int = 0,
    @SerializedName("question_detail")
    var questionDetail: String = "",
    @SerializedName("reply")
    var reply: AnswerBean? = AnswerBean(),
    @SerializedName("reward_money")
    var reward_money: String = "",
    @SerializedName("reware_rule")
    var reware_rule: String = "",
    @SerializedName("topics")
    var topics: ArrayList<TopicBean>? = arrayListOf(),
    @SerializedName("video")
    var video: String = "",
    @SerializedName("video_status")
    var video_status: Int = 1, //审核中是0 ，审核通过1   -1的就是拒审，2.5.0版本需求不显示视频
    @SerializedName("view_count")
    var view_count: Int = 0,
    @SerializedName("comment_count")
    var comment_count: Int = 0,
    @SerializedName("give_count")
    var give_count: Int = 0,
    @SerializedName("answer")
    var answer: AnswerBean = AnswerBean(),

    @SerializedName("answer_count")
    var answerCount: Int = 0,
    @SerializedName("can_adopt")
    var canAdopt: Boolean = false,
    @SerializedName("has_adopt")
    var hasAdopt: Boolean = false,
    @SerializedName("is_del")
    var isDel: Int = 0, //问题和回答 0:正常,审核通过 1:删除  2:审核中 (机审中和后台审核中都是这个) 3:审核不通过(机审不通过或者后台审核不通过) 4:审核不通过,但是可以恢复
    @SerializedName("published_uid")
    var publishedUid: Int = 0,
    @SerializedName("question_content")
    var questionContent: String = "",
    @SerializedName("question_id")
    var questionId: Int = 0,
    @SerializedName("reward_answer_id")
    var rewardAnswerId: Int = 0,
    @SerializedName("has_reply")
    var has_reply: Boolean = false,
    @SerializedName("is_read")
    var isRead: Int = 0,
    @SerializedName("type")
    var type: Int = 2,//'对应显示类型，1：回答样式，2：列表样式',
    @SerializedName("is_from_circle")
    var isFromCircle: Int = 0,
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("circle")
    var circle: Circle = Circle(),

    @SerializedName("has_video")
    var hasVideo: Boolean = false,

    //是否在审核中
    @SerializedName("update_type")
    var updateType: Int = 0, //0 默认状态 1 失败
    //是否在审核中
    @SerializedName("is_finish_check")
    var isFinishCheck: Int = 0, //1 审核完成
    //视频审核状态
    @SerializedName("audit_status")
    var auditStatus: Int = 1, //0 未审核 ，1 审核完成 （通过或者不通过）

    @SerializedName("block_message_status")
    var blockMessageStatus: Int = 0, // block_message_status  ,默认为0 , 1     展示:内容审核中 ; 2展示提示 : 内容部分涉嫌违规

    @SerializedName("origin_data")
    var originData: OriginData? = null,
    @SerializedName("origin_question_data")
    var originQuestionData: OriginData? = null,
    @SerializedName("block_data")
    var blockData: BlockData? = null,
    @SerializedName("check_log_id")
    var check_log_id: Int = 0,
    @SerializedName("views")
    var views: Int = 0,
    @SerializedName("waitting_count")
    var waittingCount: Int = 0,

    @SerializedName("create_time")
    var createTime: Long = 0L,
    @SerializedName("time")
    var time: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("lock")
    var lock: Int = 0,// 0  未锁定  1：已锁定
    @SerializedName("custom_title")
    var customTitle: String = "",
    @SerializedName("custom_detail")
    var customDetails: String? = "",
//    @SerializedName("reply")
//    var replyId: Reply? = Reply()

) : Serializable, Parcelable

@Parcelize
data class Reply(
    @SerializedName("answer_id")
    var answerId: Int = 0
) : Parcelable, Serializable

@Parcelize
data class Circle(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("covers")
    var covers: String = "",
    @SerializedName("member_count")
    var member_count: Int = 0,
    @SerializedName("discuss_count")
    var discussCount: Int = 0,
    @SerializedName("question_count")
    var question_count: Int = 0,
    @SerializedName("number")
    var number: String = "",
    @SerializedName("group_id")
    var group_id: Int = 0,
    @SerializedName("is_join")
    var isJoin: Boolean = false,
    @SerializedName("users")
    var users: ArrayList<User>? = arrayListOf(),
) : Parcelable, Serializable


fun Question.getShareTitle(): String {
    return if (customTitle.isNullOrEmpty()) questionContent else customTitle
}

fun toQuestionDetails(
    questionId: Int,
    answerId: Int,
    title: String,
    isReply: Boolean,
    publishedUid: Int,
    answerCount: Int,
    answerContent: String,
    ownerUser: User
) {
    ownerUser.categoryTitle = ownerUser.jobName
    val question = Question(
        questionId = questionId,
        questionContent = title,
        answerCount = answerCount,
        publishedUid = publishedUid,
        is_reply = isReply,
        answer = AnswerBean(
            answerId = answerId, answerContent = answerContent,
            user = ownerUser
        )
    )
    ARouter.getInstance().build("/question/activity/answer_detail")
        .withSerializable(TagConstant.QUESTION_DATA, question)
        //.withInt(TagConstant.ANSWER_ID, question.answer!!.answerId)
        .withSerializable(TagConstant.ANSWER_DATA, question.answer)
        .navigation()
}

fun toQuestionDetails(
    questionId: Int,
    answerId: Int,
    title: String,
    isReply: Boolean,
    publishedUid: Int,
    answerCount: Int,
    answerContent: String,
    ownerUser: User,
    circleId: Int,
    circle: Circle,
    isShareCircle: Boolean,
) {
    ownerUser.categoryTitle = ownerUser.jobName
    val question = Question(
        questionId = questionId,
        questionContent = title,
        answerCount = answerCount,
        publishedUid = publishedUid,
        is_reply = isReply,
        answer = AnswerBean(
            answerId = answerId, answerContent = answerContent,
            user = ownerUser, circleId = circleId, circle = circle
        )
    )
    ARouter.getInstance().build("/question/activity/answer_detail")
        .withSerializable(TagConstant.QUESTION_DATA, question)
        //.withInt(TagConstant.ANSWER_ID, question.answer!!.answerId)
        .withSerializable(TagConstant.ANSWER_DATA, question.answer)
        .withBoolean(TagConstant.PARAMS, isShareCircle)
        .navigation()
}

fun toQuestionDetails(
    questionId: Int,
    title: String,
    isReply: Boolean,
    publishedUid: Int,
    answerCount: Int,
    isDel: Int,
    lock: Int,
    answer: AnswerBean,
    isShareCircle: Boolean,
    isFromPersonPage: Boolean = false,
) {

    var mAnswerBean = AnswerBean()
    mAnswerBean.answerContent = answer.answerContentHtml
    mAnswerBean.answerContentHtml = answer.answerContentHtml
    answer.check_log_id = answer.check_log_id

    mAnswerBean.answerId = answer.answerId
    mAnswerBean.circle = answer.circle
    mAnswerBean.itemId = answer.itemId
    mAnswerBean.check_log_id = answer.check_log_id
    mAnswerBean.isChecked = answer.isChecked
    mAnswerBean.isDel = answer.isDel
    mAnswerBean.blockMessageStatus = answer.blockMessageStatus
    mAnswerBean.publishedUid = answer.publishedUid
    mAnswerBean.isFinishCheck = answer.isFinishCheck
    mAnswerBean.blockMessage = answer.blockMessage
    mAnswerBean.blockData = answer.blockData
    mAnswerBean.originAnswerContent =
        if (answer.originAnswerContent.isNullOrEmpty()) "" else answer.originAnswerContent
    mAnswerBean.user = answer.user


    val question = Question(
        questionId = questionId,
        questionContent = title,
        answerCount = answerCount,
        publishedUid = publishedUid,
        is_reply = isReply,
        isDel = isDel,
        lock = lock,
        answer = mAnswerBean
    )
    ARouter.getInstance().build("/question/activity/answer_detail")
        .withSerializable(TagConstant.QUESTION_DATA, question)
        .withSerializable(TagConstant.ANSWER_DATA, question.answer)
        .withBoolean(TagConstant.PARAMS, isShareCircle)
        .withBoolean(TagConstant.FROM_MINE_PERSON_PAGE, isFromPersonPage)
        .navigation()

}

