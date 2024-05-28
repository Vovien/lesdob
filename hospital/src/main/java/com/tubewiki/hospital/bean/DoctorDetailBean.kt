package com.tubewiki.hospital.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class DoctorDetailBean(
    @SerializedName("area_group_cities")
    var areaGroupCities: MutableList<AreaGroupCity> = mutableListOf(),
    @SerializedName("doctor")
    var doctor: Doctor = Doctor(),
    @SerializedName("meta_data")
    var metaData: MetaData = MetaData(),
    @SerializedName("random_doctors")
    var randomDoctors: MutableList<RandomDoctor> = mutableListOf()
) : Parcelable {

    @Keep
    @Parcelize
    data class Doctor(
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("column_ids")
        var columnIds: String = "",
        @SerializedName("columns")
        var columns: MutableList<HospitalDetailBean.Doctor.Column> = mutableListOf(),
        @SerializedName("department")
        var department: HospitalDetailBean.Doctor.Department = HospitalDetailBean.Doctor.Department(),
        @SerializedName("department_id")
        var departmentId: Int = 0,
        @SerializedName("doctor_descript")
        var doctorDescript: String = "",
        @SerializedName("doctor_id")
        var doctorId: Int = 0,
        @SerializedName("focus_count")
        var focusCount: Int = 0,
        @SerializedName("honor")
        var honor: MutableList<String> = mutableListOf(),
        @SerializedName("hospital_name")
        var hospitalName: String = "",
        @SerializedName("is_auth")
        var isAuth: Boolean = false,
        @SerializedName("label_ids")
        var labelIds: String = "",
        @SerializedName("labels")
        var labels: MutableList<HospitalDetailBean.Doctor.Label> = mutableListOf(),
        @SerializedName("name")
        var name: String = "",
        @SerializedName("position")
        var position: String = "",
        @SerializedName("price")
        var price: Int = 0,
        @SerializedName("consult_count")
        var consultCount: Int = 0,
        @SerializedName("star")
        var star: Int = 0,
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("word")
        var word: MutableList<String> = mutableListOf(),
        @SerializedName("work_years")
        var workYears: Int = 0,
        @SerializedName("article_count")
        var articleCount: Int = 0,
        @SerializedName("catalogs")
        var catalogs: MutableList<String> = mutableListOf(),
        @SerializedName("category_titles")
        var categoryTitles: String = "",
        @SerializedName("column_titles")
        var columnTitles: MutableList<HospitalDetailBean.Doctor.Column> = mutableListOf(),
        @SerializedName("content")
        var content: String = "",
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("created_at")
        var createdAt: Int = 0,
        @SerializedName("doctors")
        var doctors: MutableList<HospitalDetailBean.Doctor> = mutableListOf(), //这个doctor用医院的，department字段后台有区别
        @SerializedName("has_expertise_area")
        var hasExpertiseArea: Boolean = false,
        @SerializedName("hospital")
        var hospital: HospitalBean = HospitalBean(),
        @SerializedName("hospital_id")
        var hospitalId: Int = 0,
        @SerializedName("infos")
        var infos: MutableList<DoctorDetailBean.Info> = mutableListOf(),
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("update_time")
        var updateTime: Int = 0,
        @SerializedName("updated_at")
        var updatedAt: String = "",
    ) : Parcelable

    @Keep
    @Parcelize
    data class AreaGroupCity(
        @SerializedName("area_name")
        var areaName: String = "",
        @SerializedName("cities")
        var cities: MutableList<City> = mutableListOf()
    ) : Parcelable {
        @Keep
        @Parcelize
        data class City(
            @SerializedName("name")
            var name: String = "",
            @SerializedName("title")
            var title: String = ""
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class MetaData(
        @SerializedName("meta_description")
        var metaDescription: String = "",
        @SerializedName("meta_keyword")
        var metaKeyword: String = "",
        @SerializedName("meta_title")
        var metaTitle: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class RandomDoctor(
        @SerializedName("doctor_id")
        var doctorId: Int = 0,
        @SerializedName("name")
        var name: String = ""
    ) : Parcelable


    @Keep
    @Parcelize
    data class Info(
        @SerializedName("key")
        var key: String = "",
        @SerializedName("value")
        var value: String = "",
        @SerializedName("id")
        var id: Int = 0
    ) : Parcelable
}