package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class ArticleDetails(
    @SerializedName("article")
    var article: Article = Article(),

//    @SerializedName("medicines")
//    var medicines: List<String> = listOf(),

    @SerializedName("next_article")
    var nextArticle: NextArticle = NextArticle(),
    @SerializedName("prev_article")
    var prevArticle: NextArticle = NextArticle(),
//    @SerializedName("relate_articles")
//    var relateArticles: ArrayList<ArticleList> = arrayListOf(),
    @SerializedName("relate_columns")
    var relateColumns: ArrayList<ArticleColumn> = arrayListOf(),
//    @SerializedName("relate_questions")
//    var relateQuestions: ArrayList<RelateQuestion> = arrayListOf(),
    @SerializedName("others")
    var others: ArrayList<PregnantStatusDetailBean.Other> = arrayListOf(),
    @SerializedName("invitation_content")
    var invitationContent: InvitationContentBean? = null,
    @SerializedName("recommend_content")
    var recommendContent: ArrayList<RecommendContentBean> = arrayListOf(),
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Article(
        var webHeight: Int = 0,
        @SerializedName("abstract")
        var abstracts: String? = "",
        @SerializedName("risk_type")
        var riskType: Int = 0,//风险类型：1争议  2谣言  3广告  4 摘自
        @SerializedName("risk_description")
        var riskDescription: String = "",
        @SerializedName("agree_label")
        var agreeLabel: String = "", //id、id2等人赞同了该回答
        @SerializedName("add_time")
        var addTime: Long = 0,
        @SerializedName("catalogs")
        var catalogs: List<String> = listOf(),
        @SerializedName("category")
        var category: CategoryList = CategoryList(),
        @SerializedName("category_id")
        var categoryId: Int = 0,
        @SerializedName("collect_count")
        var collectCount: Int = 0,
        @SerializedName("column")
        var column: ArticleColumn = ArticleColumn(),
        @SerializedName("column_id")
        var columnId: Int = 0,
        @SerializedName("comments")
        var comments: Int = 0,
        @SerializedName("content")
        var content: String? = "",
        @SerializedName("content_html")
        var contentHtml: String? = "",
        @SerializedName("hospital_ids")
        var hospitalIds: String? = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("image")
        var image: String = "",
        @SerializedName("infos")
        var infos: ArrayList<ArticleInfo> = arrayListOf(),
        @SerializedName("is_collect")
        var isCollect: Boolean = false,
        @SerializedName("is_del")
        var isDel: Int = 0,
        //视频审核状态
        @SerializedName("audit_status")
        var auditStatus: Int = 1,  //0 未审核 ，1 审核完成 （通过或者不通过）

        @SerializedName("is_given")
        var isGiven: Boolean = false,
        @SerializedName("open_reward")
        var openReward: Int = 0,
        @SerializedName("references")
        var references: ArrayList<Reference> = arrayListOf(),
        @SerializedName("images")
        var images: ArrayList<String> = arrayListOf(),
        @SerializedName("rewards")
        var rewards: ArrayList<User> = arrayListOf(),
        @SerializedName("seo_description")
        var seoDescription: String? = "",
        @SerializedName("seo_keyword")
        var seoKeyword: String? = "",
        @SerializedName("seo_title")
        var seoTitle: String? = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("custom_title")
        var customTitle: String = "",
        @SerializedName("topics")
        var topics: ArrayList<Topic> = arrayListOf(),
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user")
        var user: User = User(),
        @SerializedName("views")
        var views: Int = 0,
        @SerializedName("votes")
        var votes: Int = 0,
        @SerializedName("lock")
        var lock: Int = 0,// 0  未锁定  1：已锁定
        @SerializedName("binding_circle")
        var bindCircle: BindCircle? = null,
        @SerializedName("binding_articles")
        var bindArticle: ArrayList<Article>? = arrayListOf(),
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class NextArticle(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("title")
        var title: String? = ""
    ) : Parcelable


    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class RelateQuestion(
        @SerializedName("category")
        var category: CategoryList = CategoryList(),
        @SerializedName("category_id")
        var categoryId: Int = 0,
        @SerializedName("covers")
        var covers: String? = "",
        @SerializedName("published_uid")
        var publishedUid: Int = 0,
        @SerializedName("question_content")
        var questionContent: String? = "",
        @SerializedName("question_id")
        var questionId: Int = 0,
        @SerializedName("topics")
        var topics: ArrayList<Topic> = arrayListOf(),
        @SerializedName("user")
        var user: User = User()
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Topic(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("topic_id")
        var topicId: Int = 0,
        @SerializedName("topic_title")
        var topicTitle: String = ""
    ) : Parcelable
}

@SuppressLint("ParcelCreator")
@kotlinx.parcelize.Parcelize
@Keep
data class ArticleInfo(
    @SerializedName("info_id")
    var infoId: Int? = 0,
    @SerializedName("key")
    var key: String? = "",
    @SerializedName("value")
    var value: String? = ""
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class Reference(
    @SerializedName("articlename")
    var articlename: String? = "",
    @SerializedName("authorname")
    var authorname: String? = "",
    @SerializedName("publisheddate")
    var publisheddate: String? = "",
    @SerializedName("referencedate")
    var referencedate: String? = "",
    @SerializedName("publisheddate2")
    var publisheddate2: String? = "",
    @SerializedName("referencedate2")
    var referencedate2: String? = "",
    @SerializedName("type")
    var type: Int? = 0,
    @SerializedName("webname")
    var webname: String? = "",
    @SerializedName("pressname")
    var pressname: String? = "",
    @SerializedName("workname")
    var workname: String? = "",
    @SerializedName("weburl")
    var weburl: String? = ""
) : Parcelable


@Keep
@Parcelize
data class BindCircle(
    @SerializedName("covers") var covers: String = "",
    @SerializedName("group_id") var groupId: Int = 0,
    @SerializedName("id") var id: Int = 0,
    @SerializedName("is_join") var isJoin: Boolean = false,
    @SerializedName("member_count") var memberCount: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("number") var number: String = "",
    @SerializedName("question_count") var questionCount: Int = 0,
    @SerializedName("description") var description: String = ""
) : Parcelable

fun ArticleDetails.Article.getShareTitle(): String {
    return if (customTitle.isNullOrEmpty()) title else customTitle
}
