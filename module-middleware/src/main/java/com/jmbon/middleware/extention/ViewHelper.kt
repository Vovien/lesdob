package com.jmbon.middleware.extention

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.LinearGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 设置View背景
 * @param shape: View背景的形状，默认为矩形
 * @param orientation: 背景的渐变方向
 * @param gradientColor: 渐变颜色数组, 依次为开始颜色、中间颜色（可忽略）、结束颜色
 * @param backgroundColor: 背景色
 * @param borderColor: 边框颜色
 * @param borderGradientColors: 边框渐变色
 * @param borderOrientation: 边框渐变方向
 * @param borderWidth: 边框宽度
 * @param radius: 圆角大小
 * @param cornerRadii: 圆角大小，与radius不同的是，4个角可设置不同的值，所以该参数必须为大小为4的数组，
 * 依次标识左上，右上，右下，左下角的圆角大小。
 */
fun View.setBackground(
    @Shape shape: Int = GradientDrawable.RECTANGLE,
    orientation: GradientDrawable.Orientation? = null,
    @ColorInt gradientColor: IntArray = intArrayOf(),
    @ColorInt backgroundColor: Int = 0,
    @ColorInt borderColor: Int = 0,
    @ColorInt borderGradientColors: IntArray = intArrayOf(),
    borderOrientation: GradientDrawable.Orientation? = null,
    borderWidth: Int = 0,
    radius: Int = 0,
    cornerRadii: FloatArray = floatArrayOf(),
) {
    ViewCompat.setBackground(
        this, getDrawable(
            shape = shape,
            orientation = orientation,
            gradientColor = gradientColor,
            backgroundColor = backgroundColor,
            borderColor = borderColor,
            borderGradientColors = borderGradientColors,
            borderOrientation = borderOrientation,
            borderWidth = borderWidth,
            radius = radius,
            cornerRadii = cornerRadii
        )
    )
}

/**
 * 创建Drawable对象
 * @param shape: View背景的形状，默认为矩形
 * @param orientation: 背景的渐变方向
 * @param gradientColor: 渐变颜色数组, 依次为开始颜色、中间颜色（可忽略）、结束颜色
 * @param backgroundColor: 背景色
 * @param borderColor: 边框颜色
 * @param borderGradientColors: 边框渐变色
 * @param borderOrientation: 边框渐变方向
 * @param borderWidth: 边框宽度
 * @param radius: 圆角大小
 * @param cornerRadii: 圆角大小，与radius不同的是，4个角可设置不同的值，所以该参数必须为大小为4的数组，
 * 依次标识左上，右上，右下，左下角的圆角大小。
 */
fun getDrawable(
    @Shape shape: Int = GradientDrawable.RECTANGLE,
    orientation: GradientDrawable.Orientation? = null,
    @ColorInt gradientColor: IntArray = intArrayOf(),
    @ColorInt backgroundColor: Int = 0,
    @ColorInt borderColor: Int = 0,
    @ColorInt borderGradientColors: IntArray = intArrayOf(),
    borderOrientation: GradientDrawable.Orientation? = null,
    borderWidth: Int = 0,
    radius: Int = 0,
    cornerRadii: FloatArray = floatArrayOf(),
): Drawable {
    if (cornerRadii.isNotEmpty() && cornerRadii.size != 4) {
        throw ArrayIndexOutOfBoundsException("cornerRadii must have four values")
    }
    val drawable = if (orientation != null && gradientColor.size > 1) {
        GradientDrawable(orientation, gradientColor)
    } else {
        GradientDrawable().apply {
            setColor(backgroundColor)
        }
    }
    drawable.shape = shape
    // 设置圆角
    if (radius > 0) {
        drawable.cornerRadius = radius.toFloat()
    } else if (cornerRadii.size == 4) {
        drawable.cornerRadii = floatArrayOf(
            cornerRadii[0], cornerRadii[0], cornerRadii[1], cornerRadii[1],
            cornerRadii[2], cornerRadii[2], cornerRadii[3], cornerRadii[3]
        )
    }

    // 设置边框
    if (borderOrientation != null && borderGradientColors.size > 1) {
        return LayerDrawable(
            arrayOf(
                drawable,
                createGradientBorderDrawable(
                    strokeWidth = borderWidth.toFloat(),
                    strokeOrientation = borderOrientation,
                    strokeColors = borderGradientColors,
                    radius = radius.toFloat(),
                    radii = cornerRadii
                )
            )
        )
    } else {
        drawable.setStroke(borderWidth, borderColor)
    }

    return drawable
}

