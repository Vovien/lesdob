package com.jmbon.minitools.recommend.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.chad.library.adapter.base.entity.SectionEntity
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 选择表单数据结构
 *
 * Author: jhg
 *
 * Date: 2023/6/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class ChooseFormBean(
    val form_data: List<ItemTypeBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class ItemTypeBean(
    val title: String = "",
    val key: String = "",
    val value: Int = 0,
    val form: ArrayList<ItemFormBean>? = arrayListOf(),
    val last_button_word: String = "",
) : Parcelable

@Keep
@Parcelize
data class ItemFormBean(
    val title: String = "",
    /**
     * 表单类型: address：地址，multiple_list:多选列表，single_list：单选列表，cost_map：花费柱状图
     * @see FormTypeEnum
     */
    val type: String = "",
    /**
     * 提交表单时的可以
     */
    val key: String = "",
    /**
     * 是否跳过下一个表单, 1=跳过
     */
    var skip_status: Int = 0,
    /**
     * 多选选项
     */
    val multiple_list: List<MultipleBean>? = listOf(),
    /**
     * 单选选项
     */
    val single_list: List<SingleBean>? = listOf(),
    /**
     * 预算柱状图
     */
    val cost_map: List<Int>? = listOf(),
) : Parcelable

@Keep
@Parcelize
data class MultipleBean(
    val id: Int = 0,
    val type: Int = 0,
    val title: String = "",
    val tag_list: List<FormTag>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class SingleBean(
    val title: String = "",
    val value: Int = 0,
    val is_selected: Boolean = false,
    val key: String? = ""
) : Parcelable

@Keep
@Parcelize
data class FormTag(
    val title: String = "",
    val value: Int = 0,
    var groupId: Int = 0,
    val tag_ids: List<Int>? = listOf(),
    val is_mutual: Int = 0,
    var is_selected: Boolean = false
) : Parcelable, SectionEntity {
    override val isHeader: Boolean
        get() = value < 0
}

enum class FormTypeEnum(val value: String) {
    /**
     * 单选表单
     */
    FORM_TYPE_SINGLE("single_list"),

    /**
     * 多选表单
     */
    FORM_TYPE_MULTI("multiple_list"),

    /**
     * 地址表单
     */
    FORM_TYPE_ADDRESS("address"),

    /**
     * 花费柱状图
     */
    FORM_TYPE_COST_MAP("cost_map")
}
