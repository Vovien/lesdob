package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class LogOffSign(
    @SerializedName("cancel_sign")
    var cancelSign: String = ""
) : Parcelable