package com.tubewiki.home.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class FeedbackResult(
    @SerializedName("helpfulCount")
    var helpfulCount: Int = 0,
    @SerializedName("id")
    var id: Int = 0
) : Parcelable