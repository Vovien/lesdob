package com.tubewiki.mine.adapter.city

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.CityList
import com.tubewiki.mine.databinding.ItemGridLayoutBinding

class HotCityListAdapter : BindingQuickAdapter<CityList.ChinaCity, ItemGridLayoutBinding>() {
    override fun convert(holder: BaseBindingHolder, item: CityList.ChinaCity) {
        holder.getViewBinding<ItemGridLayoutBinding>().apply {
            cpGirdItemName.text = item.title
        }
    }
}