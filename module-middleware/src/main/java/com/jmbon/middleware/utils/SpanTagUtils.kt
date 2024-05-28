package com.jmbon.middleware.utils

import android.content.Context
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableStringBuilder
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.R
import com.jmbon.widget.TagSpanV2

/**
 * @author : leimg
 * time   : 2021/9/15
 * desc   :
 * version: 1.0
 */
object SpanTagUtils {
    fun buildTag(
        tag: String,
        ending: String,
        textColor: Int,
        full: Boolean
    ): SpannableStringBuilder {
        val color = ColorUtils.getColor(R.color.color_currency)
        val textColor = ColorUtils.getColor(textColor)
        val stringBuilder = SpannableStringBuilder().append(tag)
        // 背景色 文字颜色
        stringBuilder.setSpan(
            TagSpanV2(
                if (full) ColorUtils.getColor(R.color.color_1AA5CB_10) else color,
                textColor,
                11f.dp(),
                11f.dp(),
                8f.dp(),
                0f.dp(),
                2f.dp(),
                if (full) Paint.Style.FILL else Paint.Style.STROKE, true
            ),
            stringBuilder.length - tag.length,
            stringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        stringBuilder.append(ending)
        return stringBuilder
    }

    fun buildBitmapTag(
        mActivity: Context,
        tag: String,
        ending: String,
        bitmapId: Int,
        bgColor: Int,
        full: Boolean
    ): SpannableStringBuilder {
        val color = ColorUtils.getColor(R.color.color_currency)
        val whiteColor = ColorUtils.getColor(R.color.white)
        val stringBuilder = SpannableStringBuilder().append(tag)
        // 背景色 文字颜色
        stringBuilder.setSpan(
            TagSpanV2(
                mActivity,
                if (full) ColorUtils.getColor(bgColor) else color,
                whiteColor,
                11f.dp(),
                11f.dp(),
                8f.dp(),
                0f.dp(),
                2f.dp(),
                bitmapId,
                14f.dp(),
                if (full) Paint.Style.FILL else Paint.Style.STROKE, true
            ),
            stringBuilder.length - tag.length,
            stringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        stringBuilder.append(ending)
        return stringBuilder
    }

    fun buildBitmapTag(
        mActivity: Context,
        tag: String,
        start: String,
        ending: String,
        bitmapId: Int,
        textColor: Int,
        bgColor: Int,
        full: Boolean
    ): SpannableStringBuilder {
        val color = ColorUtils.getColor(R.color.color_currency)
        val textColor = ColorUtils.getColor(textColor)
        val stringBuilder = SpannableStringBuilder().append(start).append(tag)
        // 背景色 文字颜色
        stringBuilder.setSpan(
            TagSpanV2(
                mActivity,
                start,
                if (full) ColorUtils.getColor(bgColor) else color,
                textColor,
                11f.dp(),
                11f.dp(),
                8f.dp(),
                0f.dp(),
                2f.dp(),
                bitmapId,
                14f.dp(),
                if (full) Paint.Style.FILL else Paint.Style.STROKE, true
            ),
            stringBuilder.length - tag.length,
            stringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        stringBuilder.append(ending)
        return stringBuilder
    }
}