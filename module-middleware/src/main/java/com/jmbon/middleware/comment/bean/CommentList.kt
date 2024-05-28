package com.jmbon.middleware.comment.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


// evaluation_list
@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class CommentList(
    @SerializedName("answers")
    var answers: MutableList<Comment> = mutableListOf(),
    @SerializedName("comment_total")
    var commentTotal: Int = 0,
    @SerializedName("last_id")
    var lastId: Int = 0,
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
        var addTime: Long = 0,
        @SerializedName("answer_content")
        var answerContent: String? = "",
        @SerializedName("answer_content_text")
        var answerContentText: String? = "",
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("collection_count")
        var collectionCount: Int = 0,
        @SerializedName("comment_count")
        var commentCount: Int = 0,
        @SerializedName("give_count")
        var giveCount: Int = 0,
        @SerializedName("is_collect")
        var isCollect: Boolean = false,
        @SerializedName("is_comment")
        var isComment: Boolean = false,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("is_given")
        var isGiven: Boolean = false,
        @SerializedName("is_mutual_focus")
        var isMutualFocus: Boolean = false,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("owner")
        var owner: UserReceiver = UserReceiver(),
        @SerializedName("receiver")
        var receiver: UserReceiver? = UserReceiver(),
        @SerializedName("reply_id")
        var replyId: Int = 0,
        @SerializedName("second_answer_id")
        var secondAnswerId: Int = 0,
        @SerializedName("top_answer_id")
        var topAnswerId: Int = 0,
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("update_answer")
        var updateAnswer: String? = "",
        @SerializedName("update_type")
        var updateType: Int = 0,
        @SerializedName("user")
        var user: UserReceiver = UserReceiver(),
        @SerializedName("second_answer_count")
        var secondAnswerCount: Int = 0,
        @SerializedName("second_answers")
        var secondAnswers: MutableList<Comment> = arrayListOf(),
        @SerializedName("article_published_uid")
        var articlePublishedUid: Int = 0,
        @SerializedName("video_published_uid")
        var videoPublishedUid: Int = 0,
        @SerializedName("question_published_uid")
        var questionPublishedUid: Int = 0,
        @SerializedName("answer_published_uid")
        var answerPublishedUid: Int = 0,
        var secondAnswerPage: Int = 1,
        var secondAnswerFinish: Boolean = false,
        var clientId: String = "",
        var isPreview: Boolean = false,
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class UserReceiver(
            @SerializedName("avatar_file")
            var avatarFile: String? = "",
            @SerializedName("category_title")
            var categoryTitle: String? = "",
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("is_auth")
            var isAuth: Boolean = false,
            @SerializedName("is_focus")
            var isFocus: Boolean = false,
            @SerializedName("job_name")
            var jobName: String? = "",
            @SerializedName("notify_message")
            var notifyMessage: Int = 0,
            @SerializedName("open_reward")
            var openReward: Int = 0,
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("is_cancel")
            var isCancel: Int = 0,//0：正常，1：已注销
            @SerializedName("user_name")
            var userName: String? = "",
            @SerializedName("verify_type")
            var verifyType: Int = 0
        ) : Parcelable
    }

}
