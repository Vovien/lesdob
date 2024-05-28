package com.jmbon.middleware.utils

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class ToastHasMessageDetectorListener(val dismiss: (Boolean) -> Unit) :
    GestureDetector.SimpleOnGestureListener() {

    /**
     * 屏幕拖动事件，如果按下的时间过长，调用了onLongPress，再拖动屏幕不会触发onScroll。
     * 拖动屏幕会多次触发
     * @param motionEvent 开始拖动的第一次按下down操作,也就是第一个ACTION_DOWN
     * @param motionEvent1 触发当前onScroll方法的ACTION_MOVE
     * @param distanceX 当前的x坐标与最后一次触发scroll方法的x坐标的差值。
     * @param distanceY 当前的y坐标与最后一次触发scroll方法的y坐标的差值。
     * @return
     */
    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        hideKey(distanceX, distanceY)
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    private fun hideKey(distanceX: Float, distanceY: Float) {
        val x = abs(distanceX)
        val y = abs(distanceY)
        if (y > x && distanceY < 0)
           dismiss(true)
    }


    /**
     * 按下屏幕，在屏幕上快速滑动后松开，由一个down,多个move,一个up触发
     * @param motionEvent 开始快速滑动的第一次按下down操作,也就是第一个ACTION_DOWN
     * @param motionEvent1 触发当前onFling方法的move操作,也就是最后一个ACTION_MOVE
     * @param velocityX X轴上的移动速度，像素/秒
     * @param velocityY Y轴上的移动速度，像素/秒
     * @return
     */
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        hideKey(velocityX, velocityY)
        return super.onFling(e1, e2, velocityX, velocityY)
    }
}