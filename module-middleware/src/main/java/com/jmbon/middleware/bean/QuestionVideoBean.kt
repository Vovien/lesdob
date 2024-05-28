package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class QuestionVideoBean(
    @SerializedName("cover")
    var cover: String = "",
    @SerializedName("create_time")
    var createTime: Int = 0,
    @SerializedName("duration")
    var duration: Int = 0,
    @SerializedName("give_count")
    var giveCount: Int = 0,
    @SerializedName("view_count")
    var views: Int = 0,
    @SerializedName("comment_count")
    var commentCount: Int = 0,
    @SerializedName("watch_count")
    var watchCount: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("play_count")
    var playCount: Int = 0,
    @SerializedName("fake_play_count")
    var fakePlayCount: Int = 0,
    @SerializedName("style")
    var style: Int = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("uid")
    var uid: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("play_url_mp4")
    var playUrlMp4: String = "",
    @SerializedName("play_url_u3u8")
    var playUrlU3u8: String = "",
    @SerializedName("user")
    var user: User = User()
) : Parcelable
