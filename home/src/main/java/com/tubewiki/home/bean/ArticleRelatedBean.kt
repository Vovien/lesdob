package com.tubewiki.home.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class ArticleRelatedBean(
    @SerializedName("indexInfo")
    var indexInfo: IndexInfo = IndexInfo(),
    @SerializedName("indexList")
    var indexList: MutableList<Index> = mutableListOf()
) : Parcelable {
    @Keep
    @Parcelize
    data class IndexInfo(
        @SerializedName("articleNum")
        var articleNum: Int = 0,
        @SerializedName("fixed")
        var fixed: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("indexName")
        var indexName: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Index(
        @SerializedName("articleId")
        var articleId: Int = 0,
        @SerializedName("articleTitle")
        var articleTitle: String = "",
        @SerializedName("isFixed")
        var isFixed: Boolean = false
    ) : Parcelable
}