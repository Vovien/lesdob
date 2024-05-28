package com.jmbon.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

/**
 * 彩虹登录样式
 * @author MilkCoder
 * @date 2023/10/11
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
class RainbowLoginView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private var mBackGroundRect: RectF? = null
    private var backGradient: LinearGradient? = null
    private val strokeWidth = 2.dp

    //默认画笔
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        //设置抗锯齿
        mPaint.isAntiAlias = false
        //设置防抖动
        mPaint.isDither = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBackGroundRect = RectF(0f + strokeWidth, 0f + strokeWidth, w.toFloat() - strokeWidth, h.toFloat() - strokeWidth)
        backGradient = LinearGradient(
            0f,
            0f,
            w.toFloat(),
            0f,
            intArrayOf(
                Color.parseColor("#FFE02020"),
                Color.parseColor("#FFFA6400"),
                Color.parseColor("#FFF7B500"),
                Color.parseColor("#FF6DD400"),
                Color.parseColor("#FF0091FF"),
                Color.parseColor("#FF6236FF"),
                Color.parseColor("#FFB620E0")
            ),
            null,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.shader = backGradient
        //绘制背景 圆角矩形
        if (mBackGroundRect != null) {
            canvas.drawRoundRect(mBackGroundRect!!, 16.dp, 16.dp, mPaint)
        }
    }

    val Number.dp
        get() = (this.toFloat() * Resources.getSystem().displayMetrics.density)

}