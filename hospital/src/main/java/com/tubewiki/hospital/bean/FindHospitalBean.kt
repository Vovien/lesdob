package com.tubewiki.hospital.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class FindHospitalBean(

    @SerializedName("hospitals")
    var hospitals: MutableList<HospitalBean> = mutableListOf(),
    @SerializedName("recommend_hospitals")
    var recommendHospitals: MutableList<HospitalBean> = mutableListOf(),
) : Parcelable