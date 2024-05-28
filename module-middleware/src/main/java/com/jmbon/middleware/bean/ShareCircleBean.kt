package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class ShareCircleBean(
    @SerializedName("circles")
    var circles: MutableList<Circle> = mutableListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @Keep
    @Parcelize
    data class Circle(
        @SerializedName("covers")
        var covers: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("group_id")
        var groupId: Int = 0,
        @SerializedName("number")
        var number: String = "",
        @SerializedName("member_count")
        var memberCount: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("question_count")
        var questionCount: Int = 0
    ) : Parcelable
}