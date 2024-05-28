package com.tubewiki.hospital.dialog

import android.content.Context
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.tubewiki.hospital.databinding.DialogMoreDetailLayoutBinding

/**
 * @author : leimg
 * time   : 2022/8/11
 * desc   :
 * version: 1.0
 */
class MoreDetailDialog(var mContext: Context, var title: String, var content: String) :
    BaseBottomDialog<DialogMoreDetailLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()

        binding.tvTitle.text = title
        binding.tvContent.text = content
        binding.ivClose.setOnClickListener {
            dismiss()
        }

    }
}