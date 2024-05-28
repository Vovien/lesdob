package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class EmailBean(
    @SerializedName("email")
    var email: String = ""
) : Parcelable