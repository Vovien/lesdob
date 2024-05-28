package com.tubewiki.mine.adapter.city

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.CityList
import com.tubewiki.mine.databinding.ItemSelectCityListDefaultLayoutBinding

class OverseasAdapter :
    BindingQuickAdapter<CityList.ChinaCity, ItemSelectCityListDefaultLayoutBinding>() {
    override fun convert(holder: BaseBindingHolder, item: CityList.ChinaCity) {
        holder.getViewBinding<ItemSelectCityListDefaultLayoutBinding>().apply {
            cpListItemName.text = item.title
        }
    }
}