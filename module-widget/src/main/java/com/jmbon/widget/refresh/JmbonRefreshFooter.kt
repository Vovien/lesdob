package com.jmbon.widget.refresh

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.widget.databinding.JmbonRefreshHeaderBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class JmbonRefreshFooter(context: Context) : RefreshFooter {

    private val view by lazy { JmbonRefreshHeaderBinding.inflate(LayoutInflater.from(context)) }

    fun setAnimation(animation:String){
        view.dotProgress.setAnimation(animation)
    }
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        if (newState == RefreshState.PullUpToLoad) {
            if (mNoMoreData)
                noMoreData(mNoMoreData)
        }

        if (!mNoMoreData) {
            when (newState) {
                RefreshState.None -> {
                    view.dotProgress.cancelAnimation()
                    view.dotProgress.visibility = View.VISIBLE
//                    mTitleText.setText(mTextPulling)
//                    arrowView.animate().rotation(180f)
                }
                RefreshState.PullUpToLoad -> {
                    view.dotProgress.visible()
//                    mTitleText.setText(mTextPulling)
//                    arrowView.animate().rotation(180f)
                }
                RefreshState.Loading, RefreshState.LoadReleased -> {
                    //view.dotProgress.visibility = View.GONE
//                    mTitleText.setText(mTextLoading)
                }
                RefreshState.ReleaseToLoad -> {
                    view.dotProgress.cancelAnimation()
                }
                RefreshState.Refreshing -> {
//                    mTitleText.setText(mTextRefreshing)
                    view.dotProgress.cancelAnimation()
                    // view.dotProgress.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun getView(): View {
        return view.root
    }
    fun setNoMoreDataText(text:String){
        view.textNoMore.text = text
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.FixedBehind
    }

    override fun setPrimaryColors(vararg colors: Int) {
        if (colors.isNotEmpty()) {
            view.root.setBackgroundColor(colors[0])
        }
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        val params = view.root.layoutParams as SmartRefreshLayout.LayoutParams
        params.height = SizeUtils.dp2px(56f)
        view.dotProgress.cancelAnimation()
        //view.dotProgress.gone()
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {

    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        view.dotProgress.playAnimation()
    }

    protected var mNoMoreData = false

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 0
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            noMoreData(noMoreData)
        }
        return true
    }

    private fun noMoreData(noMoreData: Boolean) {
        view.dotProgress.visibility = if (noMoreData) View.GONE else View.VISIBLE
        view.textNoMore.visibility = if (!noMoreData) View.GONE else View.VISIBLE
    }


}