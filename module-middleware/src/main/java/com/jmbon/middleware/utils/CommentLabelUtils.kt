package com.jmbon.middleware.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.jmbon.middleware.R

fun TextView.setLabel(type: Int) {
    if (this.background == null)
        this.setBackgroundResource(R.drawable.shape_user_label)
    // 1 答主
    // 2 提问者
    // 3 文章作者

    text = when (type) {
        1 -> {
            setTextColor(R.color.white.Color)
            this.setAdapterLabelDrawable(R.color.color_currency.Color)
            "答主"
        }
        2 -> {
            setTextColor(R.color.color_currency.Color)
            this.setAdapterLabelDrawable(R.color.color_1A0EA9B0.Color)
            "提问者"
        }
        else -> {
            setTextColor(R.color.white.Color)
            this.setAdapterLabelDrawable(R.color.privacy_color.Color)
            "作者"
        }
    }
}

fun TextView.setAdapterLabelDrawable(color: Int) {


    if (this.background is ColorDrawable) {
        val drawable = this.background as ColorDrawable
        drawable.color = color
        this.background = drawable
    } else {
        val drawable = this.background as GradientDrawable
        drawable.setColor(color)
        this.background = drawable
    }
}