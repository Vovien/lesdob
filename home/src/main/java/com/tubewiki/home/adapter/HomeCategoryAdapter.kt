package com.tubewiki.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.glide.GlideUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.ItemTypeEnum
import com.jmbon.middleware.utils.BannerHelper
import com.tubewiki.home.R
import com.tubewiki.home.bean.TopicCategoryBean

/******************************************************************************
 * Description: 首页专题类别Adapter
 *
 * Author: jhg
 *
 * Date: 2023/9/20
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class HomeCategoryAdapter: BaseQuickAdapter<TopicCategoryBean, BaseViewHolder>(R.layout.item_home_category_layout) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            data[position].apply {
                BannerHelper.onClick(CommonBanner(item_id = topic_id, item_type = ItemTypeEnum.ITEM_TYPE_COLUMN.value))
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: TopicCategoryBean) {
        GlideUtil.getInstance().loadUrl(holder.getView(R.id.iv_icon), item.icon, R.drawable.icon_circle_placeholder)
        holder.setText(R.id.tv_title, item.name)
    }
}