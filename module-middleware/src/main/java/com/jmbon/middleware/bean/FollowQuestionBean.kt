package com.jmbon.middleware.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FollowQuestionBean(
    @SerializedName("add_time")
    var add_time: Int,
    @SerializedName("give_count")
    var give_count: Int,
    @SerializedName("comment_count")
    var comment_count: Int,
    @SerializedName("answer_content")
    var answer_content: String,
    @SerializedName("answer_id")
    var answer_id: Int,
    @SerializedName("answer_images")
    var answer_images: MutableList<String>,
    @SerializedName("videos")
    var videos: MutableList<String>,
    @SerializedName("is_read")
    var is_read: Int = 1,
    @SerializedName("question_content")
    var question_content: String,
    @SerializedName("question_id")
    var question_id: Int,
    @SerializedName("uid")
    var uid: Int,
    @SerializedName("user")
    var user: User,
    @SerializedName("answer_content_html")
    var answerContentHtml: String = "",
    @SerializedName("origin_answer_content")
    var originAnswerContent: String? = "",
    @SerializedName("answer_count")
    var answerCount: Int = 0,

    @SerializedName("is_del")
    var isDel: Int = 0,
    @SerializedName("is_reply")
    var is_reply: Boolean = false,
    @SerializedName("published_uid")
    var publishedUid: Int = 0,

    @SerializedName("circle_id")
    var circle_id: Int = 0,
    @SerializedName("circle")
    var circle: Circle = Circle(),
    @SerializedName("check_log_id")
    var check_log_id: Int = 0,

    //是否在审核中
    @SerializedName("is_finish_check")
    var isFinishCheck: Int = 0, //1 审核完成
//是否在审核中
    @SerializedName("is_checked")
    var isChecked: Int = 0, //1 审核完成
    @SerializedName("block_message")
    var blockMessage: String = "",
    @SerializedName("lock")
    var lock: Int =0,// 0  未锁定  1：已锁定
    @SerializedName("block_message_status")
    var blockMessageStatus: Int = 0, // block_message_status  ,默认为0 , 1     展示:内容审核中 ; 2展示提示 : 内容部分涉嫌违规

    @SerializedName("origin_data")
    var originData: OriginData? = null,
    @SerializedName("origin_question_data")
    var originQuestionData: OriginData? = null,
    @SerializedName("block_data")
    var blockData: BlockData? = null,
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FollowQuestion(
    @SerializedName("datas")
    var datas: ArrayList<FollowQuestionBean> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int? = 0,
    @SerializedName("total")
    var total: Int? = 0
) : Parcelable