/**
 * 根据View的状态为View设置不同的背景
 * @param state: View的状态，默认对应isEnabled属性
 * @param shape: View背景的形状，默认为矩形
 * @param orientation: 背景的渐变方向
 * @param falseGradientColor: 状态对应的属性值为false时的渐变色, 依次为开始颜色、中间颜色（可忽略）、结束颜色
 * @param trueGradientColor: 状态对应的属性值为true时的渐变色, 依次为开始颜色、中间颜色（可忽略）、结束颜色
 * @param falseBackgroundColor: 状态对应的属性值为false时的背景色
 * @param trueBackgroundColor: 状态对应的属性值为true时的背景色
 * @param falseBorderColor: 状态对应的属性值为false时的边框颜色
 * @param trueBorderColor: 状态对应的属性值为true时的边框颜色
 * @param borderWidth: 边框宽度，只设置次值时两种状态下边框宽度一样
 * @param falseBorderWidth: 状态对应的属性值为false时的边框的宽度
 * @param trueBorderWidth: 状态对应的属性值为true时的边框的宽度
 * @param radius: 圆角大小
 * @param cornerRadii: 圆角大小，与radius不同的是，4个角可设置不同的值，所以该参数必须为大小为4的数组，
 * 依次标识左上，右上，右下，左下角的圆角大小。
 */
fun View.setStateBackground(
    @State state: Int = android.R.attr.state_enabled,
    @Shape shape: Int = GradientDrawable.RECTANGLE,
    orientation: GradientDrawable.Orientation? = null,
    @ColorInt falseGradientColor: IntArray = intArrayOf(),
    @ColorInt trueGradientColor: IntArray = intArrayOf(),
    @ColorInt falseBackgroundColor: Int = 0,
    @ColorInt trueBackgroundColor: Int = 0,
    @ColorInt falseBorderColor: Int = 0,
    @ColorInt trueBorderColor: Int = 0,
    borderWidth: Int = 0,
    falseBorderWidth: Int = borderWidth,
    trueBorderWidth: Int = borderWidth,
    radius: Int = 0,
    cornerRadii: FloatArray = floatArrayOf(),
) {
    ViewCompat.setBackground(
        this, getDrawable(
            state = state,
            shape = shape,
            orientation = orientation,
            falseGradientColor = falseGradientColor,
            trueGradientColor = trueGradientColor,
            falseBackgroundColor = falseBackgroundColor,
            trueBackgroundColor = trueBackgroundColor,
            falseBorderColor = falseBorderColor,
            trueBorderColor = trueBorderColor,
            borderWidth = borderWidth,
            falseBorderWidth = falseBorderWidth,
            trueBorderWidth = trueBorderWidth,
            radius = radius,
            cornerRadii = cornerRadii
        )
    )
}

/**
 * 根据属性创建对应的Drawable对象
 * @param state: View的状态，默认对应isEnabled属性
 * @param shape: View背景的形状，默认为矩形
 * @param orientation: 背景的渐变方向
 * @param falseGradientColor: 状态对应的属性值为false时的渐变色, 依次为开始颜色、中间颜色（可忽略）、结束颜色
 * @param trueGradientColor: 状态对应的属性值为true时的渐变色, 依次为开始颜色、中间颜色（可忽略）、结束颜色
 * @param falseBackgroundColor: 状态对应的属性值为false时的背景色
 * @param trueBackgroundColor: 状态对应的属性值为true时的背景色
 * @param falseBorderColor: 状态对应的属性值为false时的边框颜色
 * @param trueBorderColor: 状态对应的属性值为true时的边框颜色
 * @param borderWidth: 边框宽度，只设置次值时两种状态下边框宽度一样
 * @param falseBorderWidth: 状态对应的属性值为false时的边框的宽度
 * @param trueBorderWidth: 状态对应的属性值为true时的边框的宽度
 * @param radius: 圆角大小
 * @param cornerRadii: 圆角大小，与radius不同的是，4个角可设置不同的值，所以该参数必须为大小为4的数组，
 * 依次标识左上，右上，右下，左下角的圆角大小。
 */
