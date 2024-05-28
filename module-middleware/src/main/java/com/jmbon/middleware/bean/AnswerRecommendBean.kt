package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class AnswerRecommendBean(
    @SerializedName("answer")
    var answer: AnswerBean = AnswerBean(),
    @SerializedName("recommend")
    var recommend: Recommend? = Recommend(),
    @SerializedName("article")
    var article: ArticleDetails.Article? = ArticleDetails.Article(),
    @SerializedName("is_recommend")
    var isRecommend: Boolean = false,
    @SerializedName("question")
    var question: Question? = Question(),
) : Parcelable {
    @Keep
    @Parcelize
    data class Recommend(
        @SerializedName("content")
        var content: Content = Content(),
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: Int = 0 // 1文章 2问题回答
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Content(
            @SerializedName("abstract")
            var abstract: String = "",
            @SerializedName("content")
            var content: String = "",
            @SerializedName("answer_id")
            var answerId: Int = 0,
            @SerializedName("item_id")
            var item_id: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("title")
            var title: String = "",
            @SerializedName("answer_content")
            var answer_content: String = "",
            @SerializedName("answer_content_html")
            var answer_content_html: String = "",
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("user")
            var user: User? = User()
        ) : Parcelable
    }
}