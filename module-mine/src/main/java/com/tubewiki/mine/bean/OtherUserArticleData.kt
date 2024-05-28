package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class OtherUserArticleData(
    @SerializedName("articles")
    var articles: ArrayList<CollectionArticleData.Data> = arrayListOf(),
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable