package com.tubewiki.mine.bean

import android.annotation.SuppressLint
import android.os.Parcelable


import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.AnswerBean
import com.jmbon.middleware.bean.ArticleDetails
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 *  官方消息类型
 *  delete_question： 删除问题
 *  delete_article：删除文章
 *  delete_video：删除视频
 *  delete_question_answer： 删除问题回答
 *  adopt_reward： 采纳用户回答
 *  article_offer_reward：文章打赏
 *  answer_offer_reward： 回答打赏
 *  tort_article：举报文章
 *  tort_video:举报视频
 *  tort_question：举报问题
 *  tort_answer：举报回答
 *  feedback_answer：反馈回答
 *  feedback_question：反馈问题
 *  feedback_article：反馈文章
 *  feedback_video：反馈视频
 *  answer_video_examine_fail：回答视频审核失败
 *  answer_video_examine_success：回答视频审核成功
 *  report_answer_success：回答上报成功
 *  report_answer_fail：回答上报失败
 *  question_video_examine_fail：问题审核失败
 *  question_video_examine_success:问题审核成功
 *  report_question_fail：问题上报失败
 *  report_question_success：问题上报成功
 *
 *
 */

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class OfficialMessageData(
    @SerializedName("officials")
    var officials: ArrayList<Official> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {

    companion object {
        var typeDeleteQuestion = "delete_question"//： 删除问题
        var typeDeleteArticle = "delete_article" //：删除文章
        var typeDeleteVideo = "delete_video" //：删除视频
        var typeDeleteQuestionAnswer = "delete_question_answer"//： 删除问题回答
        var typeAdoptReward = "adopt_reward" //： 采纳用户回答
        var typeRewardBack = "reward_back" //： 悬赏返回
        var typeArticleOfferReward = "article_offer_reward" //：文章打赏
        var typeAnswerOfferReward = "answer_offer_reward"//： 回答打赏
        var typeTortArticle = "tort_article"//：举报文章
        var typeTortVideo = "tort_video"//  :举报视频
        var typeTortQuestion = "tort_question"//：举报问题
        var typeTortAnswer = "tort_answer" // ：举报回答
        var typeFeedbackAnswer = "feedback_answer"//：反馈回答
        var typeFeedbackQuestion = "feedback_question"//：反馈问题
        var typeFeedbackArticle = "feedback_article"//：反馈文章
        var typeFeedbackVideo = "feedback_video"//  ：反馈视频
        var typeAnswerVideoExamineFail = "answer_video_examine_fail"//：回答视频审核失败
        var typeAnswerVideoExamineSuccess = "answer_video_examine_success"//：回答视频审核成功
        var typeReportAnswerSuccess = "report_answer_success"//：回答上报成功
        var typeReportAnswerFail = "report_answer_fail"//：回答上报失败
        var typeQuestionVideoExamineFail = "question_video_examine_fail"//：问题审核失败
        var typeQuestionVideoExamineSuccess = "question_video_examine_success"//:问题审核成功
        var typeReportQuestionFail = "report_question_fail"//：问题上报失败
        var typeReportQuestionSuccess = "report_question_success"//：问题上报成功

        var typeQuestionImageExamineFail = "question_image_violation"//：问题图片审核失败
        var typeQuestionImageExamineSuccess = "question_image_examine_success"//:问题图片审核成功
        var typeAnswerImageExamineFail = "answer_image_violation"//：回答图片审核失败
        var typeAnswerImageExamineSuccess = "answer_image_examine_success"//：回答图片审核成功
    }

    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Official(
        @SerializedName("amount")
        var amount: Float = 0f,
        @SerializedName("answer_content")
        var answerContent: String = "",
        @SerializedName("answer_content_html")
        var answerContentHtml: String = "",
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("answer_username")
        var answerUsername: String = "",
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("comment")
        var comment: String = "",
        @SerializedName("comment_id")
        var commentId: Int = 0,
        @SerializedName("comment_username")
        var commentUsername: String = "",
        @SerializedName("content_type")
        var contentType: Int = 0, //0 无类型 1文章 2提问
        @SerializedName("description")
        var description: String = "",
        @SerializedName("description_highlight")
        var descriptionHighlight: ArrayList<String> = arrayListOf(), //高亮字段
        @SerializedName("examine_id")
        var examineId: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("is_read")
        var isRead: Boolean = false,
        @SerializedName("question_id")
        var questionId: Int = 0,
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("time")
        var time: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: String = "", //消息类型
        @SerializedName("username")
        var username: String = "",
        @SerializedName("video")
        var video: String = "",
        @SerializedName("image")
        var image: String = "",
        @SerializedName("video_cover")
        var videoCover: String = ""
    ) : Parcelable, Serializable
}


