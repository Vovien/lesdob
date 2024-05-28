package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.jmbon.middleware.bean.TopicBean

@Keep
@Parcelize
data class ExamineDetailBean(
    @SerializedName("answer_id")
    var answerId: Int = 0,
    @SerializedName("content")
    var content: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("images")
    var images: ArrayList<String>? = arrayListOf(),
    @SerializedName("block_images")
    var blockImages: ArrayList<String> = arrayListOf(),
    @SerializedName("is_update")
    var isUpdate: Int = 0, //// 是否已修改【0：未修改，1：已修改。审核中 2已修改审核通过】
    @SerializedName("question_id")
    var questionId: Int = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("topics")
    var topics: ArrayList<TopicBean> = arrayListOf(),
    @SerializedName("type")
    var type: String = "",
    @SerializedName("video")
    var video: String = "",
    @SerializedName("videos")
    var videos: ArrayList<String> = arrayListOf()
) : Parcelable
