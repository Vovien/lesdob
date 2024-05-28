package com.tubewiki.home.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class CommonQuestionBean(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var questions: ArrayList<Data> = arrayListOf(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable {
    @Keep
    @Parcelize
    data class Data(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("problemName")
        var problemName: String = "",
        @SerializedName("topics")
        var topics: ArrayList<Topic> = arrayListOf()
    ) : Parcelable

    @Keep
    @Parcelize
    data class Topic(
        @SerializedName("articleNum")
        var articleNum: Int = 0,
        @SerializedName("cover")
        var cover: String = "",
        @SerializedName("customDescription")
        var customDescription: String = "",
        @SerializedName("customTitle")
        var customTitle: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("icon")
        var icon: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("indexCover")
        var indexCover: String = "",
        @SerializedName("topicName")
        var topicName: String = ""
    ) : Parcelable
}