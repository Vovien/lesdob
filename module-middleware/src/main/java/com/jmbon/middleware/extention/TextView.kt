package com.jmbon.middleware.extention

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.DrawableRes

/**
 * @Description：扩展TextView
 * @Author： jhg
 * @Time： 2022/3/21
 * @Version 1.0.0
 */
/**
 * 设置TextView左侧的图片
 * @param drawableResId: 图片资源
 * @param width: 图片的宽度
 * @param height: 图片的高度
 */
fun TextView.setLeftDrawable(@DrawableRes drawableResId: Int, width: Int = -1, height: Int = -1): TextView {
    getDrawable(drawableResId, width, height)?.let {
        setCompoundDrawables(it, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }
    return this
}

/**
 * 设置TextView顶部的图片
 * @param drawableResId: 图片资源
 * @param width: 图片的宽度
 * @param height: 图片的高度
 */
fun TextView.setTopDrawable(@DrawableRes drawableResId: Int, width: Int = -1, height: Int = -1): TextView {
    getDrawable(drawableResId, width, height)?.let {
        setCompoundDrawables(compoundDrawables[0], it, compoundDrawables[2], compoundDrawables[3])
    }
    return this
}

/**
 * 设置TextView右侧的图片
 * @param drawableResId: 图片资源
 * @param width: 图片的宽度
 * @param height: 图片的高度
 */
fun TextView.setRightDrawable(@DrawableRes drawableResId: Int, width: Int = -1, height: Int = -1): TextView {
    getDrawable(drawableResId, width, height)?.let {
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], it, compoundDrawables[3])
    }
    return this
}

/**
 * 设置TextView底部的图片
 * @param drawableResId: 图片资源
 * @param width: 图片的宽度
 * @param height: 图片的高度
 */
fun TextView.setBottomDrawable(@DrawableRes drawableResId: Int, width: Int = -1, height: Int = -1): TextView {
    getDrawable(drawableResId, width, height)?.let {
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], it)
    }
    return this
}

/**
 * 获取指定大小的Drawable
 * @param drawableResId: 图片资源
 * @param width: 图片的宽度
 * @param height: 图片的高度
 */
private fun getDrawable(@DrawableRes drawableResId: Int, width: Int = -1, height: Int = -1): Drawable? {
    if (drawableResId == 0) {
        return null
    }

    val drawable = drawableResId.toDrawable()
    drawable?.let {
        if (width > 0 && height > 0) {
            it.setBounds(0, 0, width, height)
        } else {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        }
    }
    return drawable
}