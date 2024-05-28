package com.apkdv.mvvmfast.base.dialog

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView

/**
 * time   : 2021/4/29
 * desc   : 改造CenterPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseCenterDialog<T : ViewBinding>(mContext: Context) : CenterPopupView(mContext) {

    protected lateinit var binding: T

    init {
        XPopup.setAnimationDuration(300)
    }

    override fun onCreate() {
        super.onCreate()
        // 解决小米手机弹框显示时navigationBar颜色不一致的问题
        if (context is Activity) {
            ImmersionBar.with(context as Activity, dialog)
                .transparentBar()
                .init()
        }
    }

    override fun addInnerContent() {
        binding = inflateBindingWithGeneric(LayoutInflater.from(context))
        binding.root.layoutParams = LinearLayout.LayoutParams(
            centerPopupContainer.layoutParams.width,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        centerPopupContainer.addView(binding.root)

    }

    fun getMyRootView(): View {
        return binding.root
    }

}