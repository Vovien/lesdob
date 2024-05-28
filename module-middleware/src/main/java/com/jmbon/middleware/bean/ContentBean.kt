package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/10/23
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class ContentBean(
    val cover: String = "",
    val content: String = "",
    val title: String = "",
    val id: Int = 0,
    val read_num: Int = 0,
) : Parcelable