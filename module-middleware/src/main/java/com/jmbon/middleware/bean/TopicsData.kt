package com.jmbon.middleware.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * 话题列表返回数据
 */
//@SuppressLint("ParcelCreator")
//@Parcelize
//@Keep
//data class TopicsData(
//    val topics: MutableList<TopicBean>,
//    val pregnant_status: Int
//    val pregnant_status: Int
//)


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class TopicsData(
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("pregnant_status")
    var pregnantStatus: Int = 0,
    @SerializedName("topics")
    var topics: ArrayList<Topic> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Topic(
        @SerializedName("discuss_count")
        var discussCount: Int = 0,
        @SerializedName("focus_count")
        var focusCount: Int = 0,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("topic_description")
        var topicDescription: String = "",
        @SerializedName("topic_id")
        var topicId: Int = 0,
        @SerializedName("topic_pic")
        var topicPic: String = "",
        @SerializedName("topic_title_highlight")
        var highlight: ArrayList<String> = arrayListOf(),
        @SerializedName("topic_title")
        var topicTitle: String = ""
    ) : Parcelable
}