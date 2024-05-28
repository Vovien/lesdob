package com.tubewiki.mine.base


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class UserSafeSet(
    @SerializedName("user")
    var safeSet: SafeSet = SafeSet()
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class SafeSet(
        @SerializedName("email")
        var email: String = "",
        @SerializedName("has_email")
        var hasEmail: Boolean = false,
        @SerializedName("has_password")
        var hasPassword: Boolean = false,
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("password")
        var password: String = "",
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("alipay_name")
        var alipay_name: String = "",
        @SerializedName("alipay_account")
        var alipay_account: String = "",
        @SerializedName("wechat_name")
        var wechatName: String = "",
        @SerializedName("wechat_login_openid")
        var wechat_login_openid: String = "",
        @SerializedName("is_binding_wechat")
        var isBindingWechat: Boolean = false,
        @SerializedName("is_binding_alipay")
        var isBindingAlipay: Boolean = false
    ) : Parcelable
}