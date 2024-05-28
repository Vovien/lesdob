package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CircleUser(
    @SerializedName("appeal_count")
    var appealCount: Int = 0,
    @SerializedName("is_admin")
    var isAdmin: Boolean = false,
    @SerializedName("user")
    var user: User = User()
) : Parcelable {
    @Keep
    @Parcelize
    data class User(
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("declaration")
        var declaration: String = "",
        @SerializedName("is_block_circle")
        var isBlockCircle: Int = 0,
        @SerializedName("is_circle_admin")
        var isCircleAdmin: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user_name")
        var userName: String = "",
        @SerializedName("nickname")
        var nickname: String = "",
        @SerializedName("verify_type")
        var verifyType: Int = 0,
        @SerializedName("is_cancel")
        var isCancel: Int = 0, //1 注销
    ) : Parcelable
}