package com.jmbon.middleware.dialog

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.SpanUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.DialogTipsLayoutBinding
import com.jmbon.middleware.databinding.DialogViolativeContentTipsLayoutBinding
import com.jmbon.middleware.utils.SpanTagUtils


/**
 * @author : leimg
 * time   : 2021/6/22
 * desc   :违规内容提示dialog
 * version: 1.0
 */
class ViolativeContentTipsDialog(
    var mContext: Context,
    var tagContent: String,
    var result: () -> Unit
) :
    BaseCenterDialog<DialogViolativeContentTipsLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()

//        SpanUtils.with(binding.tvContent).append("系统检测到您添加的内容部分涉嫌").append("${tagContent}违规")
//            .setForegroundColor(context.resources.getColor(R.color.colorFF5050)).setUnderline()
//            .append("，请查看处理。")
//            .create()

        binding.tvOk.setOnClickListener {
            dismiss()
            result()
        }

    }

}