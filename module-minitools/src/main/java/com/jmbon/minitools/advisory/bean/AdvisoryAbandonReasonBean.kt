package com.jmbon.minitools.advisory.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/5/8
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class AdvisoryAbandonReasonBean(
    val reason_list: List<ItemReasonBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class ItemReasonBean(
    val id: Int = 0,
    val title: String = "",
    var isSelected: Boolean = false,
) : Parcelable