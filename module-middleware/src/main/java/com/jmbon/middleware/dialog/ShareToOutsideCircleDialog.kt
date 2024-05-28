package com.jmbon.middleware.dialog

import android.content.Context
import android.text.Html
import android.widget.TextView
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.utils.SizeUtil
import com.jmbon.middleware.R
import com.jmbon.middleware.databinding.DialogShareToOutsideLayoutBinding
import com.jmbon.middleware.utils.SpanTagUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.setOnSingleClickListener

/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   : 分享到圈子外部
 * version: 1.0
 *
 * type:help,answer,article
 */
class ShareToOutsideCircleDialog(
    context: Context,
    var title: String,
    var result: () -> Unit
) :
    BaseCenterDialog<DialogShareToOutsideLayoutBinding>(context) {
    override fun onCreate() {
        super.onCreate()

        binding.tvContent.setText(
            SpanTagUtils.buildTag(
                "求助", Html.fromHtml(title).toString(),
                R.color.color_currency, false
            ), TextView.BufferType.SPANNABLE
        )

        binding.tvCancel.setOnSingleClickListener({ dismiss() })
        binding.tvSure.setOnSingleClickListener({
            //分享到圈子 跳转圈子页面
            result.invoke()
            dismiss()
        })
    }

    override fun getPopupWidth(): Int {
        return SizeUtil.getWidth() - (48 * 2f).dp()
    }
}