package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class MenstrualData(
    @SerializedName("sub_title")
    var sub_title: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("type")
    var type: Int = 0 //类型【1：姨妈期，2：距离姨妈期还有多少天，3：易孕期与排卵期，4：未设置经期（不显示）】
) : Parcelable