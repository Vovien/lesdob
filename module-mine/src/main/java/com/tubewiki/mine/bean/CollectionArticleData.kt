package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import com.jmbon.middleware.bean.ArticleInfo
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class CollectionArticleData(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("datas")
    var datas: ArrayList<Data> = arrayListOf(),
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Data(
        @SerializedName("abstract")
        var abstract: String = "",
        @SerializedName("add_time")
        var addTime: Long = 0,
        @SerializedName("column")
        var column: Column = Column(),
        @SerializedName("column_id")
        var columnId: Int = 0,
        @SerializedName("comments")
        var comments: Int = 0,
        @SerializedName("content")
        var content: String = "",
        @SerializedName("content_html")
        var contentHtml: String = "",
        @SerializedName("given_count")
        var givenCount: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("image")
        var image: String = "",
        @SerializedName("is_read")
        var isRead: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("infos")
        var infos: ArrayList<ArticleInfo> = arrayListOf(),
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user")
        var user: User = User(),
        @SerializedName("views")
        var views: Int = 0
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class Column(
            @SerializedName("column_id")
            var columnId: Int = 0,
            @SerializedName("column_name")
            var columnName: String = ""
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class User(
            @SerializedName("avatar_file")
            var avatarFile: String = "",
            @SerializedName("doctor_id")
            var doctorId: Int = 0,
            @SerializedName("is_auth")
            var isAuth: Boolean = false,
            @SerializedName("job_name")
            var jobName: String = "",
            @SerializedName("uid")
            var uid: Int = 0,
            @SerializedName("user_name")
            var userName: String = ""
        ) : Parcelable
    }
}