package com.tubewiki.mine.bean
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep


/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/10/12
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class ConversationRecordBean(
    val dialog_list: List<ItemRecordBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class ItemRecordBean(
    val answer: String = "",
    val avatar: String = "",
    val create_time: String = "",
    val dialog_id: Int = 0,
    val question: String = ""
) : Parcelable