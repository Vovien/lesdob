package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class PrivateMessageData(
    @SerializedName("user")
    var user: User = User()
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class User(
        @SerializedName("can_send")
        var canSend: Boolean = false,
        @SerializedName("notify_message")
            var notifyMessage: Int = 0, //1通知，0不通知 2全部禁止
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("verify_type")
            var verifyType: Int = 0
        ) : Parcelable
    }
