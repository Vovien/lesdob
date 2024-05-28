package com.jmbon.pay.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class AliUserInfo(
    @SerializedName("data")
    var `data`: Data = Data()
) : Parcelable {
    @Keep
    @Parcelize
    data class Data(
        @SerializedName("avatar")
        var avatar: String = "",
        @SerializedName("city")
        var city: String = "",
        @SerializedName("code")
        var code: String = "",
        @SerializedName("gender")
        var gender: String = "",
        @SerializedName("msg")
        var msg: String = "",
        @SerializedName("nick_name")
        var nickName: String = "",
        @SerializedName("province")
        var province: String = "",
        @SerializedName("user_id")
        var userId: String = ""
    ) : Parcelable
}