package com.tubewiki.mine.bean


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class VerificationCodeToken(
    @SerializedName("validate_token")
    var validateToken: String = "",
    @SerializedName("email_token")
    var emailToken: String = ""
)