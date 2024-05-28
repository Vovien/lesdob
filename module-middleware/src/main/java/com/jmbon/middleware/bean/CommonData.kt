package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class CommonData(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable