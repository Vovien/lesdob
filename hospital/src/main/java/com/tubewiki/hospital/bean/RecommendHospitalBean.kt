package com.tubewiki.hospital.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class RecommendHospitalBean(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var data: MutableList<HospitalBean> = mutableListOf(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable