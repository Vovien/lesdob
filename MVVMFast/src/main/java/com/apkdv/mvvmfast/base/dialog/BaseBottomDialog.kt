package com.apkdv.mvvmfast.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView

/**
 * @author : wangzhen
 * time   : 2021/3/26
 * desc   : 改造BottomPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseBottomDialog<T : ViewBinding>(context: Context) : BottomPopupView(context) {

    protected lateinit var binding: T

    init {
        XPopup.setAnimationDuration(500)
    }

    override fun addInnerContent() {
        binding = inflateBindingWithGeneric(LayoutInflater.from(context))
        binding.root.layoutParams = LinearLayout.LayoutParams(bottomPopupContainer.layoutParams.width,
           LinearLayout.LayoutParams.WRAP_CONTENT)
        bottomPopupContainer.addView(binding.root)

    }

    fun getMyRootView(): View {
        return binding.root
    }

}