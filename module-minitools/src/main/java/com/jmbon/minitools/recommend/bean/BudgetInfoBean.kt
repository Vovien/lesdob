package com.jmbon.minitools.recommend.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 试管预算信息
 *
 * Author: jhg
 *
 * Date: 2023/6/27
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class BudgetInfoBean(
    val tube_desc: String = "",
    val tube_list: ArrayList<ItemBudgetBean>? = arrayListOf(),
    val insemination_desc: String = "",
    val insemination_list: ArrayList<ItemBudgetBean>? = arrayListOf()
) : Parcelable

@Keep
@Parcelize
data class ItemBudgetBean(
    val title: Int = 0,
    val value: Float = 0f
) : Parcelable