package com.apkdv.mvvmfast.view.state.state

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.view.state.MultiStatePage
import com.blankj.utilcode.util.ColorUtils


class EmptyState : BaseState() {

    private lateinit var tvEmptyMsg: TextView
    private lateinit var imgEmpty: ImageView


    override fun getLayoutId(): Int = R.layout.default_viewstatus_box_empty

    override fun onMultiStateViewCreate(view: View, retry: () -> Unit,offset:Int,orientation:Int) {
        super.onMultiStateViewCreate(view, retry,offset, orientation)
        tvEmptyMsg = view.findViewById(R.id.state_layout_empty_hint)
        imgEmpty = view.findViewById(R.id.state_layout_empty_img)
        setEmptyMsg(MultiStatePage.config.emptyMsg)
        setEmptyIcon(MultiStatePage.config.emptyIcon)
    }

    fun setEmptyMsg(emptyMsg: String) {
        tvEmptyMsg.text = emptyMsg
    }

    fun setEmptyIcon(@DrawableRes emptyIcon: Int) {
        imgEmpty.setImageResource(emptyIcon)
    }
}