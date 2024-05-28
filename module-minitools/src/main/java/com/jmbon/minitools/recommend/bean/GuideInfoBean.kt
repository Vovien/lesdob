package com.jmbon.minitools.recommend.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 引导信息
 *
 * Author: jhg
 *
 * Date: 2023/9/7
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class GuideInfoBean(
    val guide_page_id: Int = 0,
    val guide_page_type: Int = 0,
    val guide_page_img: String = "",
    val guide_button_list: ArrayList<GuideButtonStyleItemBean>? = arrayListOf()
) : Parcelable