package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.Question
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class MineDraftData(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("datas")
    var datas: ArrayList<Data> = arrayListOf(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Data(
        @SerializedName("answer_content")
        var answerContent: String = "",
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("images")
        var images: ArrayList<String> = arrayListOf(),
        @SerializedName("is_read")
        var isRead: Int = 0,
        @SerializedName("published_uid")
        var publishedUid: Int = 0,
        @SerializedName("question_content")
        var questionContent: String = "",
        @SerializedName("question_id")
        var questionId: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("update_time")
        var updateTime: Int = 0,
        @SerializedName("lock")
        var lock: Int =0,// 0  未锁定  1：已锁定
        @SerializedName("user")
        var user: User = User()
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class User(
            @SerializedName("avatar_file")
            var avatarFile: String = "",
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("is_auth")
            var isAuth: Boolean = false,
            @SerializedName("notify_message")
            var notifyMessage: Int = 0,
            @SerializedName("open_reward")
            var openReward: Int = 0,
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("user_name")
            var userName: String = "",
            @SerializedName("verify_type")
            var verifyType: Int = 0
        ) : Parcelable
    }
}

fun MineDraftData.Data.convertQuestion(): Question {
    val question = Question()

    question.draft?.answerContent = this.answerContent
    question.draft?.answerId = answerId
    question.draft?.createTime = createTime
    question.draft?.updateTime = updateTime
    question.images = images
    question.publishedUid = publishedUid
    question.questionContent = questionContent
    question.questionId = questionId
    question.lock = lock
    return question
}