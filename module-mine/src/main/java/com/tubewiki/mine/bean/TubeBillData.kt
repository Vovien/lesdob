package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class TubeBillData(
    @SerializedName("cost")
    var cost: Int = 0,
    @SerializedName("tool_id")
    var toolId: String = ""

) : Parcelable