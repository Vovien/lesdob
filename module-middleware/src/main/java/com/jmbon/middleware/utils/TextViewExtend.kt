package com.jmbon.middleware.utils

import android.text.DynamicLayout
import android.text.Layout
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.TextView


/**
 * textview 的行数
 */
fun TextView.lineCountBeforeInit(string: String): Int {
    val mDynamicLayout = DynamicLayout(
        string, paint, this.width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.0f, true
    )
    return mDynamicLayout.lineCount
}


/**
 * text view 展开收起动画
 */
fun TextView.textAnimation(startValue: Int, endValue: Int, showd: String, maxLine: Int = 0, endStr: String = "") {
    val animation: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            this@textAnimation.height = (startValue + endValue * interpolatedTime).toInt()
        }
    }
    animation.duration = 100
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            this@textAnimation.text = showd
            if (maxLine > 0)
                setFoldEllipsize(maxLine, endStr)
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })
    this.startAnimation(animation)
}

/**
 * 指示器开始伸展动画
 */
fun ImageView.extendIndicatorAnimation(indicator: TextView, openStr: String) {
    val animation: Animation =
        RotateAnimation(
            0f, 180f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
    animation.duration = 300
    animation.fillAfter = true
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            indicator.text = openStr
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })
    this.startAnimation(animation)
}

/**
 * 指示器开始折叠动画
 */
fun ImageView.foldIndicatorAnimator(indicator: TextView, closeStr: String) {
    val animation: Animation =
        RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    animation.fillAfter = true
    animation.duration = 300
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            indicator.text = closeStr
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })
    this.startAnimation(animation)
}

/**
 * 根据行数计算 textview 的高度
 */
fun TextView.calculateHeight(lineCount: Int): Int {
    return if (this.layout != null) this.layout.getLineTop(lineCount) + this.compoundPaddingTop + this.compoundPaddingBottom else 0
}

/**
 * 给 TextView 收起状态添加省略号
 */
fun TextView.setFoldEllipsize(maxLine: Int, endStr: String) {

    val end: Int = this.layout.getLineEnd(maxLine - 1)
    val mText = this.text.toString()
    val sb: StringBuilder = StringBuilder(this.text.toString())
    if (mText.length > end) {
        sb.replace(end - 1, end, endStr)
    }
    this.text = sb.toString()
}