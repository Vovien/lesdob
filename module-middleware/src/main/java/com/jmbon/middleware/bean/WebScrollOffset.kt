package com.jmbon.middleware.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class WebScrollOffset(
    @SerializedName("offsetTop")
    var offsetTop: Int = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("val")
    var valX: String = "",
    @SerializedName("allOffsetTop")
    var allOffsetTop: Int = 0,
) : Parcelable