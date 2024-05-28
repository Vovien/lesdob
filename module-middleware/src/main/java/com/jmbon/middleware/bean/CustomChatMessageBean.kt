package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.io.Serializable

@Keep
@Parcelize
data class CustomChatMessageBean(
    @SerializedName("type")
    var type: String = "none",
    @SerializedName("sub_title")
    var sub_title: String = "",

    @SerializedName("client_id")
    var clientId: String = "",

    @SerializedName("is_send_all")
    var is_send_all: Int = 0,//是否@全体  0：不是，1：是
    @SerializedName("role")
    var role: Int = 0,//1 普通用户 2管理员 3 群主
    @SerializedName("verify_type")
    var verifyType: Int = 0,//0 普通用户 2医生
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("number")
    var number: String = "",
    @SerializedName("isRefuse")
    var isRefuse: Boolean = false, //免打扰
    @SerializedName("group_name")
    var groupName: String = "",
    @SerializedName("nickname")
    var nickname: String = "",

    @SerializedName("content")
    var content: Content = Content(),

    @SerializedName("source")
    var source: Source = Source(),
) : Parcelable, Serializable {
    @Keep
    @Parcelize
    data class Content(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("answer_id")
        var answerId: Int = 0,
        @SerializedName("text")
        var text: String = "",
        @SerializedName("name")
        var name: String = "",  //圈子名称
        @SerializedName("title")
        var title: String = "", //文章问题标题
        @SerializedName("image")
        var image: String = "", //文章封面图

        @SerializedName("category_name")
        var category_name: String = "",//圈子分类名称


        @SerializedName("member_count")
        var member_count: String = "",//圈子成员数量

        @SerializedName("question_count")
        var question_count: String = "",//问题数量

        @SerializedName("covers")
        var covers: String = "",//圈子封面

        @SerializedName("answer_content")
        var answer_content: String = "",//问题回答内容
        @SerializedName("type")
        var type: String = "",//问题类型【question:问题,help:求助】

        @SerializedName("username")
        var username: String = "",//回答都用户名

        @SerializedName("answer_count")
        var answer_count: Int = 0,//回答个数

        @SerializedName("message")
        var message: String = "",//公告内容

        @SerializedName("create_time")
        var createTime: Long = 0L,//公告时间

        @SerializedName("uid")
        var uid: Int = 0,//踢出成员的uid

        @SerializedName("is_del")
        var is_del: Int = 0,//1求助被删除

        //  @SerializedName("nickname")
        var nickname: String = "",//踢出成员的昵称

        @SerializedName("images")
        var images: MutableList<String> = mutableListOf(),//公告图片

    ) : Parcelable, Serializable

    @Keep
    @Parcelize
    data class Source(
        @SerializedName("type")
        var type: String = "",
        @SerializedName("user_name")
        var user_name: String = "",
        @SerializedName("uid")
        var uid: String = "",
        @SerializedName("content")
        var content: Content = Content()
    ) : Parcelable, Serializable
}



