package com.jmbon.widget.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.jmbon.widget.R

/**
 *问答下拉刷新控件
 */
class JmbonHeaderRefreshView : FrameLayout {
    private var mRefrushSmartHeadView: View? = null
    private var tvTitle: TextView? = null
    private var ivRefersh: ImageView? = null


    /**
     * @param context
     */
    constructor(context: Context) : super(context) {
        initRefreshView(context)
    }

    //  下面的都是RefreshHeader继承的方法  start
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initRefreshView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initRefreshView(context)
    }

    /**
     * 打气一个view,并进行初始化
     *
     * @param context
     */
    private fun initRefreshView(context: Context) {

        mRefrushSmartHeadView =
            LayoutInflater.from(context).inflate(R.layout.jm_header_refresh_layout, null)
        addView(mRefrushSmartHeadView)
        mRefrushSmartHeadView?.let {
            tvTitle = it.findViewById(R.id.tv_title)
            ivRefersh = it.findViewById(R.id.iv_refresh)
        }
    }


    /**
     * 状态重置
     */
    fun resetStatus() {
        tvTitle?.text = StringUtils.getString(R.string.pull_down_switch)
        ivRefersh?.animate()?.rotation(0f)
    }


    fun pullUpCanceled() {
        //上拉取消
        tvTitle?.text = StringUtils.getString(R.string.pull_down_switch)
        ivRefersh?.animate()?.rotation(0f)
    }

    fun releaseToLoad() {
        tvTitle?.let {
            if (it.text != StringUtils.getString(R.string.release_up)) {
                callVibrator(it)
                ivRefersh?.animate()?.rotation(180f)
            }
        }
        //上拉将要释放开始刷新
        tvTitle?.text = StringUtils.getString(R.string.release_up)


    }

    fun pullUpToLoad() {

        tvTitle?.let {
            if (it.text != StringUtils.getString(R.string.pull_down_switch)) {
                ivRefersh?.animate()?.rotation(0f)
            }
        }
        tvTitle?.text = StringUtils.getString(R.string.pull_down_switch)


    }


    private fun callVibrator(view: View) {
        view.performHapticFeedback(
            HapticFeedbackConstants.LONG_PRESS,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }
}