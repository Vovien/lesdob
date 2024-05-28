package com.jmbon.middleware.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class Article(
    @SerializedName("articles")
    var articles: ArrayList<ArticleList> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("index")
    var index: Int = 0
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ArticleList(

    @SerializedName("abstract")
    var abstracts: String? = "",
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("add_time",alternate = ["create_time"])
    var addTime: Long = 0,
    @SerializedName("column")
    var column: ArticleColumn = ArticleColumn(),
    @SerializedName("column_id")
    var columnId: Int = 0,
    @SerializedName("comments")
    var comments: Int = 0,
    @SerializedName("content")
    var content: String? = "",
    @SerializedName("given_count")
    var givenCount: Int = 0,
    @SerializedName("votes")
    var votes: Int = 0,
    @SerializedName("is_collect")
    var isCollect: Boolean = false,
    @SerializedName("is_given")
    var isGiven: Boolean = false,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image")
    var image: String = "",
    @SerializedName("images")
    var imageList: ArrayList<String> = arrayListOf(),
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("custom_title")
    var customTitle: String? = "",
    @SerializedName("uid")
    var uid: Int = 0,
    @SerializedName("user")
    var user: User = User(),
    @SerializedName("content_html")
    var contentHtml: String? = "",
    @SerializedName("views")
    var views: Int = 0,
    @SerializedName("title_highlight")
    var highlight: ArrayList<String> = arrayListOf(),
    @SerializedName("infos")
    var infos: ArrayList<ArticleInfo> = arrayListOf(),


    // 下面是圈子需要的数据
    @SerializedName("type")
    var type: String = "",
    @SerializedName("users")
    var users: ArrayList<User> = arrayListOf(),
    @SerializedName("circle_name")
    var circleName: String = "",
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("discuss_count")
    var discussCount: Int = 0,

    @SerializedName("answer_count")
    var answerCount: Int = 0,

    @SerializedName("column_name")
    var columnName: String = "",
    @SerializedName("avatar_file")
    var avatarFile: String = "",
    @SerializedName("user_name")
    var userName: String = "",
    @SerializedName("is_cancel")
    var isCancel: Int = 0, //1 注销
) : Parcelable, MultiItemEntity {
    override val itemType: Int
        get() = 1
}

@SuppressLint("ParcelCreator")
@Parcelize
data class ArticleColumn(
    @SerializedName("article_count")
    var articleCount: Int = 0,
    @SerializedName("column_description")
    var columnDescription: String? = "",
    @SerializedName("column_id")
    var columnId: Int = 0,
    @SerializedName("column_name")
    var columnName: String? = "",
    @SerializedName("column_pic")
    var columnPic: String? = "",
    @SerializedName("focus_count")
    var focusCount: Int = 0,
    @SerializedName("browses")
    var browses: Int = 0,
    @SerializedName("is_verify")
    var isVerify: Int = 0,
    @SerializedName("is_focus")
    var isFocus: Boolean = false
) : Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class ColumnDetails(
    @SerializedName("column")
    var column: ArticleColumn = ArticleColumn(),
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class SearchArticle(
    @SerializedName("columns")
    var columns: ArrayList<ArticleColumn> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable

fun convertToArticleList(abstracts:String,addTime:Long,comments:Int,content:String,
                         id:Int,infos: ArrayList<ArticleInfo>,title:String
): ArticleList {
    val articleList = ArticleList()
    articleList.abstracts = abstracts
    articleList.addTime = addTime
    articleList.comments = comments
    articleList.contentHtml = content
    articleList.id = id
    articleList.infos = infos
    articleList.title = title
    return articleList
}