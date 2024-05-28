package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class WalletSettingData(
    @SerializedName("data")
    var `data`: Data = Data()
) : Parcelable {
    @Keep
    @Parcelize
    data class Data(
        @SerializedName("deduction")
        var deduction: Boolean = false,
        @SerializedName("has_alipay")
        var hasAlipay: Boolean = false,
        @SerializedName("has_password")
        var hasPassword: Boolean = false,
        @SerializedName("has_wechat")
        var hasWechat: Boolean = false,
        @SerializedName("has_mission")
        var hasMission: Boolean = false,
        @SerializedName("user")
        var user: User = User()
    ) : Parcelable {
        @Keep
        @Parcelize
        data class User(
            @SerializedName("alipay_name")
            var alipayName: String = "",

            @SerializedName("wechat_name")
            var wechatName: String = ""
        ) : Parcelable
    }
}
