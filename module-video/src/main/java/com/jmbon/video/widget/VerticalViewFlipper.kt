package com.jmbon.video.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import com.jmbon.middleware.bean.VideoAdv
import com.jmbon.middleware.utils.load
import com.jmbon.video.R

/**
 *
 * @author MilkCoder
 * @date 2023/9/27
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
class VerticalViewFlipper @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ViewFlipper(context, attributeSet) {

    var mData = arrayListOf<VideoAdv>()

    init {
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.animation_down_up_in_animation)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.animation_down_up_out_animation)
        isAutoStart = true
        flipInterval = 10000
    }

    fun setData(data: List<VideoAdv>) {
        stopFlipping()
        mData.clear()
        removeAllViews()
        mData.addAll(data)
        mData.forEach {
            val childLayout = LayoutInflater.from(context).inflate(R.layout.view_video_adv, null)
            childLayout.findViewById<ImageView>(R.id.imgtRelatedArticles).load(it.icon)
            childLayout.findViewById<TextView>(R.id.textSwitcher).text = it.title
            addView(childLayout) //添加childLayout
        }
        startFlipping()
    }

    fun getCurrentData(): VideoAdv? {
        val s = currentView.findViewById<TextView>(R.id.textSwitcher)
        return mData.find { it.title == s.text }
    }

    override fun startFlipping() {
        if (mData.size <= 1) return
        super.startFlipping()
    }

    override fun stopFlipping() {
        if (mData.size <= 1) return
        super.stopFlipping()
    }
}