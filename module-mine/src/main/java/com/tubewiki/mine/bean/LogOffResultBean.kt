package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class LogOffResultBean(
    @SerializedName("description")
    var description: String = "",
    @SerializedName("type")
    var type: Int = 0,//    type: '对应返回类型：200：成功，883:未达到注销条件,884:注意'
    @SerializedName("title")
    var title: String = ""
) : Parcelable