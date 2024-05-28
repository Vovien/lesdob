package com.jmbon.middleware.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout


/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/9/13
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class CustomCoordinatorLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CoordinatorLayout(context, attrs) {

    private var mListener: OnInterceptTouchListener? = null

    fun setOnInterceptTouchListener(listener: OnInterceptTouchListener?) {
        mListener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (mListener != null) {
            mListener!!.onIntercept()
        }
        return super.onInterceptTouchEvent(ev)
    }


    interface OnInterceptTouchListener {
        fun onIntercept()
    }
}