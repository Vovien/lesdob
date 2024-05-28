package com.jmbon.middleware.utils

import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.R

import com.jmbon.widget.TagSpanV2

fun buildTag(tag: String, ending: String, full: Boolean, isBold: Boolean): SpannableStringBuilder {
    val color = ColorUtils.getColor(R.color.color_currency)
    val stringBuilder = SpannableStringBuilder().append(tag)
    // 背景色 文字颜色
    stringBuilder.setSpan(
        TagSpanV2(
            if (full) ColorUtils.getColor(R.color.color_1AA5CB_10) else color,
            color,
            SizeUtils.sp2px(11f),
            12f.dp(),
            8f.dp(),
            if (full) Paint.Style.FILL else Paint.Style.STROKE,isBold
        ),
        stringBuilder.length - tag.length,
        stringBuilder.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    stringBuilder.append(ending)
    return stringBuilder
}

fun buildTag(textColor: Int, tag: String, ending: String, full: Boolean, isBold: Boolean): SpannableStringBuilder {
    val color = ColorUtils.getColor(textColor)
    val stringBuilder = SpannableStringBuilder().append(tag)
    // 背景色 文字颜色
    stringBuilder.setSpan(
        TagSpanV2(
            if (full) ColorUtils.getColor(R.color.color_1AA5CB_10) else color,
            color,
            SizeUtils.sp2px(11f),
            12f.dp(),
            8f.dp(),
            if (full) Paint.Style.FILL else Paint.Style.STROKE,isBold
        ),
        stringBuilder.length - tag.length,
        stringBuilder.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    stringBuilder.append(ending)
    return stringBuilder
}

fun joinCircle(name: String, groupNumber: String, join: Boolean,questionId:Int = 0) {
    if (join) {
        ARouter.getInstance().build("/chat/circle/circle_detail")
            .withString("groupName", name)
            .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
            .withString("groupNumber", groupNumber)
            .navigation()
    } else {
        ARouter.getInstance().build("/chat/group/info")
            .withString("groupName", name)
            .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
            .withString("groupNumber", groupNumber)
            .navigation()
    }
}

/**
 * 这种写法的问题在于 如果已经设置了 ClickListener 切换用户会 id 不会变。
 * 由于圈子页面在二级页面，退出账号必然重建了页面，可以避免这种问题
 */
fun View?.toCircleProfile(uid: Int, cancel: Int = 0) {
    this?.let {
        it.setOnSingleClickListener({
            ARouter.getInstance().build("/my/circle/profile")
                .withInt(TagConstant.USER_UID, uid)
                .withInt(TagConstant.USER_CANCEL, cancel)
                .navigation()
        })
    }
}