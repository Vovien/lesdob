package com.jmbon.minitools.advisory.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 咨询会话历史
 *
 * Author: jhg
 *
 * Date: 2023/5/18
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class AdvisoryConversationHistoryBean(
    val list: List<AdvisoryItemForm>? = listOf()
) : Parcelable

