package com.tubewiki.mine.bean


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserDetailsV2(
    @SerializedName("circles")
    var circles: ArrayList<GroupCircle> = arrayListOf(),
    @SerializedName("tools")
    var tools: ArrayList<LittleTool> = arrayListOf(),
    @SerializedName("menstrual")
    var menstrual: MenstrualData? = null,
    @SerializedName("bill")
    var bill: TubeBillData? = null,
    @SerializedName("user")
    var user: User? = null
) : Parcelable
