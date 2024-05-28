package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class VodEntity(
    @SerializedName("config") var config: Config = Config(),
    @SerializedName("sts") var sts: Sts = Sts(),
) : Parcelable {
    @Keep
    @Parcelize
    data class Config(
        @SerializedName("bucket") var bucket: String = "",
        @SerializedName("endpoint") var endpoint: String = "",
        @SerializedName("host") var host: String = "",
        @SerializedName("regionId") var regionId: String = "",
        @SerializedName("workflowId") var workflowId: String = "",
    ) : Parcelable

    @Keep
    @Parcelize
    data class Sts(
        @SerializedName("AssumedRoleUser") var assumedRoleUser: AssumedRoleUser = AssumedRoleUser(),
        @SerializedName("Credentials") var credentials: Credentials = Credentials(),
        @SerializedName("RequestId") var requestId: String = "",
    ) : Parcelable {
        @Keep
        @Parcelize
        data class AssumedRoleUser(
            @SerializedName("Arn") var arn: String = "",
            @SerializedName("AssumedRoleId") var assumedRoleId: String = "",
        ) : Parcelable

        @Keep
        @Parcelize
        data class Credentials(
            @SerializedName("AccessKeyId") var accessKeyId: String = "",
            @SerializedName("AccessKeySecret") var accessKeySecret: String = "",
            @SerializedName("Expiration") var expiration: String = "",
            @SerializedName("SecurityToken") var securityToken: String = "",
        ) : Parcelable
    }
}