package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TopicDetail(
    @SerializedName("topic_detail")
    val topic: Topic? = Topic()
) : Parcelable

@Keep
@Parcelize
data class Topic(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("customTitle")
    var customTitle: String = "",
    @SerializedName("customDescription")
    var customDescription: String = "",
    @SerializedName("indexCover")
    var indexCover: String = "",
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("topic_name")
    var topicName: String = "",
    @SerializedName("cover")
    var cover: String = "",
    @SerializedName("cover_color")
    var coverColor: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("articleNum")
    var articleNum: String = "",
    @SerializedName("left_button")
    val leftButton: DetailButtonBean? = null,
    @SerializedName("right_button")
    val rightButton: DetailButtonBean? = null,
) : Parcelable

@Keep
@Parcelize
data class DetailButtonBean(
    val title: String = "",
    @SerializedName("subscript")
    val label: String = "",
    val item_type: String = "",
    val identity: String = ""
) : Parcelable
