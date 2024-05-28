package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class MutuallyMessageData(
    @SerializedName("messages")
    var messages: ArrayList<Message> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Message(
        @SerializedName("add_time")
        var addTime: Int = 0,
        @SerializedName("data")
        var `data`: Data = Data(),
        @SerializedName("data_type")
        var dataType: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("is_read")
        var isRead: Boolean = false,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("tab")
        var tab: String = "",
        @SerializedName("type") //1 点赞 2收藏
        var type: Int = 0,
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user")
        var user: User = User()
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class Data(
            @SerializedName("answer")
            var answer: Answer = Answer(),
            @SerializedName("comment")
            var comment: Comment? = null,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("question_content")
            var questionContent: String = "",
            @SerializedName("question_id")
            var questionId: Int = 0,
            @SerializedName("title")
            var title: String = ""  ,
            @SerializedName("cover")
            var cover: String = ""
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            @Keep
            data class Answer(
                @SerializedName("answer_content")
                var answerContent: String = "",
                @SerializedName("answer_id")
                var answerId: Int = 0,
                @SerializedName("item_id")
                var itemId: Int = 0,
                @SerializedName("top_answer_id")
                var topAnswerId: Int = 0,
                @SerializedName("type")
                var type: Int = 0,
                @SerializedName("uid")
                var uid: Int = 0,
                @SerializedName("user")
                var user: User = User()
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                @Keep
                data class User(
                    @SerializedName("avatar_file")
                    var avatarFile: String = "",
                    @SerializedName("doctor_id")
                    var doctorId: Int = 0,
                    @SerializedName("is_auth")
                    var isAuth: Boolean = false,
                    @SerializedName("notify_message")
                    var notifyMessage: Int = 0,
                    @SerializedName("open_reward")
                    var openReward: Int = 0,
                    @SerializedName("uid")
                    var uid: Int = 0,
                    @SerializedName("user_name")
                    var userName: String = "",
                    @SerializedName("verify_type")
                    var verifyType: Int = 0
                ) : Parcelable
            }

            @SuppressLint("ParcelCreator")
            @Parcelize
            @Keep
            data class Comment(
                @SerializedName("answer_content")
                var answerContent: String = "",
                @SerializedName("answer_id")
                var answerId: Int = 0,
                @SerializedName("item_id")
                var itemId: Int = 0,
                @SerializedName("top_answer_id")
                var topAnswerId: Int = 0,
                @SerializedName("type")
                var type: Int = 0,
                @SerializedName("uid")
                var uid: Int = 0,
                @SerializedName("user")
                var user: User = User()
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                @Keep
                data class User(
                    @SerializedName("avatar_file")
                    var avatarFile: String = "",
                    @SerializedName("doctor_id")
                    var doctorId: Int = 0,
                    @SerializedName("is_auth")
                    var isAuth: Boolean = false,
                    @SerializedName("notify_message")
                    var notifyMessage: Int = 0,
                    @SerializedName("open_reward")
                    var openReward: Int = 0,
                    @SerializedName("uid")
                    var uid: Int = 0,
                    @SerializedName("user_name")
                    var userName: String = "",
                    @SerializedName("verify_type")
                    var verifyType: Int = 0
                ) : Parcelable
            }
        }

        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class User(
            @SerializedName("avatar_file")
            var avatarFile: String = "",
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("is_auth")
            var isAuth: Boolean = false,
            @SerializedName("notify_message")
            var notifyMessage: Int = 0,
            @SerializedName("open_reward")
            var openReward: Int = 0,
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("is_cancel")
            var isCancel: Int = 0, //1 注销
            @SerializedName("user_name")
            var userName: String = "",
            @SerializedName("verify_type")
            var verifyType: Int = 0
        ) : Parcelable
    }

}