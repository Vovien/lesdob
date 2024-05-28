package com.apkdv.mvvmfast.base.dialog

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.SizeUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BubbleAttachPopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 * time   : 2021/4/29
 * desc   : 改造CenterPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseBubbleDialog<T : ViewBinding>(context: Context) :
    BubbleAttachPopupView(context) {

    protected lateinit var binding: T

    private var titleBar = 0

    init {
        titleBar = SizeUtils.dp2px(44f) + StatusBarCompat.getStatusBarHeight(context)
        XPopup.setAnimationDuration(300)
    }

    override fun addInnerContent() {
        binding = inflateBindingWithGeneric(LayoutInflater.from(context))
        binding.root.layoutParams = LinearLayout.LayoutParams(
            bubbleContainer.layoutParams.width,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        bubbleContainer.addView(binding.root)
    }

    fun getMyRootView(): View {
        return binding.root
    }

    override fun isShowUpToTarget(): Boolean {
        if (isCanMoreThanTitleBar) {
            titleBar = 0
        }
        //1. 获取atView在屏幕上的位置
        // 依附于指定View
        //1. 获取atView在屏幕上的位置
        val locations = IntArray(2)
        popupInfo.atView.getLocationOnScreen(locations)
        val rect = Rect(
            locations[0], locations[1], locations[0] + popupInfo.atView.measuredWidth,
            locations[1] + popupInfo.atView.measuredHeight
        )
        val maxY = XPopupUtils.getAppHeight(context).toFloat()
        // 尽量优先放在上方，当不够的时候在显示在下方
        //假设下方放不下，超出window高度
        val isTallerThanWindowHeight = rect.bottom + popupContentView.measuredHeight > maxY
        var centerY = ((rect.top + rect.bottom) / 2).toFloat()

        val isNotEnoughTopHeight =
            (rect.top - titleBar) < (popupContentView.measuredHeight + SizeUtils.dp2px(16f))

        //下面放不下放上面
        isShowUp = if (isTallerThanWindowHeight) {
            true
        } else !isNotEnoughTopHeight
        return isShowUp
        // return super.isShowUpToTarget()
    }
    var isCanMoreThanTitleBar = false

}