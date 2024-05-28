package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class FollowUnReadBean(
    @SerializedName("dot")
    var dot: Dot = Dot()
) : Parcelable {
    @Keep
    @Parcelize
    data class Dot(
        @SerializedName("column")
        var column: Int = 0,
        @SerializedName("question")
        var question: Int = 0,
        @SerializedName("topic")
        var topic: Int = 0,
        @SerializedName("user")
        var user: Int = 0
    ) : Parcelable
}