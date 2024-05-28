package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.home.R
import com.tubewiki.home.bean.ArticleRelatedBean
import com.tubewiki.home.databinding.ItemAllColumnLayoutBinding
import com.tubewiki.home.databinding.ItemColumnArticlePageLayoutBinding
import com.tubewiki.home.databinding.ItemHomePregentHelpLayoutBinding
import com.tubewiki.home.databinding.ItemHomeRecommendKnowledgeLayoutBinding

/**
 * 文章详情页索引 adapter
 */
class ColumnArticlePageAdapter :
    BindingQuickAdapter<ArticleRelatedBean.Index, ItemColumnArticlePageLayoutBinding>() {

    var selectedIndex = -1
    var allCount = 0


    override fun convert(holder: BaseBindingHolder, item: ArticleRelatedBean.Index) {
        holder.getViewBinding<ItemColumnArticlePageLayoutBinding>().apply {
            tvTitle.text = item.articleTitle
            tvAll.text = "共${if (allCount == 0) itemCount else allCount}篇"
            if (holder.adapterPosition == selectedIndex) {
                tvIndex.text = "当前"
                tvTitle.setTextColor(context.resources.getColor(R.color.color_BFBFBF))
            } else {
                tvIndex.text = "${holder.adapterPosition + 1}"
                tvTitle.setTextColor(context.resources.getColor(R.color.color_currency))
            }

        }
    }
}