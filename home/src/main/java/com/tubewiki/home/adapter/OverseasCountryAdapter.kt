package com.tubewiki.home.adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.LocationInfoBean
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.home.R
import com.tubewiki.home.activity.ChooseCityActivity
import com.tubewiki.home.bean.ItemCityBean
import org.greenrobot.eventbus.EventBus

/******************************************************************************
 * Description: 海外国家列表的Adapter
 *
 * Author: jhg
 *
 * Date: 2023/3/17
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class OverseasCountryAdapter(private val isCityAdapter: Boolean = false) :
    BaseQuickAdapter<ItemCityBean, BaseViewHolder>(R.layout.item_city_item_layout) {

    private val isGlobalConfig by lazy {
        (context as? Activity)?.intent?.getBooleanExtra("isGlobalConfig", true) ?: true
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            data[position].apply {
                if (isGlobalConfig) {
                    CommonViewModel.updateLocation(
                        if (isCityAdapter) {
                            LocationInfoBean(
                                cityName = title,
                                cityCode = id.toString(),
                                countryName = "中国"
                            )
                        } else {
                            LocationInfoBean(countryName = title)
                        }
                    )
                } else {
                    val event = if (isCityAdapter) {
                        CommonEventHub.ChangeCityEvent(cityName = title, cityId = id, countryName = "中国")
                    } else {
                        CommonEventHub.ChangeCityEvent(countryName = title)
                    }
                    EventBus.getDefault().post(event)
                }
            }
            ActivityUtils.finishActivity(ChooseCityActivity::class.java)
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: ItemCityBean) {
        holder.setText(R.id.tv_title, item.title)
    }
}