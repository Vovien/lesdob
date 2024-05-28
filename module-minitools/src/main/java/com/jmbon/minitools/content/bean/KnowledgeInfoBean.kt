package com.jmbon.minitools.content.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.jmbon.middleware.bean.ContentBean
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/10/24
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class KnowledgeInfoBean(
    val article_list: List<ContentBean>? = null,
    val card: HeaderInfoBean? = null,
): Parcelable

@Keep
@Parcelize
data class HeaderInfoBean (
    val bg_img: String,
    val button_txt: String,
    val person_img: String,
    val text_list: List<String>? = null,
    val title_img: String,
    val item_type: String = "",
    val identity: String = "",
    val rand_user: List<RandomUserBean>? = null,
): Parcelable

@Keep
@Parcelize
data class RandomUserBean (
    val avatar_file: String,
    val user_name: String
): Parcelable