package com.jmbon.middleware.bean.js

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 经验正文广告
 * @author MilkCoder
 * @date 2023/7/28
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class ExperienceBodyAdv(
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("qrcode")
    val qrcode: String = "",
    @SerializedName("qrcode_title")
    val qrcodeTitle: String = "",
    @SerializedName("statement")
    val statement: String = "",
    @SerializedName("sub_title1")
    val subTitle1: String = "",
    @SerializedName("sub_title2")
    val subTitle2: String = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable