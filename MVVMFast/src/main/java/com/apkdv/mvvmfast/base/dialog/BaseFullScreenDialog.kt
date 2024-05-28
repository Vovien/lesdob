package com.apkdv.mvvmfast.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.FullScreenPopupView

/**
 * time   : 2021/4/29
 * desc   : 改造FullScreenPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseFullScreenDialog<T : ViewBinding>(context: Context) :
    FullScreenPopupView(context) {

    protected lateinit var binding: T

    init {
        XPopup.setAnimationDuration(500)
    }

    override fun initPopupContent() {
        if (fullPopupContainer.childCount == 0) addViewBindingInnerContent()
        popupContentView.translationX = popupInfo.offsetX.toFloat()
        popupContentView.translationY = popupInfo.offsetY.toFloat()
    }

    private fun addViewBindingInnerContent() {
        binding = inflateBindingWithGeneric(LayoutInflater.from(context))
        binding.root.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        fullPopupContainer.addView(binding.root)
    }

    fun getMyRootView(): View {
        return binding.root
    }

}