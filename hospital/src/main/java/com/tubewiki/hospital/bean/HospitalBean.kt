package com.tubewiki.hospital.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class HospitalBean(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("city")
    var city: String = "",
    @SerializedName("city_id")
    var cityId: Int = 0,
    @SerializedName("distance")
    var distance: String = "",
    @SerializedName("doctor_count")
    var doctorCount: Int = 0,
    @SerializedName("hospital_name")
    var hospitalName: String = "",
    @SerializedName("hospital_short_name")
    var hospitalShortName: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("lat")
    var lat: Double = 0.0,
    @SerializedName("length")
    var length: Int = 0,
    @SerializedName("level")
    var level: Int = 0,
    @SerializedName("level_name")
    var levelName: String = "",
    @SerializedName("lng")
    var lng: Double = 0.0,
    @SerializedName("logo")
    var logo: String = "",
    @SerializedName("province")
    var province: String = "",
    @SerializedName("province_id")
    var provinceId: Int = 0,
    
) : Parcelable