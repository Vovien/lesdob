package com.tubewiki.mine.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.utils.load
import com.tubewiki.mine.bean.LittleTool
import com.tubewiki.mine.databinding.ItemLittleToolsBinding

class LittleToolAdapter : BindingQuickAdapter<LittleTool,ItemLittleToolsBinding>() {
    override fun convert(holder: BaseBindingHolder, item: LittleTool) {
        holder.getViewBinding<ItemLittleToolsBinding>().apply {
            textName.text = item.name
            imageIcon.load(item.icon)
        }
    }
}