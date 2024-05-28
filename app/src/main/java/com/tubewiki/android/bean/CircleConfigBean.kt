package com.tubewiki.android.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description: 圈子配置信息
 *
 * Author: jhg
 *
 * Date: 2023/9/20
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class CircleConfigBean(
    val circle_list: List<ItemCircleBean>? = listOf(),
    val selected_group_type: List<CircleItemConfigBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class CircleItemConfigBean(
    val group_type: Int = 0,
    val title: String = "",
    val sub_title: String = "",
    val icon: String = ""
) : Parcelable

@Keep
@Parcelize
data class ItemCircleBean(
    @SerializedName("assistant")
    val assistant: String = "",
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("member_avatar")
    val memberAvatar: List<String>? = listOf(),
    @SerializedName("member_count")
    val memberCount: Int = 0,
    @SerializedName("name")
    val name: String = ""
): Parcelable

