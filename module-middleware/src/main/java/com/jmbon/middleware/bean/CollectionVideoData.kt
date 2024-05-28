package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import com.jmbon.middleware.bean.ArticleInfo
import com.jmbon.middleware.bean.VideoDetail
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class CollectionVideoData(
    @SerializedName("datas")
    var datas: MutableList<VideoDetail.VideoData> = mutableListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable
