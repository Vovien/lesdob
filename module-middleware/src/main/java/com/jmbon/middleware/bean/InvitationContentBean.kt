package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class InvitationContentBean(
    @SerializedName("covers")
    var covers: String = "",
    @SerializedName("question_id")
    var questionId: Int = 0,

    @SerializedName("question_content")
    var questionContent: String = "",
    @SerializedName("question_detail")
    var questionDetail: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("aliyun_cover")
    var aliyunCover: String = "",
    @SerializedName("cover")
    var cover: String = "",
    @SerializedName("play_url_mp4")
    var playUrlMp4: String = "",

    @SerializedName("play_url_u3u8")
    var playUrlM3u8: String = "",

    @SerializedName("description")
    var description: String = "",
    @SerializedName("discuss_count")
    var discussCount: Int = 0,
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("duration")
    var duration: Int = 0,
    @SerializedName("is_join")
    var isJoin: Boolean = false,
    @SerializedName("number")
    var number: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("type")
    var type: Int = 0,// //邀请类型【1：问题，2：圈子 3：视频】
    @SerializedName("user")
    var user: User = User(),
) : Parcelable