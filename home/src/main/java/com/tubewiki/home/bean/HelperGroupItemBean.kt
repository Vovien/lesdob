package com.tubewiki.home.bean
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * 好孕求助数据
 * @author MilkCoder
 * @date 2023/9/11
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class HelperGroupItemBean(
    @SerializedName("code")
    val code: Int = 0,
    val data: List<GroupItem> = listOf(),
    @SerializedName("msg")
    val msg: String = ""
) : Parcelable

@Parcelize
data class GroupItem(
    @SerializedName("avatars")
    val avatars: List<String> = listOf(),
    @SerializedName("color")
    val color: String = "",
    @SerializedName("group_nickname")
    val groupNickname: String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("txt")
    val txt: String = ""
) : Parcelable