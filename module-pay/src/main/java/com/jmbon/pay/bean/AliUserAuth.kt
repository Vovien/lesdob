package com.jmbon.pay.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AliUserAuth(
    @SerializedName("alipay_system_oauth_token_response")
    var alipaySystemOauthTokenResponse: AlipaySystemOauthTokenResponse = AlipaySystemOauthTokenResponse(),
    @SerializedName("sign")
    var sign: String = ""
) : Parcelable {
    @Keep
    @Parcelize
    data class AlipaySystemOauthTokenResponse(
        @SerializedName("access_token")
        var accessToken: String = "",
        @SerializedName("alipay_user_id")
        var alipayUserId: String = "",
        @SerializedName("expires_in")
        var expiresIn: Int = 0,
        @SerializedName("re_expires_in")
        var reExpiresIn: Int = 0,
        @SerializedName("refresh_token")
        var refreshToken: String = "",
        @SerializedName("user_id")
        var userId: String = ""
    ) : Parcelable
}