package com.tubewiki.mine.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.jmbon.middleware.bean.ContentBean
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 我的收藏
 *
 * Author: jhg
 *
 * Date: 2023/10/29
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class CollectionBean(
    val collect_list: List<ContentBean>? = listOf()
) : Parcelable