package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class FeedBackBean(
    @SerializedName("feedback")
    var feedback: Feedback = Feedback()
) : Parcelable {
    @Keep
    @Parcelize
    data class Feedback(
        @SerializedName("feedback")
        var feedback: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: Int = 0
    ) : Parcelable
}