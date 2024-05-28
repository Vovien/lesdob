package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.CircleInfoBean
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.tubewiki.home.R
import com.tubewiki.home.databinding.ItemArticleCircleLayoutBinding

/**
 * 文章详情页推荐圈子adapter
 */
class ArticleCircleAdapter :
    BindingQuickAdapter<CircleInfoBean, ItemArticleCircleLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: CircleInfoBean) {
        holder.getViewBinding<ItemArticleCircleLayoutBinding>().apply {

            ivCover.loadRadius(
                item.cover, 8f.dp(),
                R.drawable.icon_tube_placeholder
            )

            tvTitle.text = item.name
            tvDesc.text = "${item.member_count}位姐妹正在热聊"

        }
    }
}