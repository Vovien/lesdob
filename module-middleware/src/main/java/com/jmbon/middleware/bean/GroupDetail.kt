package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class GroupDetail(
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("group_name")
    var groupName: String = "",
    @SerializedName("notice")
    var notice: String = "",
    @SerializedName("notices")
    var notices: MutableList<Notice> = mutableListOf(),
    @SerializedName("number")
    var number: String = ""
) : Parcelable {
    @Keep
    @Parcelize
    data class Notice(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("message")
        var message: String = ""
    ) : Parcelable
}