package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.jmbon.middleware.bean.ContentBean
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 专题文章
 *
 * Author: jhg
 *
 * Date: 2023/10/27
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class SubjectArticleBean(
    val article_list: List<ContentBean>? = listOf()
) : Parcelable