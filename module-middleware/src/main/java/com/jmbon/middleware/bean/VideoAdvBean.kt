package com.jmbon.middleware.bean
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * 视频悬浮广告
 * @author MilkCoder
 * @date 2023/9/12
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class VideoAdvBean(
    @SerializedName("code")
    val code: Int = 0,
    val data: List<VideoAdv> = listOf(),
    @SerializedName("msg")
    val msg: String = ""
) : Parcelable

@Keep
@Parcelize
data class VideoAdv(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable