package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class StsEntity(
    @SerializedName("AccessKeyId") var accessKeyId: String = "",
    @SerializedName("AccessKeySecret") var accessKeySecret: String = "",
    @SerializedName("bucket") var bucket: String = "",
    @SerializedName("endpoint") var endpoint: String = "",
    @SerializedName("Expiration") var expiration: String = "",
    @SerializedName("region") var region: String = "",
    @SerializedName("SecurityToken") var securityToken: String = ""
) : Parcelable