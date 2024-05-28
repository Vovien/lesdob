package com.tubewiki.hospital.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class HospitalDetailBean(
    @SerializedName("area_group_cities")
    var areaGroupCities: MutableList<AreaGroupCity> = mutableListOf(),
    @SerializedName("doctors")
    var doctors: MutableList<Doctor> = mutableListOf(),
    @SerializedName("hospital")
    var hospital: Hospital = Hospital(),
    @SerializedName("meta_data")
    var metaData: MetaData = MetaData(),
    @SerializedName("random_hospitals")
    var randomHospitals: MutableList<RandomHospital> = mutableListOf(),
    @SerializedName("technology_names")
    var technologyNames: String = ""
) : Parcelable {
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
    data class Doctor(
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("column_ids")
        var columnIds: String = "",
        @SerializedName("columns")
        var columns: MutableList<Column> = mutableListOf(),
        @SerializedName("department")
        var department: String = "",
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
        var labels: MutableList<Label> = mutableListOf(),
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
        var columnTitles: MutableList<Column> = mutableListOf(),
        @SerializedName("content")
        var content: String = "",
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("created_at")
        var createdAt: Int = 0,
        @SerializedName("doctors")
        var doctors: MutableList<Doctor> = mutableListOf(),
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
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Column(
            @SerializedName("column_id")
            var columnId: Int = 0,
            @SerializedName("column_name")
            var columnName: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Label(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("label")
            var label: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Department(
            @SerializedName("department")
            var department: String = "",
            @SerializedName("id")
            var id: Int = 0
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class Hospital(
        @SerializedName("address")
        var address: String = "",
        @SerializedName("catalogs")
        var catalogs: MutableList<String> = mutableListOf(),
        @SerializedName("city_id")
        var cityId: Int = 0,
        @SerializedName("comment_count")
        var commentCount: Int = 0,
        @SerializedName("content")
        var content: String = "",
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("created_at")
        var createdAt: Int = 0,
        @SerializedName("doctor_count")
        var doctorCount: Int = 0,
        @SerializedName("focus_count")
        var focusCount: Int = 0,
        @SerializedName("has_survey")
        var hasSurvey: Boolean = false,
        @SerializedName("hospital_description")
        var hospitalDescription: String = "",
        @SerializedName("hospital_name")
        var hospitalName: String = "",
        @SerializedName("hospital_short_name")
        var hospitalShortName: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("images")
        var images: MutableList<String> = mutableListOf(),
        @SerializedName("infos")
        var infos: MutableList<Info> = mutableListOf(),
        @SerializedName("is_auth")
        var isAuth: Boolean = false,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("lat")
        var lat: Double = 0.0,
        @SerializedName("leaders")
        var leaders: MutableList<Leader> = mutableListOf(),
        @SerializedName("level")
        var level: Level = Level(),
        @SerializedName("level_id")
        var levelId: Int = 0,
        @SerializedName("lng")
        var lng: Double = 0.0,
        @SerializedName("logo")
        var logo: String = "",
        @SerializedName("other_infos")
        var otherInfos: OtherInfos = OtherInfos(),
        @SerializedName("phone")
        var phone: String = "",
        @SerializedName("province_id")
        var provinceId: Int = 0,
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("technology_ids")
        var technologyIds: String = "",
        @SerializedName("technology_names")
        var technologyNames: MutableList<TechnologyName> = mutableListOf(),
        @SerializedName("type")
        var type: String = "",
        @SerializedName("update_time")
        var updateTime: Int = 0,
        @SerializedName("updated_at")
        var updatedAt: String = ""
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Leader(
            @SerializedName("avatar_file")
            var avatarFile: String = "",
            @SerializedName("doctor_descript")
            var doctorDescript: String = "",
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("hospital_id")
            var hospitalId: Int = 0,
            @SerializedName("is_cancel")
            var isCancel: Int = 0,
            @SerializedName("is_dean")
            var isDean: Int = 0,
            @SerializedName("is_leader")
            var isLeader: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("position")
            var position: String = "",
            @SerializedName("record")
            var record: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Level(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("level_name")
            var levelName: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class OtherInfos(
            @SerializedName("address")
            var address: String = "",
            @SerializedName("mobile")
            var mobile: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class TechnologyName(
            @SerializedName("article_id")
            var articleId: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("technology")
            var technology: String = ""
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class MetaData(
        @SerializedName("meta_description")
        var metaDescription: String = "",
        @SerializedName("meta_keywords")
        var metaKeywords: String = "",
        @SerializedName("meta_title")
        var metaTitle: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class RandomHospital(
        @SerializedName("hospital_name")
        var hospitalName: String = "",
        @SerializedName("hospital_short_name")
        var hospitalShortName: String = "",
        @SerializedName("id")
        var id: Int = 0
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