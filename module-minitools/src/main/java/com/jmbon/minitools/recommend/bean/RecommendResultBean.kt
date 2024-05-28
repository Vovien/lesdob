package com.jmbon.minitools.recommend.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 推荐结果
 *
 * Author: jhg
 *
 * Date: 2023/6/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class RecommendResultBean(
    val originality_list: ArrayList<ItemRecommendBean> = arrayListOf(),
    val avatar_file:String = "",
    /**
     * 引导类型: 1=经验贴, 2=新引导页
     */
    val guide_page_type: Int = 1,
    val guide_page_id: Int = 0,
    val guide_page_img: String = "",
    val guide_button_list : ArrayList<GuideButtonStyleItemBean> = arrayListOf()
) : Parcelable

@Keep
@Parcelize
data class ItemRecommendBean(
    val id: Int = 0,
    val experience_id: Int = 0,
    val cover: String = "",
    val title: String = "",
    val cost: Int = 0,
    val disease_name: String = "",
    val hospital_name: String = ""
) : Parcelable

@Keep
@Parcelize
data class GuideButtonStyleItemBean(
    val title: String = "",
    val bg_color: String = "",
    val item_type: String = "",
    val identity: String = ""
) : Parcelable