package com.tubewiki.home.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.glide.GlideUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.ItemTypeEnum
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.tubewiki.home.R
import com.tubewiki.home.bean.Bank

/******************************************************************************
 * Description: 首页精子银行Adapter
 *
 * Author: jhg
 *
 * Date: 2023/9/20
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class HomeBankAdapter: BaseQuickAdapter<Bank, BaseViewHolder>(R.layout.item_home_bank_layout) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            data[position].apply {
                BannerHelper.onClick(CommonBanner(item_id = topic_id, item_type = ItemTypeEnum.ITEM_TYPE_COLUMN.value))
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: Bank) {
        holder.itemView.setBackground(
            backgroundColor = R.color.white08.toColorInt(),
            borderGradientColors = intArrayOf(Color.TRANSPARENT, R.color.white80.toColorInt()),
            borderOrientation = GradientDrawable.Orientation.TOP_BOTTOM,
            borderWidth = 0.5.dp,
            radius = 12.dp
        )
        GlideUtil.getInstance().loadUrl(holder.getView(R.id.iv_icon), item.icon, R.drawable.icon_circle_placeholder)
        holder.setText(R.id.tv_title_en, item.en_name)
        holder.setText(R.id.tv_title, item.zh_name)
    }
}