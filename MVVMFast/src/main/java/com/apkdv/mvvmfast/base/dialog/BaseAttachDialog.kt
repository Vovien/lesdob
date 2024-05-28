package com.apkdv.mvvmfast.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.SizeUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.AttachPopupView

/**
 * time   : 2021/4/29
 * desc   : 改造CenterPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseAttachDialog<T : ViewBinding>(context: Context) :
    AttachPopupView(context) {

    protected lateinit var binding: T

    private var titleBar = 0

    init {
        titleBar = SizeUtils.dp2px(44f) + StatusBarCompat.getStatusBarHeight(context)
        XPopup.setAnimationDuration(300)
    }

    override fun addInnerContent() {
        binding = inflateBindingWithGeneric(LayoutInflater.from(context))
        binding.root.layoutParams = LinearLayout.LayoutParams(
            attachPopupContainer.layoutParams.width,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        attachPopupContainer.addView(binding.root)
    }

    fun getMyRootView(): View {
        return binding.root
    }

}