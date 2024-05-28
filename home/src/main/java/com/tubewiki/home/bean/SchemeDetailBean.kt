package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 方案详情
 *
 * Author: jhg
 *
 * Date: 2023/6/14
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class SchemeDetailBean(
    val case_list: List<SchemeItemBean>? = listOf(),
    val banner: String = "",
    val step: List<Step>? = listOf(),
    val bottom_title: String = "",
    val desc: String = ""
) : Parcelable

@Keep
@Parcelize
data class SchemeItemBean(
    val id: Int = 0,
    val experience_id: Int = 0,
    val cover: String = "",
    val title: String = "",
    val cost: Int = 0,
    val disease_name: String = "",
    val hospital_name: String = "",
    val thank: String = "",
    val tags: List<String>? = listOf(),
) : Parcelable

@Keep
@Parcelize
data class Step(
    val title: String = "",
    val icon: String = ""
) : Parcelable