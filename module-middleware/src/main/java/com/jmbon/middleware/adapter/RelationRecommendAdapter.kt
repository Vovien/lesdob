package com.jmbon.middleware.adapter

import com.jmbon.middleware.bean.CircleInfoBean
import com.jmbon.middleware.bean.event.CircleChangedEvent
import com.jmbon.middleware.databinding.ItemRelatedToRecommendBinding
import com.jmbon.middleware.utils.setOnSingleClickListener

/**
 * @author : leimg
 * time   : 2021/4/13
 * desc   :
 * version: 1.0
 */
class RelationRecommendAdapter :
    EventAdapter<CircleChangedEvent, CircleInfoBean, ItemRelatedToRecommendBinding>() {

    override fun convert(holder: BaseBindingHolder, item: CircleInfoBean) {

        holder.getViewBinding<ItemRelatedToRecommendBinding>()
            .apply {
                title.text = ""


                root.setOnSingleClickListener({

                })
            }
    }

    override fun setEventData(
        it: CircleChangedEvent,
        item: CircleInfoBean,
        viewBinding: ItemRelatedToRecommendBinding,
    ) {

    }
}
