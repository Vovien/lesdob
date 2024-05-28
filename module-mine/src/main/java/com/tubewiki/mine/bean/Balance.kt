package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Parcelize
@Keep
data class Balance(
    @SerializedName("balance")
    var balance: Float = 0f
) : Parcelable