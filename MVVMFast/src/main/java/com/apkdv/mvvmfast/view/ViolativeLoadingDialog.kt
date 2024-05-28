package com.apkdv.mvvmfast.view

import android.content.Context
import com.airbnb.lottie.LottieAnimationView
import com.apkdv.mvvmfast.R
import com.blankj.utilcode.util.SizeUtils
import com.lxj.xpopup.core.CenterPopupView

/**
 * 审核dialog
 */
class ViolativeLoadingDialog(context: Context) : CenterPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.violative_loading_pop_layout
    }

    lateinit var lottie: LottieAnimationView
    override fun onCreate() {
        super.onCreate()
        lottie =
            contentView.findViewById(R.id.lottie)

    }
    override fun getPopupHeight(): Int {
        return SizeUtils.dp2px(84f)
    }

    override fun getPopupWidth(): Int {
        return SizeUtils.dp2px(195f)
    }


    override fun onShow() {
        lottie.playAnimation()
        super.onShow()
    }
    override fun onDismiss() {
        lottie.cancelAnimation()
        super.onDismiss()
    }
}