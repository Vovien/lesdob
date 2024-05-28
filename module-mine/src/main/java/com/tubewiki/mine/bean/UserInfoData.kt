package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class UserInfoData(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("user")
    var user: User = User()
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class User(
        @SerializedName("article_edit_type")
        var articleEditType: Int = 0,//0 普通用户， 1 || 2  内部编辑人员
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("city_id")
        var cityId: Int = 0,
        @SerializedName("city_name")
        var cityName: String = "",
        @SerializedName("collect_count")
        var collectCount: Int = 0,
        @SerializedName("description")
        var description: String = "",
        @SerializedName("doctor")
        var doctor: Doctor = Doctor(),
        @SerializedName("fans_count")
        var fansCount: Int = 0,
        @SerializedName("focus_count")
        var focusCount: Int = 0,
        @SerializedName("given_count")
        var givenCount: Int = 0,
        @SerializedName("has_article")
        var hasArticle: Boolean = false,
        @SerializedName("has_video")
        var hasVideo: Boolean = false,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("is_mutual_focus")
        var isMutualFocus: Boolean = false,
        @SerializedName("pregnant_status")
        var pregnantStatus: Int = 0,
        @SerializedName("sex")
        var sex: Int = 0,
        @SerializedName("sex_show")
        var sexShow: Int = 0,
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user_name")
        var userName: String = "",
        @SerializedName("background")
        var background: String = "",
        @SerializedName("category_title")
        var categoryTitle: String = "",

        @SerializedName("job_type")
        var jobType: Int = 1,//1：普通用户，2：优秀回答者，3：优秀创作者

        @SerializedName("verify_type")
        var verifyType: Int = 0,
        @SerializedName("is_cancel")
        var isCancel: Int = 0, //1 注销
    ) : Parcelable{
        @Keep
        @Parcelize
        data class Doctor(
            @SerializedName("department")
            var department: String = "",
            @SerializedName("department_id")
            var departmentId: Int = 0,
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("hospital_id")
            var hospitalId: Int = 0,
            @SerializedName("hospital_name")
            var hospitalName: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("position")
            var position: String = "",
            @SerializedName("uid")
            var uid: Int = 0
        ) : Parcelable
    }
}
