package com.apkdv.mvvmfast.ktx

import android.graphics.Typeface
import android.widget.TextView

/**
 * 设置字体样式
 */
fun TextView.setTextStyle(style: Int) {
    this.setTypeface(Typeface.MONOSPACE, style)
}