package com.jmbon.minitools.content.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description: 好孕互助群数据结构
 *
 * Author: jhg
 *
 * Date: 2023/9/8
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class HelpGroupBean(
    val circle_list: List<HelpGroupItemBean>? = listOf(),
    val group_type_title: String = ""
) : Parcelable

@Keep
@Parcelize
data class HelpGroupItemBean(
    val id: Int = 0,
    val name: String = "",
    val cover: String = "",
    val txt: String = "",
    val color: String = "",
    val item_type: String = "",
    val identity: String = "",
    val member_avatar: List<String>? = listOf()
) : Parcelable