package com.jmbon.middleware.dialog

import android.content.Context
import android.view.MotionEvent
import android.view.animation.Animation
import com.apkdv.mvvmfast.utils.SizeUtil
import com.jmbon.middleware.R
import com.lxj.xpopup.impl.FullScreenPopupView

class GifGuidePopup(context: Context) : FullScreenPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.article_gif_guide
    }

    override fun startAnimation(animation: Animation?) {

    }

    override fun doDismissAnimation() {
    }

    override fun doShowAnimation() {

    }


    private var x1 = 0f
    private var y1 = 0f
    private val scrollWidth by lazy { SizeUtil.getWidth() * 0.2 }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            x1 = ev.x
            y1 = ev.y
        }
        return if (ev.action == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            val xScroll = x1 - ev.x
            if (xScroll > scrollWidth && y1 - ev.y < xScroll) {
                dismiss()
                true
            } else super.dispatchTouchEvent(ev)
        } else super.dispatchTouchEvent(ev)
    }


}