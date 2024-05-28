package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class FollowUserData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("recommends")
    var recommends: ArrayList<User> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("users")
    var users: ArrayList<User> = arrayListOf(),
    @SerializedName("doctors")
    var doctors: ArrayList<User> = arrayListOf()
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class User(
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("category_title")
        var categoryTitle: String = "",
        @SerializedName("doctor_id")
        var doctorId: Int = 0,
        @SerializedName("is_auth")
        var isAuth: Boolean = false,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("is_mutual_focus")
        var isMutualFocus: Boolean = false,
        @SerializedName("job_name")
        var jobName: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("hospital_name")
        var hospital_name: String = "",
        @SerializedName("department")
        var department: String = "",
        @SerializedName("notify_message")
        var notifyMessage: Int = 0,
        @SerializedName("open_reward")
        var openReward: Int = 0,
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("is_cancel")
        var isCancel: Int = 0, //1 注销
        @SerializedName("user_name")
        var userName: String = "",
        @SerializedName("verify_type")
        var verifyType: Int = 0
    ) : Parcelable

}