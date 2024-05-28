package com.jmbon.widget.dialog

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.widget.databinding.DialogCustomLayoutBinding
import com.luck.picture.lib.tools.DoubleUtils

/**
 * @author : leimg
 * time   : 2021/4/12
 * desc   :通用底部弹出dialog-自定义列表格式
 * version: 1.0
 */
class CustomListBottomDialog(
    context: Context,
    var dataSource: MutableList<MultiItemEntity>,
    val result: (type: Int) -> Unit
) :
    BaseBottomDialog<DialogCustomLayoutBinding>(context) {


    private val customDialogAdapter by lazy {
        CustomListDialogListAdapter(dataSource)
    }

    override fun onCreate() {
        super.onCreate()

        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.isMotionEventSplittingEnabled = false
        binding.rvList.adapter = customDialogAdapter
        customDialogAdapter.setOnItemClickListener { adapter, view, position ->
            if (DoubleUtils.isFastDoubleClick()) {
                return@setOnItemClickListener
            }
            dismiss()
            result(position)
        }
    }
}
