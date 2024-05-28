package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PageList<T : Parcelable>(
    @SerializedName("list") var list: ArrayList<T> = ArrayList(),
    @SerializedName("page") var page: Int = 0,
    @SerializedName("page_size") var pageSize: Int = 0,
    @SerializedName("total_count") var totalCount: Int = 0,
    @SerializedName("total_page") var totalPage: Int = 0
) : Parcelable