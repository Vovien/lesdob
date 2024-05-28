package com.jmbon.middleware.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import java.lang.reflect.Field


/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/9/13
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class FixedBehavior @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppBarLayout.Behavior(context, attrs) {

    private val TOP_CHILD_FLING_THRESHOLD = 3
    private val isPositive = false

    //fling上滑appbar然后迅速fling下滑recycler时, HeaderBehavior的mScroller并未停止, 会导致上下来回晃动
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (type == ViewCompat.TYPE_NON_TOUCH && topAndBottomOffset == 0) { //recyclerview的惯性比较大 ,会顶在头部一会儿, 到头直接干掉它的滑动
            ViewCompat.stopNestedScroll(target, type)
        }
        super.onNestedPreScroll(coordinatorLayout!!, child!!, target!!, dx, dy, consumed!!, type)
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        if (ev.action == ACTION_DOWN) {
            val scroller = getSuperSuperField(this, "mScroller")
            if (scroller != null && scroller is OverScroller) {
                scroller.abortAnimation()
            }
        }
        return super.onInterceptTouchEvent(parent!!, child!!, ev)
    }

    private fun getSuperSuperField(paramClass: Any, paramString: String): Any? {
        var field: Field? = null
        var `object`: Any? = null
        try {
            field = paramClass.javaClass.superclass.superclass.getDeclaredField(paramString)
            field.isAccessible = true
            `object` = field[paramClass]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return `object`
    }
}