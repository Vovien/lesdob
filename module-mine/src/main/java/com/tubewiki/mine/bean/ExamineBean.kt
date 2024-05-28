package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.jmbon.middleware.bean.Question

@Keep
@Parcelize
data class ExamineBean(
    @SerializedName("examine")
    var examine: Examine = Examine()
) : Parcelable {
    @Keep
    @Parcelize
    data class Examine(
        @SerializedName("answer_content")
        var answerContent: String = "",
        @SerializedName("answer_content_html")
        var answerContentHtml: String = "",

        @SerializedName("question_content")
        var questionContent: String = "",
        @SerializedName("question_detail")
        var questionDetail: String = "",

        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("question_id")
        var questionId: Int = 0,
        @SerializedName("images")
        var images: ArrayList<String>? = arrayListOf(),
        @SerializedName("video")
        var video: String = "",
        @SerializedName("question")
        var question: Question = Question()
    ) : Parcelable
}