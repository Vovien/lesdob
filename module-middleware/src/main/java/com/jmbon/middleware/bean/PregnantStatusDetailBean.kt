package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class PregnantStatusDetailBean(
    @SerializedName("adv")
    var adv: Adv = Adv(),
    @SerializedName("articles")
    var articles: MutableList<ArticleList> = mutableListOf(),
    @SerializedName("category")
    var category: Category = Category(),
    @SerializedName("circles")
    var circles: MutableList<Circle> = mutableListOf(),
    @SerializedName("keywords")
    var keywords: MutableList<Keyword> = mutableListOf(),
    @SerializedName("messages")
    var messages: MutableList<Message> = mutableListOf(),
    @SerializedName("others")
    var others: MutableList<Other> = mutableListOf(),
    @SerializedName("questions")
    var questions: MutableList<Question> = mutableListOf(),
    @SerializedName("roll_keywords")
    var rollKeywords: MutableList<RollKeyword> = mutableListOf()
) : Parcelable {
    @Keep
    @Parcelize
    data class Adv(
        @SerializedName("covers")
        var covers: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("tool_id")
        var toolId: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Category(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("list_background")
        var listBackground: String = "",
        @SerializedName("main_title")
        var mainTitle: String = "",
        @SerializedName("search_background")
        var searchBackground: String = "",
        @SerializedName("sub_title")
        var subTitle: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Keyword(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("keyword")
        var keyword: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Message(
        @SerializedName("avatar_file")
        var avatarFile: String = "",
        @SerializedName("create_time")
        var createTime: Long = 0,
        @SerializedName("data")
        var `data`: Data = Data(),
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: Int = 0,//对应类型【1：收藏了文章，2：加入了圈子，3：回答了问题】',
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user_name")
        var userName: String = ""
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Data(
            @SerializedName("answer_id")
            var answerId: Int = 0,
            @SerializedName("covers")
            var covers: String = "",
            @SerializedName("group_id")
            var groupId: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("is_join")
            var isJoin: Boolean = false,
            @SerializedName("item_id")
            var itemId: Int = 0,
            @SerializedName("member_count")
            var memberCount: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("number")
            var number: String = "",
            @SerializedName("question_content")
            var questionContent: String = "",
            @SerializedName("question_count")
            var questionCount: Int = 0,
            @SerializedName("title")
            var title: String = ""
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class Other(
        @SerializedName("bind_category_id")
        var bindCategoryId: Int = 0,
        @SerializedName("covers")
        var covers: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("title")
        var title: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class RollKeyword(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("keyword")
        var keyword: String = ""
    ) : Parcelable
}