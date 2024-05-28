package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Parcelize
@Keep
data class PrivacyPolicy(
    @SerializedName("article")
    var privacyPolicy: PrivacyPolicyData = PrivacyPolicyData()
) : Parcelable {
    @Parcelize
    @Keep
    data class PrivacyPolicyData(
        @SerializedName("catalog_name")
        var catalogName: String = "",
        @SerializedName("content")
        var content: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: Int = 0
    ) : Parcelable
}