fun getDrawable(
    @State state: Int = android.R.attr.state_enabled,
    @Shape shape: Int = GradientDrawable.RECTANGLE,
    orientation: GradientDrawable.Orientation? = null,
    @ColorInt falseGradientColor: IntArray = intArrayOf(),
    @ColorInt trueGradientColor: IntArray = intArrayOf(),
    @ColorInt falseBackgroundColor: Int = 0,
    @ColorInt trueBackgroundColor: Int = 0,
    @ColorInt falseBorderColor: Int = 0,
    @ColorInt trueBorderColor: Int = 0,
    borderWidth: Int = 0,
    falseBorderWidth: Int = borderWidth,
    trueBorderWidth: Int = borderWidth,
    radius: Int = 0,
    cornerRadii: FloatArray = floatArrayOf(),
): Drawable {
    val drawable = StateListDrawable()
    drawable.addState(
        intArrayOf(state),
        getDrawable(
            shape = shape,
            orientation = orientation,
            gradientColor = trueGradientColor,
            backgroundColor = trueBackgroundColor,
            borderColor = trueBorderColor,
            borderWidth = trueBorderWidth,
            radius = radius,
            cornerRadii = cornerRadii
        )
    )
    drawable.addState(
        intArrayOf(),
        getDrawable(
            shape = shape,
            orientation = orientation,
            gradientColor = falseGradientColor,
            backgroundColor = falseBackgroundColor,
            borderColor = falseBorderColor,
            borderWidth = falseBorderWidth,
            radius = radius,
            cornerRadii = cornerRadii
        )
    )
    return drawable
}

/**
 * 根据View的状态为View设置不同的背景
 * @param context
 * @param state: View的状态，默认对应isSelected属性
 * @param falseBackgroundResId: 状态对应的属性值为false时的背景图片
 * @param trueBackgroundResId: 状态对应的属性值为true时的背景图片
 */
fun View.setStateBackground(
    context: Context,
    @State state: Int = android.R.attr.state_selected,
    @DrawableRes falseBackgroundResId: Int = 0,
    @DrawableRes trueBackgroundResId: Int = 0
) {
    ViewCompat.setBackground(
        this,
        getDrawable(context, state, falseBackgroundResId, trueBackgroundResId)
    )
}

/**
 * 根据属性创建对应的Drawable对象
 * @param context
 * @param state: View的状态，默认对应isSelected属性
 * @param falseBackgroundResId: 状态对应的属性值为false时的背景图片
 * @param trueBackgroundResId: 状态对应的属性值为true时的背景图片
 */
fun getDrawable(
    context: Context,
    @State state: Int = android.R.attr.state_selected,
    @DrawableRes falseBackgroundResId: Int = 0,
    @DrawableRes trueBackgroundResId: Int = 0
): Drawable {
    val drawable = StateListDrawable()
    drawable.addState(
        intArrayOf(state),
        ContextCompat.getDrawable(context, trueBackgroundResId)
    )
    drawable.addState(
        intArrayOf(),
        ContextCompat.getDrawable(context, falseBackgroundResId)
    )
    return drawable
}

/**
 * @param state: View的状态
 * @param falseTextColor: 表示状态为false时的颜色
 * @param trueTextColor: 表示状态为true时的颜色
 */
fun TextView.setTextColor(
    @State state: Int,
    @ColorInt falseTextColor: Int,
    @ColorInt trueTextColor: Int
) {
    val colorState = ColorStateList(
        arrayOf(intArrayOf(state), intArrayOf()),
        intArrayOf(trueTextColor, falseTextColor)
    )
    setTextColor(colorState)
}

/**
 * 创建分割线
 * @param color: 分割线的颜色
 * @param width: 分割线的宽度
 * @param height: 分割线的高度
 * @return
 */
fun getDivider(color: Int, width: Int, height: Int): Drawable {
    return object : ColorDrawable(color) {
        override fun getIntrinsicWidth(): Int {
            return if (width != 0) width else super.getIntrinsicWidth()
        }

        override fun getIntrinsicHeight(): Int {
            return if (height != 0) height else super.getIntrinsicHeight()
        }
    }
}

/**
 * 创建渐变色边框的Drawable
 * @param strokeColors: 边框颜色
 * @param strokeWidth: 边框宽度
 * @param radius: 边框圆角
 */
