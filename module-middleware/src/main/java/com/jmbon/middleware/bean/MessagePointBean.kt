package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class MessagePointBean(
    @SerializedName("numbers")
    var numbers: Numbers = Numbers(),
    @SerializedName("offical")
    var offical: String = "",
    @SerializedName("interaction")
    var interaction: String = "",
    @SerializedName("offical_time")
    var officalTime: Int = 0,
    @SerializedName("interaction_time")
    var interactionTime: Int = 0,
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Numbers(
        @SerializedName("answer")
        var answer: Int = 0,
        @SerializedName("comment")
        var comment: Int = 0,
        @SerializedName("fans")
        var fans: Int = 0,
        @SerializedName("reward")
        var reward: Int = 0,
        @SerializedName("offical")
        var offical: Int = 0,
        @SerializedName("interaction")
        var interaction: Int = 0,

    ) : Parcelable
}

