package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class CircleCover(
    @SerializedName("circle_info") var circleInfo: CircleInfo = CircleInfo()
) : Parcelable {
    @Keep
    @Parcelize
    data class CircleInfo(
        @SerializedName("circle_id") var circleId: Int = 0,
        @SerializedName("covers") var covers: String = "",
        @SerializedName("description") var description: String = "",
        @SerializedName("doctor_browse_list") var doctorBrowseList: ArrayList<DoctorBrowse> = arrayListOf(),
        @SerializedName("doctor_list") var doctorList: ArrayList<Doctor> = arrayListOf(),
        @SerializedName("is_join") var isJoin: Int = 0,
        @SerializedName("member_count") var memberCount: Int = 0,
        @SerializedName("name") var name: String = "",
        @SerializedName("number") var number: String = ""
    ) : Parcelable {
        @Keep
        @Parcelize
        data class DoctorBrowse(
            @SerializedName("name") var name: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Doctor(
            @SerializedName("avatar_file") var avatarFile: String = "",
            @SerializedName("name") var name: String = "",
            @SerializedName("position") var position: String = "",
            @SerializedName("uid") var uid: Int = 0
        ) : Parcelable
    }
}