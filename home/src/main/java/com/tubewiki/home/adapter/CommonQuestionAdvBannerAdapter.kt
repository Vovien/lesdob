package com.tubewiki.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.ktx.DataBindingBannerAdapter
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.utils.BannerHelper
import com.tubewiki.home.bean.CommonQuestionAdv
import com.tubewiki.home.databinding.ItemCommonQuestionBinding

/**
 * 海外经典案例
 * @author MilkCoder
 * @date 2023/6/6
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
class CommonQuestionAdvBannerAdapter :
    DataBindingBannerAdapter<CommonQuestionAdv, ItemCommonQuestionBinding>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnBannerListener { data, _ ->
            data.apply {
                BannerHelper.onClick(CommonBanner(item_type = itemType, identity = identity))
            }
        }
    }

    override fun onBindView(
        holder: AutomaticDataBindingHolder<ItemCommonQuestionBinding>?,
        data: CommonQuestionAdv?,
        position: Int,
        size: Int
    ) {
        holder?.dataBinding?.apply {
            adv = data
            executePendingBindings()
        }
    }


}