package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class CommentMessageData(
    @SerializedName("comments")
    var comments: ArrayList<Comment> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Comment(
        @SerializedName("add_time")
        var addTime: Int = 0,
        @SerializedName("answer_content")
        var answerContent: String = "",
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("data")
        var `data`: Data = Data(),
        @SerializedName("is_read")
        var isRead: Boolean = false,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("reply_id")
        var replyId: Int = 0,
        @SerializedName("top_answer_id")
        var topAnswerId: Int = 0,
        @SerializedName("user")
        var user: User = User(),
        @SerializedName("type")
        var type: Int = 0
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class Data(
            @SerializedName("answer")
            var answer: Answer = Answer(),
            @SerializedName("comment")
            var comment: Comment = Comment(),
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("question_content")
            var questionContent: String = "",
            @SerializedName("question_id")
            var questionId: Int = 0,
            @SerializedName("cover")
            var cover: String = "",
            @SerializedName("title")
            var title: String = ""
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            @Keep
            data class Answer(
                @SerializedName("answer_content")
                var answerContent: String = "",
                @SerializedName("answer_id")
                var answerId: Int = 0,
                @SerializedName("user")
                var user: User = User(),
                @SerializedName("top_answer_id")
                var topAnswerId: Int = 0
            ) : Parcelable

            @SuppressLint("ParcelCreator")
            @Parcelize
            @Keep
            data class Comment(
                @SerializedName("answer_content")
                var answerContent: String = "",
                @SerializedName("answer_id")
                var answerId: Int = 0,
                @SerializedName("user")
                var user: User = User(),
                @SerializedName("top_answer_id")
                var topAnswerId: Int = 0
            ) : Parcelable
        }
    }
}