package com.jmbon.widget.refresh

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.widget.R
import com.jmbon.widget.databinding.JmHeaderRefreshLayoutBinding
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

/**
 *问答下拉刷新控件
 */
class JmbonHeaderRefresh(context: Context) : RefreshHeader {

    private val binding by lazy { JmHeaderRefreshLayoutBinding.inflate(LayoutInflater.from(context)) }

    

    /**
     * 状态重置
     */
    fun resetStatus() {
        binding.tvTitle?.text = StringUtils.getString(R.string.pull_down_switch)
        binding.ivRefresh?.animate()?.rotation(0f)
    }


    fun pullUpCanceled() {
        //上拉取消
        binding.tvTitle?.text = StringUtils.getString(R.string.pull_down_switch)
        binding.ivRefresh?.animate()?.rotation(0f)
        isHasVibrator = true
    }

    fun releaseToLoad() {
        binding.tvTitle.let {
            if (it.text != StringUtils.getString(R.string.release_up)) {
                callVibrator()
                binding.ivRefresh.animate()?.rotation(180f)
            }
        }
        //上拉将要释放开始刷新
        binding.tvTitle.text = StringUtils.getString(R.string.release_up)


    }

    fun pullUpToLoad() {

        binding.tvTitle.let {
            if (it.text != StringUtils.getString(R.string.pull_down_switch)) {
                binding.ivRefresh?.animate()?.rotation(0f)
            }
        }
        binding.tvTitle?.text = StringUtils.getString(R.string.pull_down_switch)
    }




    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        LogUtils.e(newState)
        when (newState) {
            RefreshState.ReleaseToRefresh -> releaseToLoad()
            RefreshState.RefreshFinish, RefreshState.PullDownCanceled -> pullUpCanceled()
            else -> {
            }
        }
    }
    private var isHasVibrator = true
    private fun callVibrator() {
        if (this.isHasVibrator) {
            binding.root.performHapticFeedback(
                HapticFeedbackConstants.LONG_PRESS,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            isHasVibrator = false
        }

    }

    override fun getView(): View {
        return binding.root
    }
    
    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Scale
    }

    override fun setPrimaryColors(vararg colors: Int) {
        if (colors.isNotEmpty()) {
            binding.root.setBackgroundColor(colors[0])
        }
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        reSet()
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int,
    ) {
        
    }


    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        binding.ivRefresh?.animate()?.rotation(180f)
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        reSet()
        return 200
    }

    private fun reSet() {
        binding.tvTitle.text = StringUtils.getString(R.string.pull_down_switch)
        binding.ivRefresh.animate()?.rotation(0f)
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

}