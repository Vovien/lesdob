package com.jmbon.video.widget

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import com.jmbon.middleware.bean.VideoAdv
import com.jmbon.middleware.utils.load
import com.jmbon.video.R

class PictureTextSwitcher @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ViewSwitcher(context, attributeSet) {
    private var mCurrentIndex = 0
    var mData = arrayListOf<VideoAdv>()
    private var isFlipping = false

    private val mHandler = Handler()
    private val runnable = Runnable {
        if (!isFlipping) return@Runnable
        showNext()
        if (mCurrentIndex + 1 > 2) {
            mCurrentIndex = 0
        } else {
            mCurrentIndex++
        }
        startFlipping()
    }

    init {
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.animation_down_up_in_animation)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.animation_down_up_out_animation)
        setFactory {
            LayoutInflater.from(context)
                .inflate(R.layout.view_video_adv, this@PictureTextSwitcher, false)
        }
    }

    fun getCurrentData(): VideoAdv? {
        return try {
            if (mData.size > 0) {
                Log.e("www mCurrentIndex", mCurrentIndex.toString())
                mData[mCurrentIndex]
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    fun setData(data: List<VideoAdv>) {
        stopFlipping()
        mData.clear()
        mData.addAll(data)
        if (mData.size <= 0) return
        setViewData(currentView, 0)
        if (mData.size <= 1) return
        setViewData(nextView, 1)
        startFlipping()
    }

    private fun setViewData(itemView: View, pos: Int) {
        if (itemView is LinearLayout) {
            mCurrentIndex = pos
            itemView.findViewById<ImageView>(R.id.imgtRelatedArticles).load(mData[pos].icon)
            itemView.findViewById<TextView>(R.id.textSwitcher).text = mData[pos].title
        }
    }

    //开启信息轮播
    fun startFlipping() {
        if (mData.size <= 1) return
        mHandler.removeCallbacks(runnable)
        isFlipping = true
        mHandler.postDelayed(runnable, 10000)
    }

    //关闭信息轮播
    fun stopFlipping() {
        if (mData.size <= 1) return
        isFlipping = false
        mHandler.removeCallbacks(runnable)
    }

}