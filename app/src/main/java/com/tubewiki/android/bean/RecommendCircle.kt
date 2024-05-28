package com.tubewiki.android.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.CircleData
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RecommendCircle(
    @SerializedName("list") var list: ArrayList<CircleData>? = arrayListOf(),
    @SerializedName("page") var page: Int = 0,
    @SerializedName("page_size") var pageSize: Int = 0,
    @SerializedName("total_count") var totalCount: Int = 0,
    @SerializedName("total_page") var totalPage: Int = 0
) : Parcelable