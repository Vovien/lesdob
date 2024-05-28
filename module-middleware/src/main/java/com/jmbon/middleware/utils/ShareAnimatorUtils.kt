package com.jmbon.middleware.utils

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.addListener
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.invisible
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import java.util.Timer
import java.util.TimerTask

/**
 * @author : leimg
 * time   : 2022/10/24
 * desc   :
 * version: 1.0
 */
object ShareAnimatorUtils {

    val CIRCLE_TIME = 6000L

    var timerVideo: Timer? = Timer()
    var taskVideo: TimerTask? = null
    var timerDetail: Timer? = Timer()
    var taskDetail: TimerTask? = null
    fun cancelShareVideoAnimator(iconView: ImageView, tvText: TextView) {
        if (iconView.tag != null && iconView.tag is AnimatorSet) {
            (iconView.tag as AnimatorSet).cancel()
            taskVideo?.cancel()
            iconView.tag = null
        }

    }

    fun cancelShareDetailAnimator(iconView: ImageView, tvText: TextView) {
        if (iconView.tag != null && iconView.tag is AnimatorSet) {
            (iconView.tag as AnimatorSet).cancel()
            animationWxScaleSet?.cancel()
            iconView.tag = null
            taskDetail?.cancel()
            tvText.visible()
            tvText.alpha = 1.0f
            iconView.gone()
            iconView.alpha = 1.0f
        }
    }

    var animationWxScaleSet: AnimatorSet? = null

    fun startShareDetailAnimator(iconView: ImageView, tvText: TextView) {
        if (iconView.tag != null && iconView.tag is AnimatorSet) {
            (iconView.tag as AnimatorSet).cancel()
            taskDetail?.cancel()
            animationWxScaleSet?.cancel()
            iconView.tag = null
        }

        var animationSet = AnimatorSet()

        //微信图标渐变出现
        val animatorWx =
            ObjectAnimator.ofFloat(iconView, "alpha", 0f, 1f)
        //微信图标中心放大
        val animatorWxScaleX =
            ObjectAnimator.ofFloat(iconView, "scaleX", 0f, 1f)
        val animatorWxScaleY =
            ObjectAnimator.ofFloat(iconView, "scaleY", 0f, 1f)
        //微信图标渐变隐藏
        val animatorText =
            ObjectAnimator.ofFloat(tvText, "alpha", 1f, 0f)

        animationSet.interpolator = EaseCubicInterpolator()
        animationSet.duration = 300
        iconView.setTag(animationSet)
        animationSet.addListener(onEnd = {
            tvText.invisible()
            //自身缩放
            //微信图标中心放大
            val animatorWxScaleX =
                ObjectAnimator.ofFloat(iconView, "scaleX", 1f, 0.8f, 1f, 0.8f, 1f, 0.8f, 1f)

            val animatorWxScaleY =
                ObjectAnimator.ofFloat(iconView, "scaleY", 1f, 0.8f, 1f, 0.8f, 1f, 0.8f, 1f)

            animationWxScaleSet = AnimatorSet()
            animationWxScaleSet!!.interpolator = EaseCubicInterpolator()
            animationWxScaleSet!!.duration = CIRCLE_TIME
            animationWxScaleSet!!.playTogether(animatorWxScaleX, animatorWxScaleY)
            animationWxScaleSet!!.addListener(onEnd = {

                resetShareDetailAnimator(iconView, tvText)

                if (iconView.tag != null && iconView.tag is AnimatorSet) {
                    taskDetail = object : TimerTask() {
                        override fun run() {
                            iconView.post {
                                //如果没有tag说明已经主动取消了
                                if (iconView.tag != null && iconView.tag is AnimatorSet) {
                                    startShareDetailAnimator(iconView, tvText)
                                }
                            }
                        }
                    }
                    if (timerDetail == null) {
                        taskDetail?.cancel()
                        taskDetail = null
                    }
                    timerDetail?.schedule(taskDetail, CIRCLE_TIME)
                }

            })
            animationWxScaleSet!!.start()

        }, onCancel = {
            tvText.visible()
            tvText.alpha = 1.0f
            iconView.gone()
            iconView.alpha = 1.0f
        })
        animationSet.playTogether(animatorWx, animatorWxScaleX, animatorWxScaleY, animatorText)
        iconView.visible()
        tvText.visible()
        animationSet.start()


    }

    fun resetShareDetailAnimator(iconView: ImageView, tvText: TextView) {
        var animationSet = AnimatorSet()
        //分享图标渐变出现
        val animatorWx =
            ObjectAnimator.ofFloat(tvText, "alpha", 0f, 1f)
        //分享图标中心放大
        val animatorWxScaleX =
            ObjectAnimator.ofFloat(tvText, "scaleX", 0f, 1f)
        val animatorWxScaleY =
            ObjectAnimator.ofFloat(tvText, "scaleY", 0f, 1f)


        animationSet.playTogether(animatorWx, animatorWxScaleX, animatorWxScaleY)
        animationSet.interpolator = EaseCubicInterpolator()
        animationSet.duration = 300
        tvText.visible()
        iconView.gone()
        animationSet.start()
    }

    fun caseDetailAnimator(tvText: View) {
        val animationSet = AnimatorSet()
        val animatorWx =
            ObjectAnimator.ofFloat(tvText, "alpha", 0.1f, 1f)
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener {
            val evaluator = ArgbEvaluator()
            val value = it.animatedValue as Float
            val s = evaluator.evaluate(
                value, Color.parseColor("#1AFFFFFF"),
                Color.parseColor("#FF5384FF")
            ) as Int
            tvText.setBackground(backgroundColor = s, radius = 7.dp)
        }
//        val colorAnim =
//            ObjectAnimator.ofInt(
//                tvText,
//                "backgroundColor",
//                Color.parseColor("#1AFFFFFF"),
//                Color.parseColor("#FFFE6EAB")
//            )
//        colorAnim.setEvaluator(ArgbEvaluator())
        animationSet.playTogether(valueAnimator, animatorWx)
        animationSet.interpolator = EaseCubicInterpolator()
        animationSet.duration = 500
        animationSet.start()
    }

    fun feedDetailAnimator(tvText: View) {
        val animationSet = AnimatorSet()
        val animatorWx =
            ObjectAnimator.ofFloat(tvText, "alpha", 0f, 1f)
//        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
//        valueAnimator.addUpdateListener {
//            val evaluator = ArgbEvaluator()
//            val value = it.animatedValue as Float
//            val s = evaluator.evaluate(
//                value, Color.parseColor("#FFF5F5F5"),
//                Color.parseColor("#FFFE6EAB")
//            ) as Int
//            tvText.setBackground(backgroundColor = s, radius = 9.dp)
//        }
        animationSet.playTogether(animatorWx)
        animationSet.interpolator = EaseCubicInterpolator()
        animationSet.duration = 500
        animationSet.start()
    }


    fun stopAllAnimator() {
        //taskDetail?.cancel()
        //taskVideo?.cancel()
        timerDetail?.cancel()
        timerDetail = null
        timerVideo?.cancel()
        timerVideo = null

    }

}