package com.jmbon.middleware.dialog

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.DialogTipsLayoutBinding
import com.jmbon.middleware.utils.isNotNullEmpty


/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   :提示dialog
 * version: 1.0
 */
class TipsDialog(
    var mContext: Context,
    var title: String,
    var content: String,
    val showGoToButton: Boolean,
    val leftBtnText: String = "",
    val rightBtnText: String = "",
    var result: () -> Unit
) :
    BaseCenterDialog<DialogTipsLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()

        binding.tvTitle.text = title
        binding.tvContent.text = content
        if (content.isNullOrEmpty()) binding.tvContent.gone() else binding.tvContent.visible()
        if (showGoToButton) {
            binding.linearCancel.visible()
        } else {
            binding.tvOk.setTextColor(mContext.resources.getColor(R.color.color_currency))
            binding.linearCancel.gone()
        }

        if (leftBtnText.isNotNullEmpty()) {
            binding.tvOk.text = leftBtnText
        }
        if (rightBtnText.isNotNullEmpty()) {
            binding.tvCancel.text = rightBtnText
        }


        binding.tvOk.setOnClickListener {
            dismiss()
            if(!showGoToButton){
                result()
            }
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
            result()
        }
    }

}