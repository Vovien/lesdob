package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * @author : leimg
 * time   : 2022/8/2
 * desc   :
 * version: 1.0
 */
@Keep
@Parcelize
data class TubeArticleDetail(
    val article_detail: ArticleInfoBean? = ArticleInfoBean(),
    val circles: List<CircleInfoBean>? = listOf(),
    val tags: List<Tag>? = listOf(),
    val collect_status: Int = 0,
) : Parcelable

@Keep
@Parcelize
data class ArticleInfoBean(
    val content: String = "",
    val id: Int = 0,
    val reference: List<ReferenceInfoBean>? = listOf(),
    val title: String = "",
    val left_button: ButtonInfoBean? = null,
    val right_button: ButtonInfoBean? = null,
) : Parcelable

@Keep
@Parcelize
data class ButtonInfoBean(
    val title: String = "",
    val subscript: String = "",
    val group_name: String = "",
    val item_type: String = "",
    val identity: String = ""
) : Parcelable

@Keep
@Parcelize
data class ReferenceInfoBean(
    val title: String = "",
    val authorname: String = "",
    val pressname: String = "",
    val referenceDate: String = "",
    val releaseDate: String = "",
    val showValue: Int = 0,
    val type: Int = 0,
    val workname: String = ""
) : Parcelable

@Keep
@Parcelize
data class Tag(
    val id: Int = 0,
    val tag: String = ""
) : Parcelable

@Keep
@Parcelize
data class CircleInfoBean(
    val id: Int = 0,
    val name: String = "",
    val cover: String = "",
    val member_count: String = "",
    val assistant: String = "",
    val description: String = "",
    val item_type: String = "",
    val identity: String = "",
) : Parcelable