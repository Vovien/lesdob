package com.jmbon.middleware.bean.js

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 文章详情广告
 * @author MilkCoder
 * @date 2023/7/31
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class ArticleBodyAdv(
    val index: Int = 0,
    @SerializedName("data")
    val list: List<ArticleAdv> = listOf()
) : Parcelable

@Keep
@Parcelize
data class ArticleAdv(
    @SerializedName("cost")
    val cost: Int = 0,
    @SerializedName("cost_title")
    val costTitle: String = "",
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("cover_title")
    val coverTitle: String = "",
    @SerializedName("disease_name")
    val diseaseName: String = "",
    @SerializedName("disease_name_title")
    val diseaseNameTitle: String = "",
    @SerializedName("experience_id")
    val experienceId: Int = 0,
    @SerializedName("hospital_name")
    val hospitalName: String = "",
    @SerializedName("hospital_name_title")
    val hospitalNameTitle: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
) : Parcelable