fun createGradientBorderDrawable(
    strokeColors: IntArray,
    strokeWidth: Float,
    radius: Float
): ShapeDrawable {
    // 内部矩形与外部矩形的距离
    val inset = RectF(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
    // 内部矩形弧度
    val innerRadius = radius - strokeWidth
    val innerRadii = floatArrayOf(
        innerRadius,
        innerRadius,
        innerRadius,
        innerRadius,
        innerRadius,
        innerRadius,
        innerRadius,
        innerRadius
    )
    val outerR = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
    val rr = RoundRectShape(outerR, inset, innerRadii)
    val shaderFactory = object : ShapeDrawable.ShaderFactory() {
        override fun resize(width: Int, height: Int): Shader {
            return LinearGradient(
                width / 2f,
                0f,
                width / 2f,
                height.toFloat(),
                strokeColors,
                null,
                Shader.TileMode.CLAMP
            )
        }
    }
    val shapeDrawable = ShapeDrawable(rr)
    shapeDrawable.shaderFactory = shaderFactory
    return shapeDrawable
}

/**
 * 创建渐变色边框的Drawable
 * @param strokeColors: 边框颜色
 * @param strokeWidth: 边框宽度
 * @param radius: 边框圆角
 * @param radii: 边框圆角, 与radius不同的是，4个角可设置不同的值，所以该参数必须为大小为4的数组，
 * 依次标识左上，右上，右下，左下角的圆角大小。
 */
fun createGradientBorderDrawable(
    strokeWidth: Float,
    strokeOrientation: GradientDrawable.Orientation,
    strokeColors: IntArray,
    radius: Float,
    radii: FloatArray
): ShapeDrawable {
    val inset = RectF(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
    val outerRadii = if (radii.size == 4) {
        floatArrayOf(radii[0], radii[0], radii[1], radii[1], radii[2], radii[2], radii[3], radii[3])
    } else {
        floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
    }
    val innerRadii = outerRadii.map { it - strokeWidth }.toFloatArray()
    val shape = RoundRectShape(outerRadii, inset, innerRadii)
    val shaderFactory = object : ShapeDrawable.ShaderFactory() {
        override fun resize(width: Int, height: Int): Shader {
            return getShader(
                strokeOrientation,
                strokeColors,
                RectF(0f, 0f, width.toFloat(), height.toFloat())
            )
        }
    }
    val shapeDrawable = ShapeDrawable(shape)
    shapeDrawable.shaderFactory = shaderFactory
    return shapeDrawable
}

/**
 * 根据渐变方向获取Shader
 * @param orientation: 渐变方向
 * @param gradientColors: 渐变颜色
 * @param rect: 渐变区域
 */
fun getShader(
    orientation: GradientDrawable.Orientation,
    gradientColors: IntArray,
    rect: RectF
): Shader {
    return when (orientation) {
        GradientDrawable.Orientation.LEFT_RIGHT -> {
            LinearGradient(
                rect.left, rect.height() / 2, rect.right,
                rect.height() / 2, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
        GradientDrawable.Orientation.TOP_BOTTOM -> {
            LinearGradient(
                rect.width() / 2, rect.top, rect.width() / 2,
                rect.bottom, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
        GradientDrawable.Orientation.RIGHT_LEFT -> {
            LinearGradient(
                rect.right, rect.height() / 2, rect.left,
                rect.height() / 2, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
        GradientDrawable.Orientation.BOTTOM_TOP -> {
            LinearGradient(
                rect.width() / 2, rect.bottom, rect.width() / 2,
                rect.top, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
        GradientDrawable.Orientation.TL_BR -> {
            LinearGradient(
                rect.left, rect.top, rect.right,
                rect.bottom, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
        GradientDrawable.Orientation.TR_BL -> {
            LinearGradient(
                rect.right, rect.top, rect.left,
                rect.bottom, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
        GradientDrawable.Orientation.BL_TR -> {
            LinearGradient(
                rect.left, rect.bottom, rect.right,
                rect.top, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
        GradientDrawable.Orientation.BR_TL -> {
            LinearGradient(
                rect.right, rect.bottom, rect.left,
                rect.top, gradientColors, null, Shader.TileMode.CLAMP
            )
        }
    }
}

/**
 * View背景的形状取值
 */
@IntDef(
    GradientDrawable.RECTANGLE,
    GradientDrawable.OVAL,
    GradientDrawable.LINE,
    GradientDrawable.RING
)
@Retention(
    RetentionPolicy.SOURCE
)
annotation class Shape

/**
 * View状态取值
 */
@IntDef(
    R.attr.state_above_anchor,
    R.attr.state_accelerated,
    R.attr.state_activated,
    R.attr.state_active,
    R.attr.state_checkable,
    R.attr.state_checked,
    R.attr.state_drag_can_accept,
    R.attr.state_drag_hovered,
    R.attr.state_empty,
    R.attr.state_enabled,
    R.attr.state_expanded,
    R.attr.state_first,
    R.attr.state_focused,
    R.attr.state_hovered,
    R.attr.state_last,
    R.attr.state_middle,
    R.attr.state_multiline,
    R.attr.state_pressed,
    R.attr.state_selected,
    R.attr.state_single,
    R.attr.state_window_focused
)
@Retention(
    RetentionPolicy.SOURCE
)
annotation class State

