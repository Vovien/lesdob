package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.chad.library.adapter.base.entity.MultiItemEntity
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 城市列表相关的数据结构
 *
 * Author: jhg
 *
 * Date: 2023/3/17
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class CityListInfoBean(
    val citys: List<ItemCityBean>? = listOf(),
    val overseas: List<ItemCityBean>? = listOf(),
    val hot_citys: List<ItemCityBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class CitySearchResultBean(
    val citys: List<ItemCityBean>? = listOf(),
) : Parcelable

@Keep
@Parcelize
data class GroupItemBean(
    val char: String = "",
    val citys: List<ItemCityBean>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class ItemCityBean(
    val id: Int = 0,
    val title: String = "",
    val name: String = "",
    val initial: String = "",
) : Parcelable, MultiItemEntity {
    override val itemType: Int
        get() = if (id > 0) ITEM_TYPE_CONTENT else ITEM_TYPE_ALPHABET

    companion object {
        const val ITEM_TYPE_ALPHABET = 1
        const val ITEM_TYPE_CONTENT = 0
    }
}