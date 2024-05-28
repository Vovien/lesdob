package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description: 通用的Banner关联的数据结构
 *
 * Author: jhg
 *
 * Date: 2023/3/14
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class CommonBanner(
    val id: Int = 0,
    val img: String = "",
    val item_id: Int = 0,
    /**
     * Banner类型
     * @see ItemTypeEnum
     */
    val item_type: String = "",
    val origin_title: String = "",
    val url: String = "",
    val remark: String = "",
    val status: Int = 0,
    val create_time: Int = 0,
    val update_time: Int = 0,
    /**
     * 业务层使用, 一般情况下取值和item_id相同, 如果是群聊此值表示群号
     */
    val identity: String = ""
) : Parcelable

@Keep
@Parcelize
data class IdentityInfoBean(
    @SerializedName("group_name")
    val groupName: String = "",
    @SerializedName("need_pregnancy_type")
    val needPregnancyType: Int = 0,
    @SerializedName("page_type")
    val pageType: String = ""
) : Parcelable


enum class ItemTypeEnum(val value: String) {
    /**
     * 专题页
     */
    ITEM_TYPE_COLUMN("column"),

    /**
     * 文章
     */
    ITEM_TYPE_ARTICLE("article"),

    /**
     * Video
     */
    ITEM_TYPE_VIDEO("video"),

    /**
     * 问答
     */
    ITEM_TYPE_QUESTION("question"),

    /**
     * 求助帖
     */
    ITEM_TYPE_HELP("help"),

    /**
     * AI求助
     */
    ITEM_AI_TYPE_HELP("ai_help"),

    /**
     * 群聊
     */
    ITEM_TYPE_GROUP_CHAT("circle"),

    /**
     * 搜索专题
     */
    ITEM_TYPE_SEARCH_SUBJECT("search_subject"),

    /**
     * 网页链接
     */
    ITEM_TYPE_WEB("web"),

    /**
     * toast
     */
    ITEM_TYPE_TOAST("toast"),

    /**
     * 显示获取案例弹窗
     */
    ITEM_TYPE_GET_SCHEME("programme_popup"),

    /**
     * 定制方案弹窗
     */
    ITEM_TYPE_CUSTOMIZED_SOLUTION_POPUP("customized_solution_popup"),

    /**
     * 跳转到微信小程序
     */
    ITEM_TYPE_WECHAT_PAGE("wechat_page"),
}