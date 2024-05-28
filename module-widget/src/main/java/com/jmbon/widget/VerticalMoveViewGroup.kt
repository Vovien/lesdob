package com.jmbon.widget

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.Nullable
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils


/**
 * @author : leimg
 * time   : 2022/3/31
 * desc   :可以垂直拖动的View
 * version: 1.0
 */
class VerticalMoveViewGroup : FrameLayout {
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var isDrag: Boolean = false
    private var TAG: String = "VerticalMoveViewGroup"
    private var downX: Float = 0f
    private var downY: Float = 0f
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var scaledTouchSlop: Int = 10


    private var lastLeft: Int = 0
    private var lastTop: Int = 0
    private var lastRight: Int = 0
    private var lastBottom: Int = 0
    private var bottomMargin: Int = SizeUtils.dp2px(170f)


    init {
        screenWidth = ScreenUtils.getAppScreenWidth()
        screenHeight = ScreenUtils.getScreenHeight()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        Log.e(
//            "onLayout1",
//            "${changed},${left},${top},${right},${bottom},${screenHeight},${
//                screenHeight - bottomMargin
//            }"
//        )
//        Log.e("onLayout2", "${changed},${lastLeft},${lastTop},${lastRight},${lastBottom}")
        if (bottom != lastBottom && bottom == (screenHeight - bottomMargin)) {
         //   Log.e("onLayout3", "不执行onLayout,${lastLeft},${lastTop},${lastRight},${lastBottom}")
            if (lastLeft != 0) {
                layout(lastLeft, lastTop, lastRight, lastBottom)
            }else{
                super.onLayout(changed, left, top, right, bottom)
            }
        } else {
            super.onLayout(changed, left, top, right, bottom)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (this.isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDrag = false
                    downX = event.x
                    downY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.e("kid", "ACTION_MOVE")
                    val xDistance: Float = event.x - downX
                    val yDistance: Float = event.y - downY
                    var l: Int
                    var r: Int
                    var t: Int
                    var b: Int
                    //只有垂直滑动距离大于10,才算拖动事件
                    // if (Math.abs(xDistance) > 10 || Math.abs(yDistance) > 10) {
                    if (Math.abs(yDistance) > 10) {
                        Log.e("kid", "Drag")
                        isDrag = true
                        // l = (left + xDistance).toInt()
                        l = left
                        r = l + width
                        t = (top + yDistance).toInt()
                        b = t + height
                        //不划出边界判断,此处应按照项目实际情况,因为本项目需求移动的位置是手机全屏,
                        // 所以才能这么写,如果是固定区域,要得到父控件的宽高位置后再做处理
                        if (l < 0) {
                            l = 0
                            r = l + width
                        } else if (r > screenWidth) {
                            r = screenWidth
                            l = r - width
                        }
                        if (t < 0) {
                            t = 0
                            b = t + height
                        } else if (b > screenHeight) {
                            b = screenHeight
                            t = b - height
                        }
                        layout(l, t, r, b)

                        lastLeft = l
                        lastBottom = b
                        lastRight = r
                        lastTop = t
                    }
                }
                MotionEvent.ACTION_UP -> isPressed = false
                MotionEvent.ACTION_CANCEL -> isPressed = false
            }
            return true
        }
        return false
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        val flag = handleEvent(event)
        return if (flag) {
            true
        } else {
            super.dispatchTouchEvent(event)
        }
    }

    @Nullable
    private fun handleEvent(event: MotionEvent): Boolean {
        val result = false
        if (this.isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDrag = false
                    downX = event.x
                    downY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val xDistance: Float = event.x - downX
                    val yDistance: Float = event.y - downY
                    var l: Int
                    var r: Int
                    var t: Int
                    var b: Int
                    // if (Math.abs(xDistance) > scaledTouchSlop || Math.abs(yDistance) > scaledTouchSlop) {
                    //只有垂直滑动距离大于10,才算拖动事件
                    if (Math.abs(yDistance) > scaledTouchSlop) {
                        isDrag = true
                        //   l = (left + xDistance).toInt()
                        l = left
                        r = l + width
                        t = (top + yDistance).toInt()
                        b = t + height
                        //不划出边界判断,此处应按照项目实际情况,因为本项目需求移动的位置是手机全屏,
                        // 所以才能这么写,如果是固定区域,要得到父控件的宽高位置后再做处理
                        if (l < 0) {
                            l = 0
                            r = l + width
                        } else if (r > screenWidth) {
                            r = screenWidth
                            l = r - width
                        }
                        if (t < 0) {
                            t = 0
                            b = t + height
                        } else if (b > screenHeight) {
                            b = screenHeight
                            t = b - height
                        }
                        layout(l, t, r, b)

                        lastLeft = l
                        lastBottom = b
                        lastRight = r
                        lastTop = t
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {}
            }
        }
        return isDrag
    }

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}