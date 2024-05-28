package com.jmbon.middleware.dialog

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.SpanUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.DialogTipsLayoutBinding
import com.jmbon.middleware.databinding.DialogViolativeContentReportTipsLayoutBinding
import com.jmbon.middleware.databinding.DialogViolativeContentTipsLayoutBinding
import com.jmbon.middleware.utils.SpanTagUtils


/**
 * @author : leimg
 * time   : 2021/6/22
 * desc   :违规内容提示上报dialog
 * version: 1.0
 */
class ViolativeContentReportTipsDialog(
    var mContext: Context,
    var isAnswer: Boolean,
    var result: () -> Unit
) :
    BaseCenterDialog<DialogViolativeContentReportTipsLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()

        SpanUtils.with( binding.tvContent).append("系统检测到您添加的内容仍然涉嫌违规，介于您多次提交均未通过，建议将").append("文本内容").setUnderline().setBold().append("上报官方审核。").create()

        if (isAnswer) {
            binding.tvTips.text = "注意：审核期间，您将暂时无法继续回答。"
        } else {
            binding.tvTips.text = "注意：审核期间，您将暂时无法使用提问功能。"
        }

        binding.tvModify.setOnClickListener {
            dismiss()
        }
        binding.tvOk.setOnClickListener {
            dismiss()
            result()
        }

    }

}