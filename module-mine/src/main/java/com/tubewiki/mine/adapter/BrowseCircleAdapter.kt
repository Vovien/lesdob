package com.tubewiki.mine.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.R
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.loadCircle
import com.tubewiki.mine.bean.CircleList
import com.tubewiki.mine.bean.GroupCircle
import com.tubewiki.mine.databinding.ItemBrowseCircleBinding

class BrowseCircleAdapter : BindingQuickAdapter<GroupCircle, ItemBrowseCircleBinding>() {
    override fun convert(holder: BaseBindingHolder, item: GroupCircle) {
        holder.getViewBinding<ItemBrowseCircleBinding>().apply {
            imageAvatar.loadCircle(item.covers, R.drawable.icon_circle_placeholder)
            textName.text = item.name
        }
    }
}