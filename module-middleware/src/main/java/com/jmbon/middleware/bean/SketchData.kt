package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Keep
data class SketchData(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("draft")
    var draft: Draft = Draft(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable, Serializable {
    @Parcelize
    @Keep
    data class Draft(
        @SerializedName("answer_content")
        var answerContent: String = "",
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("question_id")
        var questionId: Int = 0,
        @SerializedName("update_time")
        var updateTime: Int = 0
    ) : Parcelable, Serializable
}