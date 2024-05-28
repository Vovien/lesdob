package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class RewardPayData(
    @SerializedName("sign")
    var sign: String = "",
    @SerializedName("type")
    var type: String = ""
) : Parcelable