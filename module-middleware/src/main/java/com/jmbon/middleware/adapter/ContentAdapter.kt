package com.jmbon.middleware.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.jmbon.middleware.R
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.ContentBean
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius

/******************************************************************************
 * Description: 内容Adapter
 *
 * Author: jhg
 *
 * Date: 2023/10/23
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ContentAdapter: BaseQuickAdapter<ContentBean, BaseViewHolder>(R.layout.item_content_layout) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            ArouterUtils.toArticleDetailsActivity(data[position].id)
        }
    }

    override fun convert(holder: BaseViewHolder, item: ContentBean) {
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_content, item.content)
        holder.setText(R.id.tv_read_count, "${item.read_num}阅读")
        holder.getView<ShapeableImageView>(R.id.iv_icon).loadRadius(item.cover, 8.dp)
    }
}
