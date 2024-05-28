package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.getViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.event.CircleChangedEvent
import com.jmbon.middleware.provider.BindingBaseItemProvider
import com.tubewiki.home.databinding.ItemArticleNormalLayoutBinding

/**
 * @author : leimg
 * time   : 2022/5/10
 * desc   :
 * version: 1.0
 */
class ArticleNormalProvider :
    BindingBaseItemProvider<CircleChangedEvent, TubeArticleDetail, ItemArticleNormalLayoutBinding>() {
    override val itemViewType: Int
        get() = 1 //1 普通， 2百科

    override fun convert(helper: BaseViewHolder, item: TubeArticleDetail) {
        helper.getViewBinding<ItemArticleNormalLayoutBinding>().apply {
//            tvTitle.text = item.title.ifEmpty { item.customTitle }
//            tvDesc.text = HtmlTools.delHTMLTag(item.content).ifEmpty { HtmlTools.delHTMLTag(item.customDescription) }
//
//            ivCover.loadRadius(
//                item.indexCover.ifEmpty { item.cover },
//                8f.dp(),
//                R.drawable.icon_tube_placeholder
//            )
//
//            if (item.readNum >= 10000) {
//                tvNum.text = "${item.readNum.coverToTenThousandPlus()}阅读"
//                tvNum.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
//                tvNum.setTextColor(ColorUtils.getColor(R.color.color_FFAB8F))
//            } else {
//                tvNum.text = "${item.readNum}阅读"
//                tvNum.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
//                tvNum.setTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
//            }

        }
    }

    override fun setEventData(
        it: CircleChangedEvent,
        item: TubeArticleDetail,
        viewBinding: ItemArticleNormalLayoutBinding
    ) {
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
    }


}