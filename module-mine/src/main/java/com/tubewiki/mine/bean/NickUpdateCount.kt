package com.tubewiki.mine.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class NickUpdateCount(
    @SerializedName("count")
    var count: Int = 0,
    @SerializedName("is_doctor")
    var is_doctor: Boolean = true
) : Parcelable