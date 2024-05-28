package com.jmbon.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var isPagingEnabled = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isPagingEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(ev)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item,false)
    }
}