package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class HospitalBean(
    @SerializedName("datas")
    var datas: ArrayList<Data> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @Keep
    @Parcelize
    data class Data(
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
        var lat: Int = 0,
        @SerializedName("level_name")
        var levelName: String = "",
        @SerializedName("lng")
        var lng: Int = 0,
        @SerializedName("logo")
        var logo: String = "",
        @SerializedName("province")
        var province: String = "",
        @SerializedName("province_id")
        var provinceId: Int = 0
    ) : Parcelable
}