package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class VideoDetail(
    @SerializedName("data") var data: List<VideoData> = listOf(),
) : Parcelable {

    @Keep
    @Parcelize
    data class VideoData(
        @SerializedName("dynamic_cover")
        val dynamicCover: String = "",
        @SerializedName("risk_from_title")
        val riskFromTitle: String = "",
        @SerializedName("risk_from_url")
        val riskFromUrl: String = "",
        @SerializedName("risk_type")
        val riskType: Int = 0,
        @SerializedName("style")
        val style: Int = 0,
        @SerializedName("uid")
        val uid: Int = 0,
        @SerializedName("adv")
        val adv: Int = 0,
        @SerializedName("aliyun_cover")
        val aliyunCover: String = "",
        @SerializedName("aliyun_video_id")
        val aliyunVideoId: String = "",
        @SerializedName("cover")
        val cover: String = "",
        @SerializedName("create_time")
        val createTime: Int = 0,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("duration")
        val duration: Int = 0,
        @SerializedName("height")
        val height: Int = 0,
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("identity")
        val identity: String = "",
        @SerializedName("item_type")
        val itemType: String = "",
        @SerializedName("origin_url")
        val originUrl: String = "",
        @SerializedName("play_url_mp4")
        val playUrlMp4: String = "",
        @SerializedName("play_url_u3u8")
        val playUrlU3u8: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("button")
        val button: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("user")
        val user: User = User(),
        @SerializedName("width")
        val width: Int = 0,
        var lastVideo: Boolean = false,
        var advList :List<VideoAdv> = listOf(),
    ) : Parcelable

    @Keep
    @Parcelize
    data class User(
        @SerializedName("avatar_file")
        val avatarFile: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("user_name")
        val userName: String = ""
    ) : Parcelable

}