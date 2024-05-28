package com.tubewiki.home.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class ColumnArticlesData(
    @SerializedName("articles")
    var articles: ArrayList<TubeArticleDetail> = arrayListOf(),
    @SerializedName("topic")
    var topic: Topic = Topic(),
    @SerializedName("person_num")
    var personNum: Int = 0,
    @SerializedName("person_copywriting")
    var personCopywriting: String = "",
    /**
     * 按钮配置
     */
    val detail_button: DetailButtonBean? = null,
) : Parcelable {
    @Keep
    @Parcelize
    data class Topic(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("customTitle")
        var customTitle: String = "",
        @SerializedName("customDescription")
        var customDescription: String = "",
        @SerializedName("indexCover")
        var indexCover: String = "",
        @SerializedName("icon")
        var icon: String = "",
        @SerializedName("topicName")
        var topicName: String = "",
        @SerializedName("cover")
        var cover: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("articleNum")
        var articleNum: String = "",
    ) : Parcelable

    @Keep
    @Parcelize
    data class DetailButtonBean(
        val title: String = "",
        val label: String = "",
        val item_type: String = "",
        val identity: String = ""
    ) : Parcelable
}

