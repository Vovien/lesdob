package com.tubewiki.home.bean
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
data class QuestionCategoryBean(
    val question_list: List<QuestionItemRecordBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class QuestionItemRecordBean(
    val question_id: Int = 0,
    val question: String = ""
) : Parcelable