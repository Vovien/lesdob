package com.jmbon.middleware.extention

import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.text.inSpans

/**
 * @Description：SpannableStringBuilder扩展
 * @Author： jhg
 * @Time： 2022/8/19
 * @Version 1.0.0
 */

/**
 * 设置点击事件
 * 注意：相应的TextView还需要设置以下代码：
 * movementMethod = LinkMovementMethod() // 响应点击
 * highlightColor = Color.TRANSPARENT // 取消文字点击时的背景颜色
 */
inline fun SpannableStringBuilder.clickable(showUnderline: Boolean = false, textColor: Int? = null, crossinline clickAction: () -> Unit, builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(object : ClickableSpan() {
        override fun onClick(widget: View) {
            clickAction()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            textColor?.let {
                ds.color = textColor
            }
            ds.isUnderlineText = showUnderline
        }
    }, builderAction = builderAction)