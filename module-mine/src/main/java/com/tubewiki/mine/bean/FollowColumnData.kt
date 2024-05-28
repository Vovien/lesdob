package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FollowColumnData(
    @SerializedName("columns")
    var columns: ArrayList<Column> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("recommends")
    var recommends: ArrayList<Column> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Column(
        @SerializedName("column_description")
        var columnDescription: String = "",
        @SerializedName("column_id")
        var columnId: Int = 0,
        @SerializedName("column_name")
        var columnName: String = "",

        @SerializedName("article_count")
        var articleCount: Int = 0,
        @SerializedName("column_pic")
        var columnPic: String = ""
    ) : Parcelable
}