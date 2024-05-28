package com.jmbon.middleware.dialog

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.DialogCommonWheelChooseLayoutBinding
import com.lxj.xpopup.XPopup
import com.qmuiteam.qmui.kotlin.onClick

/******************************************************************************
 * Description: 滚动式的单选弹框
 *
 * Author: jhg
 *
 * Date: 2023/5/12
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class CommonWheelChooseDialog(
    context: Context,
    private var title: String = "",
    private var defaultPos: Int,
    private val dataList: List<String>,
    private val result: (position: Int) -> Unit,
) : BaseBottomDialog<DialogCommonWheelChooseLayoutBinding>(context) {

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
        binding.tvTitle.text = title
        binding.wwValue.setData(dataList, defaultPos)

        binding.tvConfirm.onClick {
            result(binding.wwValue.currentPosition)
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