package com.jmbon.middleware.comment.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class GivenCount(
    @SerializedName("count")
    var count: Int = 0,
) : Parcelable