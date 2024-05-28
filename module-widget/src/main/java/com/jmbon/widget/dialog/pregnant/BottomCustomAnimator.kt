package com.jmbon.widget.dialog.pregnant

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.LogUtils
import com.lxj.xpopup.animator.PopupAnimator

/**
 * @author : wangzhen
 * time   : 2021/3/26
 * desc   : 底部弹框动画
 * version: 1.0
 */
class BottomCustomAnimator : PopupAnimator() {

    //动画起始坐标
    private var startTranslationX = 0f   //动画起始坐标
    private var startTranslationY = 0f
    private var oldWidth = 0
    private var oldHeight: Int = 0
    private var initTranslationX = 0f
    private var initTranslationY: kotlin.Float = 0f
    var hasInitDefTranslation = false

    override fun animateShow() {
        var animator: ViewPropertyAnimator? = null
        targetView.translationY =
            (targetView.parent as View).measuredHeight - targetView.top
                .toFloat()
        animator = targetView.animate().translationY(initTranslationY)
        animator?.setInterpolator(LinearInterpolator())
            ?.setDuration(500)?.withLayer()?.start()
        LogUtils.e("animateShow")
    }

    override fun animateDismiss() {
        var animator: ViewPropertyAnimator? = null
        startTranslationY += targetView.measuredHeight - oldHeight.toFloat()
        animator = targetView.animate().translationY(startTranslationY)
        animator?.setInterpolator(LinearInterpolator())
            ?.setDuration(5000)?.withLayer()?.start()
        LogUtils.e("animateDismiss")
    }

    override fun initAnimator() {
        LogUtils.e("animateShow")

        if (!hasInitDefTranslation) {
            initTranslationX = targetView.translationX
            initTranslationY = targetView.translationY
            hasInitDefTranslation = true
        }
        // 设置起始坐标
        // 设置起始坐标
        targetView.translationY = (targetView.parent as View).measuredHeight - targetView.top
            .toFloat()
        startTranslationX = targetView.translationX
        startTranslationY = targetView.translationY

        oldWidth = targetView.measuredWidth
        oldHeight = targetView.measuredHeight
    }
}