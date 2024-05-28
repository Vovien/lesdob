package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FollowTopicData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("recommends")
    var recommends: ArrayList<Topic> = arrayListOf(),
    @SerializedName("topics")
    var topics: ArrayList<Topic> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Topic(
        @SerializedName("focus_count")
        var focusCount: Int = 0,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("question_count")
        var questionCount: Int = 0,
        @SerializedName("topic_id")
        var topicId: Int = 0,
        @SerializedName("topic_pic")
        var topicPic: String = "",
        @SerializedName("topic_title")
        var topicTitle: String = ""
    ) : Parcelable
}
