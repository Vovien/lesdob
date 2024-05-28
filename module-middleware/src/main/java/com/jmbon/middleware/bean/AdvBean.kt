package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class AdvBean(
    @SerializedName("add_time")
    var addTime: Int = 0,
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("content_type")
    var contentType: Int = 0,
    @SerializedName("covers")
    var covers: String = "",
    @SerializedName("create_time")
    var createTime: Int = 0,
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("is_del")
    var isDel: Int = 0,
    @SerializedName("is_join")
    var isJoin: Boolean = false,
    @SerializedName("item_id")
    var itemId: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("number")
    var number: String = "",
    @SerializedName("originality")
    var originality: String = "",
    @SerializedName("resources")
    var resources: ArrayList<ResourceBean> = arrayListOf(),
    @SerializedName("title")
    var title: String = "",
    @SerializedName("tool_id")
    var toolId: String = "",
    @SerializedName("tool_type")
    var toolType: Int = 0,
    @SerializedName("type")
    var type: Int = 0,
  @SerializedName("resource_type")
    var resourceType: Int = 0, //广告显示内容类型【1：图片，2：视频】

    @SerializedName("update_time")
    var updateTime: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("views")
    var views: String = "",
    @SerializedName("user")
    var user: User = User()
) : Parcelable