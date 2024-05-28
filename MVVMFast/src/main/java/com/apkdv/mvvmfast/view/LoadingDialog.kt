package com.apkdv.mvvmfast.view

import android.content.Context
import android.graphics.Color
import com.airbnb.lottie.LottieAnimationView
import com.apkdv.mvvmfast.R
import com.blankj.utilcode.util.SizeUtils
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.util.XPopupUtils

class LoadingDialog(context: Context, val whiteBackground: Boolean = true) : CenterPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.loading_view_pop
    }

    lateinit var lottie: LottieAnimationView
    override fun onCreate() {
        super.onCreate()
        popupImplView.background =
            XPopupUtils.createDrawable(if (whiteBackground) Color.parseColor("#E6ffffff") else Color.TRANSPARENT,
                SizeUtils.dp2px(8f).toFloat())
        lottie =
            contentView.findViewById(R.id.lottie)
        if (whiteBackground) {
            lottie.setAnimation("lottie/green_loading.json")
        } else {
            lottie.setAnimation("lottie/white_loading.json")
        }
    }
    override fun getPopupHeight(): Int {
        return SizeUtils.dp2px(80f)
    }

    override fun getPopupWidth(): Int {
        return SizeUtils.dp2px(80f)
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