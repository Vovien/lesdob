package com.jmbon.middleware.extention

import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.Utils

/**
 * @Description：SpannableStringBuilder扩展
 * @Author： jhg
 * @Time： 2022/8/19
 * @Version 1.0.0
 */

/**
 * 资源id转颜色id
 */
@ColorInt
fun Int.toColorInt(): Int {
    return ContextCompat.getColor(Utils.getApp(), this)
}

/**
 * 资源id转Drawable
 */
@DrawableRes
fun Int.toDrawable(): Drawable? {
    return ContextCompat.getDrawable(Utils.getApp(), this);
}

/**
 * 资源id转String
 */
@StringRes
fun Int.toResString(): String {
    return Utils.getApp().resources.getString(this)
}

/**
 * 资源id转尺寸
 */
@DimenRes
fun Int.toDimenInt(): Int {
    return Utils.getApp().resources.getDimensionPixelSize(this)
}

/**
 * 资源id转尺寸
 */
@DimenRes
fun Int.toDimenFloat(): Float {
    return Utils.getApp().resources.getDimension(this)
}


/**
 * 资源名称转颜色id
 */
fun String.toColorInt(): Int {
    val int = Utils.getApp().resources.getIdentifier(this, "color", this)
    return int.toColorInt()
}

/**
 * 资源名称转Drawable
 */
fun String.toDrawable(): Drawable? {
    val int = Utils.getApp().resources.getIdentifier(this, "drawable", this)
    return int.toDrawable()
}

/**
 * 资源名称转String
 */
fun String.toResString(): String {
    val int = Utils.getApp().resources.getIdentifier(this, "string", this)
    return int.toResString()
}

/**
 * 资源名称转尺寸
 */
fun String.toDimenInt(): Int {
    val int = Utils.getApp().resources.getIdentifier(this, "dimen", this)
    return int.toDimenInt()
}

/**
 * 资源名称转尺寸
 */
fun String.toDimenFloat(): Float {
    val int = Utils.getApp().resources.getIdentifier(this, "dimen", this)
    return int.toDimenFloat()
}

/**
 * 将数字转换为PT单位的值
 */
fun Int.toPt(): Int {
    return AdaptScreenUtils.pt2Px(this.toFloat())
}

/**
 * 将px转换为PT
 */
fun Int.pxToPt(): Int {
    return AdaptScreenUtils.px2Pt(this.toFloat())
}

/**
 * 将数字转换为PT单位的值
 */
fun Float.toPt(): Int {
    return AdaptScreenUtils.pt2Px(this)
}