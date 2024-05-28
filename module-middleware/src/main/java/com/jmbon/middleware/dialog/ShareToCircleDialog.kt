package com.jmbon.middleware.dialog

import android.content.Context
import android.text.Html
import android.widget.TextView
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.utils.SizeUtil
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.ShareCircleBean
import com.jmbon.middleware.databinding.DialogShareToCircleLayoutBinding
import com.jmbon.middleware.utils.SpanTagUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.jmbon.middleware.utils.setOnSingleClickListener

/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   : 分享圈子dialog
 * version: 1.0
 *
 * type:help,answer,article
 */
class ShareToCircleDialog(
    context: Context,
    var circle: ShareCircleBean.Circle,
    var shareId: Int,
    var title: String,
    var type: String,
    var result: (type: Int) -> Unit
) :
    BaseCenterDialog<DialogShareToCircleLayoutBinding>(context) {

    var typeValue = 0
    override fun onCreate() {
        super.onCreate()

        binding.ivCover.loadRadius(circle.covers, 8f.dp(), R.drawable.icon_topic_placeholder)
        binding.tvMessage.text = circle.name
        binding.tvAnswerTag.text = "${circle.questionCount}问答"
        binding.tvPersonTag.text = "${circle.memberCount}圈友"


        when (type) {

            "article" -> {
                binding.tvContent.setText(
                    SpanTagUtils.buildTag(
                        "文章", Html.fromHtml(title).toString(),
                        R.color.color_1AA5CB, true
                    ), TextView.BufferType.SPANNABLE
                )
                typeValue = 2
            }
            "answer" -> {
                binding.tvContent.setText(
                    SpanTagUtils.buildTag(
                        "问答", Html.fromHtml(title).toString(),
                        R.color.color_currency, true
                    ), TextView.BufferType.SPANNABLE
                )
                typeValue = 4
            }
            "help" -> {
                binding.tvContent.setText(
                    SpanTagUtils.buildTag(
                        "求助", Html.fromHtml(title).toString(),
                        R.color.color_currency, true
                    ), TextView.BufferType.SPANNABLE
                )
                typeValue = 3
            }
            "circle" -> {
                binding.tvContent.setText(
                    SpanTagUtils.buildTag(
                        "圈子", Html.fromHtml(title).toString(),
                        R.color.color_currency, true
                    ), TextView.BufferType.SPANNABLE
                )
                typeValue = 1
            }
        }

        binding.tvCancel.setOnSingleClickListener({ dismiss() })

        binding.tvSure.setOnSingleClickListener({
            //分享到圈子 跳转圈子页面

            result(typeValue)

            dismiss()
        })
    }

    override fun getPopupWidth(): Int {
        return SizeUtil.getWidth() - (48 * 2f).dp()
    }
}