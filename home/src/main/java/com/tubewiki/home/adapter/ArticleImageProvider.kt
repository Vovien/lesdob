package com.tubewiki.home.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.event.CircleChangedEvent
import com.jmbon.middleware.provider.BindingBaseItemProvider
import com.tubewiki.home.databinding.ItemArticleImageLayoutBinding

/**
 * @author : leimg
 * time   : 2022/5/10
 * desc   :
 * version: 1.0
 */
class ArticleImageProvider :
    BindingBaseItemProvider<CircleChangedEvent, TubeArticleDetail, ItemArticleImageLayoutBinding>() {
    override val itemViewType: Int
        get() = 2 //1 普通， 2百科

    override fun convert(helper: BaseViewHolder, item: TubeArticleDetail) {
//        helper.getViewBinding<ItemArticleImageLayoutBinding>().apply {
//            tvTitle.text = item.customTitle.ifEmpty { item.title }
//            tvDesc.text = item.customDescription.ifEmpty { HtmlTools.delHTMLTag(item.content) }
//
//            ivCover.loadRadius(item.indexCover.ifEmpty { item.cover }, 8f.dp())
//
//        }
    }

    override fun setEventData(
        it: CircleChangedEvent,
        item: TubeArticleDetail,
        viewBinding: ItemArticleImageLayoutBinding
    ) {
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
    }


}