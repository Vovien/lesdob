package com.jmbon.widget.refresh

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.widget.databinding.JmbonRefreshHeaderBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class JmbonRefreshHeader(var context: Context) : RefreshHeader {

    private val view by lazy { JmbonRefreshHeaderBinding.inflate(LayoutInflater.from(context)) }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.ReleaseToRefresh -> callVibrator()
            RefreshState.RefreshFinish, RefreshState.PullDownCanceled -> isHasVibrator = true
            else -> {}
        }
    }

    private fun callVibrator() {
        if (this.isHasVibrator) {
            view.root.performHapticFeedback(
                HapticFeedbackConstants.LONG_PRESS,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            isHasVibrator = false
        }

    }

    private var isHasVibrator = true
    override fun getView(): View {
        return view.root
    }

    fun setAnimation(animation:String){
        view.dotProgress.setAnimation(animation)
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Scale
    }

    override fun setPrimaryColors(vararg colors: Int) {
        if (colors.isNotEmpty()) {
            view.root.setBackgroundColor(colors[0])
        }
    }

    private val dotParams by lazy { view.dotProgress.layoutParams as FrameLayout.LayoutParams }
    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        val params = view.root.layoutParams as SmartRefreshLayout.LayoutParams
        params.height = SizeUtils.dp2px(56f)
        view.dotProgress.cancelAnimation()
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        dotParams.width = SizeUtils.dp2px(45 * percent.coerceAtMost(1f))
        view.dotProgress.alpha = percent.coerceAtMost(1f)
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        view.dotProgress.playAnimation()
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        view.dotProgress.cancelAnimation()
        return 200
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

}