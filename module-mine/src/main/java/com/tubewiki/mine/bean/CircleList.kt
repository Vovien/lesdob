package com.tubewiki.mine.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CircleList(
    @SerializedName("groups")
    var groups: ArrayList<GroupCircle> = arrayListOf()
) : Parcelable

@Keep
@Parcelize
data class GroupCircle(
    @SerializedName("covers")
    var covers: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("is_join")
    var isJoin: Boolean = false,
    @SerializedName("number")
    var number: String = ""
) : Parcelable
