package com.jmbon.minitools.content.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.allen.library.CircleImageView
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.parseColor
import com.jmbon.minitools.R
import com.jmbon.minitools.content.bean.HelpGroupItemBean
import com.jmbon.minitools.databinding.ItemHelpGroupLayoutBinding
import kotlin.math.min

/******************************************************************************
 * Description: 好孕互助群Adapter
 *
 * Author: jhg
 *
 * Date: 2023/9/8
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class HelpGroupAdapter : BindingQuickAdapter<HelpGroupItemBean, ItemHelpGroupLayoutBinding>() {
    override fun convert(holder: BaseBindingHolder, item: HelpGroupItemBean) {
        holder.getViewBinding<ItemHelpGroupLayoutBinding>().apply {
            root.setBackground(
                backgroundColor = parseColor(item.color, Color.TRANSPARENT),
                radius = 8.dp
            )
            tvTitle.text = item.name
            holder.setGone(R.id.gp_member, item.member_avatar.isNullOrEmpty() && item.txt.isNullOrBlank())
            initOnlineMemberLayout(holder.getView(R.id.fl_member_icons), item.member_avatar)
            tvOnlineCount.text = item.txt
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            BannerHelper.onClick(
                CommonBanner(
                    item_type = data[position].item_type,
                    identity = data[position].identity
                )
            )
        }
    }

    /**
     * 初始化用户头像
     */
    private fun initOnlineMemberLayout(containerLayout: FrameLayout, icons: List<String>?) {
        containerLayout.removeAllViews()
        if (icons.isNullOrEmpty()) {
            return
        }

        val iconSize = 20.dp
        val padding = 1.dp
        val count = min(icons.size, 3)
        icons.subList(0, count).forEachIndexed { index, item ->
            CircleImageView(containerLayout.context).apply {
                setPadding(padding, padding, padding, padding)
                load(item)
                setBackground(
                    shape = GradientDrawable.OVAL,
                    borderColor = Color.WHITE,
                    borderWidth = 1.dp
                )
                containerLayout.addView(this, FrameLayout.LayoutParams(iconSize, iconSize).apply {
                    leftMargin = index * 12.dp
                })
            }
        }
    }
}