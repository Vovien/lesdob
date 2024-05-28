package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.jmbon.middleware.bean.CircleData
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/9/20
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class HomeHeaderBean(
    val app_title: String = "",
    val bank_title: String = "",
    val bank_list: List<Bank>? = listOf(),
    val topic_list: List<TopicCategoryBean>? = listOf(),
    val circle_list: List<CircleData>? = listOf(),
    val session_selected_circle: List<CircleData>? = listOf(),
) : Parcelable

@Keep
@Parcelize
data class Bank(
    val topic_id: Int = 0,
    val icon: String = "",
    val en_name: String = "",
    val zh_name: String = ""
) : Parcelable

@Keep
@Parcelize
data class TopicCategoryBean(
    val icon: String = "",
    val name: String = "",
    val topic_id: Int = 0
) : Parcelable