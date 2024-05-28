package com.tubewiki.home.bean
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * 常见问题广告
 * @author MilkCoder
 * @date 2023/8/7
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */

@Keep
@Parcelize
data class CommonQuestionAdvBean(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("adv_list")
    var advList: ArrayList<CommonQuestionAdv> = arrayListOf(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable

@Keep
@Parcelize
data class CommonQuestionAdv(
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
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable{
    val costStr: String
        get() = if (cost >= 5000) {
            String.format("%.1f万", cost / 10000f)
        } else {
            "$cost 元"
        }
}