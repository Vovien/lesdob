package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class RecommendContentBean(
    @SerializedName("covers")
    var covers: String = "",
    @SerializedName("group_id")
    var groupId: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("is_join")
    var isJoin: Boolean = false,
    @SerializedName("number")
    var number: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("views")
    var views: Int = 0,
    @SerializedName("tool_id")
    var toolId: String = "",
    @SerializedName("content_type")
    var content_type: Int = 0,//内容类型【1：文章，2：问题，3：圈子，4：小工具】

) : Parcelable