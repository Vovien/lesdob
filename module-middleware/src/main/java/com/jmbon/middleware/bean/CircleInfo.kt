package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CircleInfo(
    @SerializedName("circle")
    var circle: Circle = Circle()
) : Parcelable {
    @Keep
    @Parcelize
    data class Circle(
        @SerializedName("browses")
        var browses: ArrayList<Browse> = arrayListOf(),
        @SerializedName("covers")
        var covers: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("doctors")
        var doctors: ArrayList<Doctor> = arrayListOf(),
        @SerializedName("id")
        var id: String = "",
        @SerializedName("number")
        var number: String = "",
        @SerializedName("is_join")
        var isJoin: Boolean = false,
        @SerializedName("member_count")
        var memberCount: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("question_count")
        var questionCount: Int = 0,
        @SerializedName("questions")
        var questions: ArrayList<Question> = arrayListOf()
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Browse(
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("title")
            var title: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Doctor(
            @SerializedName("avatar_file")
            var avatarFile: String = "",
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("is_cancel")
            var isCancel: Int = 0, //1 注销
        ) : Parcelable

        @Keep
        @Parcelize
        data class Question(
            @SerializedName("discuss_count")
            var discussCount: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("title")
            var title: String = "",
            @SerializedName("users")
            var users: ArrayList<User> = arrayListOf()
        ) : Parcelable {
            @Keep
            @Parcelize
            data class User(
                @SerializedName("avatar_file")
                var avatarFile: String = "",
                @SerializedName("id")
                var id: Int = 0,
                @SerializedName("question_id")
                var questionId: Int = 0,
                @SerializedName("user_name")
                var userName: String = ""
            ) : Parcelable
        }
    }
}
