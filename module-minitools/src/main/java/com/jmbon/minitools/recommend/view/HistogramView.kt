package com.jmbon.minitools.recommend.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.extention.toDrawable
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.R
import com.jmbon.minitools.recommend.bean.ItemBudgetBean

/******************************************************************************
 * Description: 试管价格分布图
 *
 * Author: jhg
 *
 * Date: 2023/5/31
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class HistogramView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val barGap = 2.dp

    private var data: List<ItemBudgetBean> = emptyList()
    private var action: (startIndex: Int, endIndex: Int) -> Unit = { _, _ -> }
    private var maxValue = 0f
    private var currentMinValueIndex = 0
    private var currentMaxValueIndex = 0

    private var startTouchX = 0f
    private var endTouchX = 0f
    private var touchInStart = false
    private var touchInEnd = false
    private var graphHorizontalPadding = 0f

    /**
     * 滑块距离底部的距离(底部字体大小+间距)
     */
    private val iconMarginBottom = 23.dp

    /**
     * 左侧指示器是否到达了右边, 用于处理左侧指示器滑动到最右边难以向左滑动的问题
     */
    private var leftToEnd = false

    private var startIconBitmap: Bitmap = R.drawable.recommend_budget_indicator_icon.toDrawable()?.toBitmap()!!
    private var endIconBitmap: Bitmap = R.drawable.recommend_budget_indicator_icon.toDrawable()?.toBitmap()!!

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val startIconRect = Rect()
    private val endIconRect = Rect()

    private val linePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = R.color.themePrimary.toColorInt()
        strokeWidth = 6.dp.toFloat()
    }

    private val selectedPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#D2DFFF")
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = 14.dp.toFloat()
        isFakeBoldText = true
        color = R.color.color_262626.toColorInt()
    }

    private var barWidth = 0f
    private var minValueStr = ""
    private var maxValueStr = ""
    private var minValueTextWidth = 0f
    private var maxValueTextWidth = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isNullOrEmpty()) {
            return
        }

        // 设置指示器的位置
        if (startIconRect.isEmpty) {
            startIconRect.set(
                graphHorizontalPadding.toInt(),
                (height - startIconBitmap.height - iconMarginBottom),
                (startIconBitmap.width +  graphHorizontalPadding).toInt(), (height - iconMarginBottom)
            )
        }
        if (endIconRect.isEmpty) {
            endIconRect.set(
                (width - graphHorizontalPadding - endIconBitmap.width).toInt(),
                (height - startIconBitmap.height - iconMarginBottom),
                (width - graphHorizontalPadding).toInt(), (height - iconMarginBottom)
            )
        }

        // Draw bars
        barWidth = (width - graphHorizontalPadding * 2f - startIconBitmap.width - (data.size - 1) * barGap) / data.size
        val startX = graphHorizontalPadding + startIconBitmap.width / 2
        val maxBarHeight = height - startIconBitmap.height / 2f - iconMarginBottom - 5.dp
        var startIndexFound = false
        var endIndexFound = false
        for ((index, value) in data.map { it.value }.withIndex()) {
            val x = index * (barWidth + barGap) + startX
            val barHeight = value * maxBarHeight / maxValue
            val rect = RectF(x, maxBarHeight - barHeight, x + barWidth, maxBarHeight)
            paint.color = if (x >= startIconRect.left + startIconBitmap.width / 2 && x <= endIconRect.right - endIconBitmap.width / 2) {
                if (!startIndexFound) {
                    startIndexFound = true
                    currentMinValueIndex = index
                }
                if (!endIndexFound && index == data.size - 1) {
                    endIndexFound = true
                    currentMaxValueIndex = index
                }
                selectedPaint.color
            } else {
                if (!endIndexFound && x > endIconRect.right - endIconBitmap.width / 2) {
                    endIndexFound = true
                    currentMaxValueIndex = index
                }
                R.color.color_e5e5e5.toColorInt()
            }
            canvas.drawRoundRect(rect, 2.dp.toFloat(), 2.dp.toFloat(), paint)
        }

        // Draw bottom line
        val lineTop = maxBarHeight + 2.dp
        canvas.drawRoundRect(startX - 4.dp, lineTop, width - graphHorizontalPadding - endIconBitmap.width / 2f + 4.dp, lineTop + linePaint.strokeWidth, 2.dp.toFloat(), 2.dp.toFloat(), linePaint)

        // Draw start and end icons
        canvas.drawBitmap(startIconBitmap, null, startIconRect, null)
        canvas.drawBitmap(endIconBitmap, null, endIconRect, null)

        // Draw start and end values
        minValueStr = "${data.first().title}元"
        maxValueStr = "${data.last().title}元以上"
        minValueTextWidth = textPaint.measureText(minValueStr)
        maxValueTextWidth = textPaint.measureText(maxValueStr)
        val textY = height - 4.dp.toFloat()
        canvas.drawText(minValueStr, (maxValueTextWidth -  minValueTextWidth) / 2, textY, textPaint)
        canvas.drawText(maxValueStr, width - maxValueTextWidth, textY, textPaint)

        // 动态修改数值
