package com.tubewiki.home.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.event.CircleChangedEvent
import com.jmbon.middleware.provider.BindingBaseItemProvider
import com.tubewiki.home.databinding.ItemAdvertisingDisplayBinding

/**
 * 图片广告适配器
 * @author MilkCoder
 * @date 2023/6/8
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
class AdvertisingDisplayProvider :
    BindingBaseItemProvider<CircleChangedEvent, TubeArticleDetail, ItemAdvertisingDisplayBinding>() {
    override fun setEventData(
        it: CircleChangedEvent,
        item: TubeArticleDetail,
        viewBinding: ItemAdvertisingDisplayBinding
    ) {

    }

    override fun onViewRecycled(holder: BaseViewHolder) {

    }

    override val itemViewType: Int
        get() = 3

    override fun convert(helper: BaseViewHolder, item: TubeArticleDetail) {
//        item.adv?.let { adv ->
//            helper.getViewBinding<ItemAdvertisingDisplayBinding>().apply {
//                this.adv = item.adv
//                tvUserName.setUserNickColor(R.color.color_262626)
//                root.setOnSingleClickListener({
//                    BannerHelper.onClick(CommonBanner(item_type = adv.itemType, identity = adv.identity.toString()))
//                    BuryHelper.addEvent(ClickEventEnum.EVENT_PREGNANCY_GUIDE_DETAIL_CASE_AD.value)
//                })
//                executePendingBindings()
//            }
//        }
    }
}