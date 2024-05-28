package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class AnswerMessageData(
    @SerializedName("answers")
    var answers: ArrayList<Answer> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Answer(
        @SerializedName("add_time")
        var addTime: Int = 0,
        @SerializedName("answer_content")
        var answerContent: String = "",
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("answser_content")
        var answserContent: String = "",
        @SerializedName("is_read")
        var isRead: Boolean = false,
        @SerializedName("question_content")
        var questionContent: String = "",
        @SerializedName("question_id")
        var questionId: Int = 0,
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user")
        var user: User = User()
    ) : Parcelable
}
