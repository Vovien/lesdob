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
data class AdvisoryReplyBean(
    val list: List<AdvisoryItemForm> = listOf(),
    val dialog_id: Int = 0
) : Parcelable