//        minValueStr = "${data[currentMinValueIndex].title}元"
//        maxValueStr = if (currentMaxValueIndex == data.size - 1) {
//            "${data[currentMaxValueIndex].title}元以上"
//        } else {
//            "${data[currentMaxValueIndex].title}元"
//        }
//        minValueTextWidth = textPaint.measureText(minValueStr)
//        maxValueTextWidth = textPaint.measureText(maxValueStr)
//        val textY = height - 4.dp.toFloat()
//        canvas.drawText(minValueStr, startIconRect.centerX() - minValueTextWidth / 2f,
//            textY, textPaint)
//        canvas.drawText(maxValueStr, endIconRect.centerX() - maxValueTextWidth / 2f, textY, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Check if the touch point is on the start icon or end icon
                LogUtils.i("=============in x==${event.x} =endRect==${endIconRect.left}-${endIconRect.right}====startRect${startIconRect.left}-${startIconRect.right}")
                if (startIconRect.contains(event.x.toInt(), event.y.toInt())) {
                    startTouchX = event.x
                    touchInStart = true
                }
                if (endIconRect.contains(event.x.toInt(), event.y.toInt())) {
                    endTouchX = event.x
                    touchInEnd = true
                }

                return if (touchInStart || touchInEnd) {
                    true
                } else {
                    super.onTouchEvent(event)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // Check if we are scrolling or dragging the start/end icons
                if (touchInStart) {
                    val dx = event.x - startTouchX
                    if (dx > 0) {
                        if (!touchInEnd) {
                            val maxRight = endIconRect.right - 8.dp
                            if (startIconRect.right + dx <= maxRight) {
                                startIconRect.offset(dx.toInt(), 0)
                            } else {
                                startIconRect.offset((maxRight - startIconRect.right), 0)
                            }
                        }
                    } else if (dx < 0){
                        if (startIconRect.left + dx >= graphHorizontalPadding) {
                            touchInEnd = false
                            startIconRect.offset(dx.toInt(), 0)
                        } else {
                            startIconRect.set(
                                graphHorizontalPadding.toInt(), startIconRect.top,
                                (graphHorizontalPadding + startIconBitmap.width).toInt(), startIconRect.bottom)
                            //startIconRect.offset((startIconRect.left - graphHorizontalPadding).toInt(), 0)
                        }
                    }
                    startTouchX = event.x
                }
                if (touchInEnd) {
                    val dx = event.x - endTouchX
                    if (dx > 0) {
                        if (endIconRect.right + dx <= width - graphHorizontalPadding) {
                            touchInStart = false
                            endIconRect.offset(dx.toInt(), 0)
                        } else {
                            endIconRect.offset((width -graphHorizontalPadding - endIconRect.right).toInt(), 0)
                        }
                    } else if (dx < 0){
                        if (!touchInStart) {
                            val minLeft = startIconRect.left + 8.dp
                            if (endIconRect.left + dx >= minLeft) {
                                endIconRect.offset(dx.toInt(), 0)
                            } else {
                                endIconRect.offset(minLeft - endIconRect.left, 0)
                            }
                        }
                    }
                    endTouchX = event.x
                }
                invalidate()
                post {
                    if (currentMinValueIndex >= 0 && currentMaxValueIndex >= 0) {
                        action(currentMinValueIndex, currentMaxValueIndex)
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                touchInStart = false
                touchInEnd = false
            }
        }
        return super.onTouchEvent(event)
    }

    fun setData(data: List<ItemBudgetBean>, action: (startIndex: Int, endIndex: Int) -> Unit) {
        if (data.isEmpty()) {
            return
        }

        this.data = data
        this.action = action
        maxValue = data.maxOfOrNull { it.value } ?: 0f
        currentMinValueIndex = 0
        currentMaxValueIndex = data.size - 1
        action(currentMinValueIndex, currentMaxValueIndex)
        val realMaxValueTextWidth = textPaint.measureText("${data.last().title}元以上")
        graphHorizontalPadding = (realMaxValueTextWidth - endIconRect.width()) / 4f
        invalidate()
    }
}