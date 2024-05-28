package com.tubewiki.mine.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MobileLoginError(
    @SerializedName("carrierFailedResultData")
    var carrierFailedResultData: String = "",
    @SerializedName("code")
    var code: String = "",
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("requestCode")
    var requestCode: Int = 0,
    @SerializedName("requestId")
    var requestId: String = "",
    @SerializedName("vendorName")
    var vendorName: String = ""
) : Parcelable