package com.jmbon.middleware.bean


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VersionInfo(
    @SerializedName("version")
    var version: Version = Version()
) : Parcelable {
    @Parcelize
    data class Version(
        @SerializedName("create_time")
        var createTime: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("download")
        var download: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("version_code")
        var versionCode: Int = 5,
        @SerializedName("is_force")
        var isForce: Int = 0,
        @SerializedName("version_name")
        var versionName: String = ""
    ) : Parcelable
}