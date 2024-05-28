package com.jmbon.middleware.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.KeyWordList
import com.jmbon.middleware.databinding.ItemSearchKeyBinding
import com.jmbon.middleware.utils.setKeyHighLight

class SearchRecommendedAdapter : BindingQuickAdapter<KeyWordList.KeyWord, ItemSearchKeyBinding>() {

    override fun convert(holder: BaseBindingHolder, item: KeyWordList.KeyWord) {
        holder.getViewBinding<ItemSearchKeyBinding>().apply {

            textSearchKey.text = item.keyword.setKeyHighLight(item.keywordHighlight)
        }

    }
}