package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FollowTopicsData(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("datas")
    var datas: ArrayList<TopicBean> = arrayListOf(),
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable
