package com.apkdv.mvvmfast.view.state.state

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.utils.SizeUtil
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.view.state.MultiStatePage
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.Utils


class EmptyState2 : BaseState() {
    override fun getLayoutId(): Int = R.layout.default_viewstatus_no_box_empty2

    private lateinit var tvEmptyMsg: TextView


    override fun onMultiStateViewCreate(
        view: View,
        retry: () -> Unit,
        offset: Int,
        orientation: Int
    ) {
        super.onMultiStateViewCreate(view, retry, offset, orientation)
        tvEmptyMsg = view.findViewById(R.id.state_layout_empty_hint)
        setEmptyMsg(MultiStatePage.config.emptyMsg2)
        //提示语屏幕居中对齐居中
        var layoutParams = tvEmptyMsg.layoutParams as LinearLayout.LayoutParams
        val offset =
            StatusBarCompat.getStatusBarHeight(Utils.getApp()) + SizeUtil.getActionBarHeight(Utils.getApp())
        layoutParams.bottomMargin = offset
        tvEmptyMsg.layoutParams = layoutParams
    }

    fun setEmptyMsg(emptyMsg: String) {
        tvEmptyMsg.text = emptyMsg
    }
}