package com.jmbon.middleware.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import com.google.android.flexbox.FlexboxLayout
import com.jmbon.middleware.interpolator.EaseCubicInterpolator


/**
 * @author : leimg
 * time   : 2022/6/22
 * desc   :
 * version: 1.0
 */
object AnimatorUtils {

    fun setScaleAni(V: View, fromScale: Float, toScale: Float, ANITIME: Long) {
        val aniSet = AnimationSet(true)
        // final int ANITIME = 500;

        // 尺寸变化动画，设置尺寸变化
        val scaleAni = ScaleAnimation(
            fromScale,
            toScale,
            1f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.8f,
            Animation.RELATIVE_TO_SELF,
            1f
        )
        scaleAni.duration = ANITIME // 设置动画效果时间
        aniSet.addAnimation(scaleAni) // 将动画效果添加到动画集中
        V.startAnimation(aniSet) // 添加光效动画到控件
    }

    fun expand(v: View) {
        v.measure(LinearLayout.LayoutParams.WRAP_CONTENT, 40.dp)
        val taWight = v.measuredWidth
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                v.layoutParams.width = taWight * interpolatedTime.toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = 300
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredWidth
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.width =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = 300
        v.startAnimation(a)
    }

    fun View.changeWightAnimator(fromScale: Float, toScale: Float) {
        val animatorWxScaleX =ObjectAnimator.ofFloat(this, "scaleX", fromScale, toScale)
        animatorWxScaleX.duration = 300
        animatorWxScaleX.start()

    }


    /**
     * 违规内容动画
     * 缩小
     */
    fun zoomInContentAnimator(targetView: View) {
        var valueAnimator = ValueAnimator.ofFloat(0f, 19f.dp().toFloat())
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            targetView.translationX = value
        }
        valueAnimator.interpolator = EaseCubicInterpolator()
        valueAnimator.start()
    }

    /**
     * 违规内容动画
     * 放大
     */
    fun zoomOutContentAnimator(targetView: View) {
        var valueAnimator = ValueAnimator.ofFloat(19f.dp().toFloat(), 0f)
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            targetView.translationX = value
        }
        valueAnimator.interpolator = EaseCubicInterpolator()
        valueAnimator.start()
    }

    /**
     * 数字增加效果
     * @date 2023/8/8 17:56
     */
    fun TextView.startNumberDanceAnimation(number: Number) {
        val ani = ValueAnimator.ofFloat(0f, number.toFloat()).setDuration(200)
        val format = "%1\$01.${if (number is Float) "2" else "0"}f"
        ani.interpolator = AccelerateInterpolator()
        ani.addUpdateListener {
            text = String.format(format, it.animatedValue)
        }
        ani.start()
    }

    fun FlexboxLayout.startNumberDanceAnimation() {
        val animationSet = AnimatorSet()
        val anMutableList = mutableListOf<Animator>()
        forEach {
            val tv = it as TextView
            if (tv.text != "-") {
                val start = tv.text.toString().toFloat()
                val ani = ValueAnimator.ofFloat(-20f, start)
                ani.interpolator = AccelerateInterpolator()
                ani.addUpdateListener { v ->
                    when (val sss = v.animatedValue.toString().toFloat().toInt()) {
                        in -20..-11 -> tv.text = sss.plus(20).toString()
                        in -10..-1 -> tv.text = sss.plus(10).toString()
                        in 0..9 -> tv.text = sss.toString()
                    }
                }
                anMutableList.add(ani)
            }

        }
        animationSet.playTogether(anMutableList)
        animationSet.interpolator = EaseCubicInterpolator()
        animationSet.duration = 800
        animationSet.start()
    }

    /**
     * 为View添加光效动画
     */
    fun View.startLightAnimation() {
        this.isVisible = true
        this.animation =
            TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f
            ).apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        this@startLightAnimation.isVisible = false
                        this@startLightAnimation.clearAnimation()
                        this@startLightAnimation.postDelayed(1000) {
                            this@startLightAnimation.startLightAnimation()
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }
                })
            }
    }


}