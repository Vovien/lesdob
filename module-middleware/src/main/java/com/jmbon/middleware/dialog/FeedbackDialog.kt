package com.jmbon.middleware.dialog

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.R
import com.jmbon.middleware.adapter.FeedbackItemAdapter
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.DialogFeedbackLayoutBinding
import com.jmbon.middleware.databinding.DialogTipsLayoutBinding
import com.jmbon.middleware.utils.init
import com.lxj.xpopup.core.BottomPopupView


/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   :反馈弹窗
 * version: 1.0
 */
class FeedbackDialog(
    var mContext: Context,
    var result: (type: Int, dialog: BottomPopupView) -> Unit
) :
    BaseBottomDialog<DialogFeedbackLayoutBinding>(mContext) {

    val feedbackItemAdapter by lazy { FeedbackItemAdapter() }

    var selectedId = 0

    override fun onCreate() {
        super.onCreate()

        binding.recycleView.init(
            feedbackItemAdapter,
            layoutManager = GridLayoutManager(mContext, 2)
        )

        feedbackItemAdapter.setNewInstance(Constant.unHelpLabels)

        feedbackItemAdapter.setOnItemClickListener { adapter, view, pos ->
            selectedId = feedbackItemAdapter.getItem(pos).type
            feedbackItemAdapter.selectedPos = pos
            feedbackItemAdapter.notifyDataSetChanged()
        }

        binding.jbOk.setOnClickListener {
            result(selectedId, this)
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

}