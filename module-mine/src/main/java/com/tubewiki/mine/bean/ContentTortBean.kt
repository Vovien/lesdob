package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class ContentTortBean(
    @SerializedName("content_tort")
    var contentTort: ContentTort = ContentTort()
) : Parcelable {
    @Keep
    @Parcelize
    data class ContentTort(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("item_id")
        var itemId: Int = 0,

        @SerializedName("answer_question_id")
        var answerQuestionId: Int = 0,
        @SerializedName("material")
        var material: MutableList<String> = mutableListOf<String>(),
        @SerializedName("origin_content_url")
        var originContentUrl: String = "",
        @SerializedName("published_uid")
        var publishedUid: Int = 0,
        @SerializedName("reason")
        var reason: String = "",

        @SerializedName("status")
        var status: Int = 0, //1 已处理
        @SerializedName("type")
        var type: Int = 0,//'类型【1：文章，2：提问，3：回答】',
        @SerializedName("uid")
        var uid: Int = 0
    ) : Parcelable
}