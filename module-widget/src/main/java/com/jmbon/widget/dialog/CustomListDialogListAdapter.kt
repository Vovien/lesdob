package com.jmbon.widget.dialog

import android.view.ViewGroup
import com.apkdv.mvvmfast.ktx.getViewBinding
import com.apkdv.mvvmfast.ktx.withBinding
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.widget.R
import com.jmbon.widget.databinding.DialogCustomBodyLayoutBinding
import com.jmbon.widget.databinding.DialogCustomFootLayoutBinding
import com.jmbon.widget.databinding.DialogCustomHeadLayoutBinding

/**
 * @author leimg
 * @desc 回答问题草稿保存dialog 适配器
 */
class CustomListDialogListAdapter(list: MutableList<MultiItemEntity>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(list) {

    init {
        addItemType(1, R.layout.dialog_custom_head_layout)
        addItemType(2, R.layout.dialog_custom_body_layout)
        addItemType(3, R.layout.dialog_custom_foot_layout)
    }

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseViewHolder {
        val viewHolder = super.createBaseViewHolder(parent, layoutResId)
        return when (layoutResId) {
            R.layout.dialog_custom_head_layout -> viewHolder.withBinding(
                DialogCustomHeadLayoutBinding::bind
            )
            R.layout.dialog_custom_body_layout -> viewHolder.withBinding(
                DialogCustomBodyLayoutBinding::bind
            )
            R.layout.dialog_custom_foot_layout -> viewHolder.withBinding(
                DialogCustomFootLayoutBinding::bind
            )
            else -> throw IllegalStateException()
        }
    }


    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {

        when (item.itemType) {
            1 -> {
                holder.getViewBinding<DialogCustomHeadLayoutBinding>()
                    .apply {
                        val data = item as CustomDialogTypeBean
                        tvTitle.text = data.title
                    }
            }
            2 -> {
                holder.getViewBinding<DialogCustomBodyLayoutBinding>()
                    .apply {
                        val data = item as CustomDialogTypeBean
                        tvMessage.text = data.title
                        if (data.color != 0) {
                            tvMessage.setTextColor(ColorUtils.getColor(data.color))
                        } else {
                            tvMessage.setTextColor(ColorUtils.getColor(R.color.color_262626))
                        }

                    }
            }
            3 -> {
                holder.getViewBinding<DialogCustomFootLayoutBinding>()
                    .apply {
                        val data = item as CustomDialogTypeBean
                        tvCancel.text = data.title
                    }

            }
        }
    }
}