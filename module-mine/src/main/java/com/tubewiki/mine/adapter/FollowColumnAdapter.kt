package com.tubewiki.mine.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.loadUrlRadius
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.FollowColumnData
import com.tubewiki.mine.databinding.ItemFollowColumnBinding

/**
 * time   : 2021/5/25
 * desc   : 我的关注-话题适配器
 * version: 1.0
 */
class FollowColumnAdapter :
    BindingQuickAdapter<FollowColumnData.Column, ItemFollowColumnBinding>() {
    init {
        addChildClickViewIds(R.id.sb_focus_on)
    }

    override fun convert(holder: BaseBindingHolder, item: FollowColumnData.Column) {
        holder.getViewBinding<ItemFollowColumnBinding>().apply {
            ivHeader.loadUrlRadius(item.columnPic, SizeUtils.dp2px(8f))
            tvTopicName.text = item.columnName
            tvFollowAmount.text = "${item.articleCount}篇文章"

            sbFocusOn.isSelected = true
            root.setOnClickListener {

                ARouter.getInstance().build("/home/activity/column_details")
                    .withInt(TagConstant.COLUMN_ID, item.columnId)
                    .navigation()
            }
        }
    }
}