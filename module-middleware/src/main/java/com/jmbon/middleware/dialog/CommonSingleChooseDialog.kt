package com.jmbon.middleware.dialog

import android.content.Context
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.DialogCommonSingleChooseLayoutBinding
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.lxj.xpopup.XPopup
import com.qmuiteam.qmui.kotlin.onClick

/******************************************************************************
 * Description: 通用的单选弹框
 *
 * Author: jhg
 *
 * Date: 2023/5/6
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class CommonSingleChooseDialog(
    context: Context,
    private val dataList: List<String>,
    private val result: (position: Int) -> Unit,
    private val supportCancel: Boolean = true
) : BaseBottomDialog<DialogCommonSingleChooseLayoutBinding>(context) {

    private val chooseAdapter by lazy {

        object : BaseQuickAdapter<String, BaseViewHolder>(
            R.layout.item_single_choose_layout,
            dataList.toMutableList()
        ) {

            override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                super.onAttachedToRecyclerView(recyclerView)
                setOnItemClickListener { _, _, position ->
                    result(position)
                    dismiss()
                }
            }

            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_value, item)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        binding.rvContent.init(
            chooseAdapter,
            dividerHeight = 0.5f,
            dividerColor = R.color.colorF5F5F5.toColorInt(),
            vertical = false
        )
        if (dataList.size > 4) {
            binding.rvContent.layoutParams?.height = 260.dp
        }

        binding.tvCancel.isVisible = supportCancel
        binding.tvCancel.onClick {
            dismiss()
        }
    }

    fun showDialog(
        isDismissOnTouchOutside: Boolean = true,
        isDismissOnBackPressed: Boolean = true
    ) {
        chooseAdapter.setList(dataList)
        XPopup.Builder(context)
            .enableDrag(isDismissOnTouchOutside)
            .dismissOnTouchOutside(isDismissOnTouchOutside)
            .dismissOnBackPressed(isDismissOnBackPressed)
            .asCustom(this).show()
    }
}