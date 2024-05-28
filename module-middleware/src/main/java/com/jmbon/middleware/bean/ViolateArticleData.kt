package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class ViolateArticleData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("article")
    var article: ArticleDetails.Article = ArticleDetails.Article(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable

