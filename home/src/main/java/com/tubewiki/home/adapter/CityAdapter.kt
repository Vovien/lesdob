package com.tubewiki.home.adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.LocationInfoBean
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.home.R
import com.tubewiki.home.bean.ItemCityBean
import com.tubewiki.home.bean.ItemCityBean.Companion.ITEM_TYPE_ALPHABET
import com.tubewiki.home.bean.ItemCityBean.Companion.ITEM_TYPE_CONTENT
import org.greenrobot.eventbus.EventBus

/******************************************************************************
 * Description: 城市列表Adapter
 *
 * Author: jhg
 *
 * Date: 2023/3/17
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class CityAdapter :
    BaseMultiItemQuickAdapter<ItemCityBean, BaseViewHolder>() {

    init {
        addItemType(ITEM_TYPE_ALPHABET, R.layout.item_city_group_title_layout)
        addItemType(ITEM_TYPE_CONTENT, R.layout.item_city_item_layout)
    }

    private val isGlobalConfig by lazy {
        (context as? Activity)?.intent?.getBooleanExtra("isGlobalConfig", true) ?: true
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            if (data[position].itemType == ITEM_TYPE_ALPHABET) {
                return@setOnItemClickListener
            }

            data[position].apply {
                if (isGlobalConfig) {
                    CommonViewModel.updateLocation(LocationInfoBean(cityName = title, cityCode = id.toString(), countryName = "中国"))
                } else {
                    EventBus.getDefault().post(CommonEventHub.ChangeCityEvent(cityName = title, cityId = id, countryName = "中国"))
                }
            }
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: ItemCityBean) {
        if (item.itemType == ITEM_TYPE_ALPHABET) {
            holder.setText(R.id.tv_group_name, item.initial)
        } else {
            holder.setText(R.id.tv_title, item.title)
        }
    }
}