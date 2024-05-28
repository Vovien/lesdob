package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.TubeArticleDetail
import com.tubewiki.home.databinding.ItemHomeRecommendKnowledgeLayoutBinding

/**
 * 首页推荐知识adapter
 */
class HomeRecommendKnowledgeAdapter :
    BindingQuickAdapter<TubeArticleDetail, ItemHomeRecommendKnowledgeLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: TubeArticleDetail) {
        holder.getViewBinding<ItemHomeRecommendKnowledgeLayoutBinding>().apply {


//            ivCover.loadRadius(
//                item.indexCover, 8f.dp(),
//                R.drawable.icon_tube_placeholder
//            )
//
//            tvTitle.text = item.customTitle
//            tvDesc.text = item.customDescription

        }
    }
}