package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class GroupQuestionBean(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("questions")
    var questions: MutableList<Question> = mutableListOf(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @Keep
    @Parcelize
    data class Question(
        @SerializedName("answer_count")
        var answerCount: Int = 0,

        @SerializedName("discuss_count")
        var discussCount: Int = 0,
        @SerializedName("answer_users")
        var answerUsers: MutableList<AnswerUser> = mutableListOf(),
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("is_top")
        var isTop: Int = 0,
        @SerializedName("is_essence")
        var isEssence: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("circle_id")
        var circleId: Int = 0,
        @SerializedName("title_highlight")
        var titleHighlight: ArrayList<String> = arrayListOf(),
        @SerializedName("user_name")
        var userName: String = ""
    ) : Parcelable {
        @Keep
        @Parcelize
        data class AnswerUser(
            @SerializedName("avatar_file")
            var avatarFile: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("question_id")
            var questionId: Int = 0,
            @SerializedName("uid")
            var uid: Int = 0
        ) : Parcelable
    }
}