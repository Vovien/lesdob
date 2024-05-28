package com.tubewiki.home.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.jmbon.middleware.bean.TubeArticleDetail


/**
 * 指南聚合页面adapter
 */
class ColumnDetailMultiAdapter : BaseProviderMultiAdapter<TubeArticleDetail>() {

    init {
        addItemProvider(ArticleNormalProvider())
        addItemProvider(ArticleImageProvider())
        addItemProvider(AdvertisingDisplayProvider())
    }

    override fun getItemType(data: List<TubeArticleDetail>, position: Int): Int {
//        var questionMultiData = data[position]
//        return if (questionMultiData.type == "adv") 3 else questionMultiData.articleType
        return 0
    }
}