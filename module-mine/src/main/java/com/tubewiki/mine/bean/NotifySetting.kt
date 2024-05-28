package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class NotifySetting(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("user")
    var user: UserNotifySetting = UserNotifySetting()
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class UserNotifySetting(
        @SerializedName("email")
        var email: String = "",
        @SerializedName("has_password")
        var hasPassword: Boolean = false,
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("notify_focus")
        var notifyFocus: Int = 0,
        @SerializedName("notify_message")
        var notifyMessage: Int = 0,
        @SerializedName("notify_news")
        var notifyNews: Int = 0,
        @SerializedName("notify_reward")
        var notifyReward: Int = 0,
        @SerializedName("open_reward")
        var openReward: Int = 0,
        @SerializedName("is_push_hot")
        var isPushHot: Int = 0,//:关闭，1：开启
        @SerializedName("password")
        var password: String = "",
        @SerializedName("uid")
        var uid: Int = 0
    ) : Parcelable
}