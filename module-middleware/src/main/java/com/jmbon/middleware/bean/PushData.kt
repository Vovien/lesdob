package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PushData(
    @SerializedName("msg_id")
    var msg_id: String = "",
    @SerializedName("client_id")
    var clientId: String = "",
    @SerializedName("content")
    var content: Content = Content(),
    @SerializedName("is_send_all")
    var isSendAll: Int = 0,
    @SerializedName("sub_title")
    var subTitle: String = "",
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("group_name")
    var groupName: String = "",
    @SerializedName("number")
    var number: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("msg_flag")
    var msgFlag: Int = 0, //热点推送 0不弹 1弹弹窗
    @SerializedName("verify_type")
    var verifyType: Int = 0
) : Parcelable {
    @Keep
    @Parcelize
    data class Content(
        @SerializedName("answer_content", alternate = ["question_content"])
        var answerContent: String = "",
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("item_id")
        var item_id: Int = 0,
        @SerializedName("question_id")
        var questionId: Int = 0,
        @SerializedName("group_id")
        var groupId: Int = 0,
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("sub_title")
        var subTitle: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("sub_type")
        var subType: Int = 0,
        @SerializedName("days")
        var days: Int = 0,
        @SerializedName("type")
        var type: Int = 0,//1是文章 2是问题
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("number")
        var number: String = "",
        @SerializedName("circle_id")
        var circleId: Int = 0,
        @SerializedName("user_name")
        var userName: String = "",
        @SerializedName("data")
        var `data`: Data = Data(),
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Data(
            @SerializedName("answer_id")
            var answerId: Int = 0,
            @SerializedName("covers")
            var covers: String = "",
            @SerializedName("group_id")
            var groupId: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("is_join")
            var isJoin: Boolean = false,
            @SerializedName("item_id")
            var itemId: Int = 0,
            @SerializedName("member_count")
            var memberCount: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("number")
            var number: String = "",
            @SerializedName("question_content")
            var questionContent: String = "",
            @SerializedName("question_count")
            var questionCount: Int = 0,
            @SerializedName("title")
            var title: String = ""
        ) : Parcelable
    }
}