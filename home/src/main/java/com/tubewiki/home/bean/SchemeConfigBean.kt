package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 方案配置信息
 *
 * Author: jhg
 *
 * Date: 2023/6/15
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class SchemeConfigBean(
    val title_img: String = "",
    val bg_img:String = "",
    val content_list: List<String>? = listOf(),
    val wechat: String = "",
    val qrcode: String = "",
    val button_word: String = "",
) : Parcelable {
    companion object {
        /**
         * 案例
         */
        const val SCHEME_TYPE_CASE = 1

        /**
         * 方案
         */
        const val SCHEME_TYPE_SCHEME = 2
    }
}