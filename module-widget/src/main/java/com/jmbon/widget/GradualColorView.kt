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
 * 渐变色控件，可设置多种颜色位置
 * @author MilkCoder
 * @date 2023/10/11
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
class GradualColorView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private var mBackGroundRect: RectF? = null
    private var backGradient: LinearGradient? = null

    //默认画笔
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintText = Paint()
    var colorlist: List<Int>? = null

    init {
        //设置抗锯齿
        mPaint.isAntiAlias = true
        //设置防抖动
        mPaint.isDither = true
        mPaint.style = Paint.Style.FILL
        mPaintText.isAntiAlias = true
        mPaintText.isDither = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBackGroundRect = RectF(0f, 0f, w.toFloat(), h.toFloat())
        backGradient = LinearGradient(
            0f,
            0f,
           0f,
            h.toFloat(),
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
            canvas.drawRoundRect(mBackGroundRect!!, 20.dp, 20.dp, mPaint)
        }
    }

    val Number.dp
        get() = (this.toFloat() * Resources.getSystem().displayMetrics.density)

}