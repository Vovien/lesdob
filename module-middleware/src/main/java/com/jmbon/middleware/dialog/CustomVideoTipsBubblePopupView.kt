package com.jmbon.middleware.dialog

import android.content.Context

import com.apkdv.mvvmfast.base.dialog.BaseBubbleDialog
import com.jmbon.middleware.databinding.LayoutCustomTipsBubblePopBinding


/**
 * @author : leimg
 * time   : 2021/9/6
 * desc   :自定义弹窗提示
 * version: 1.0
 */
class CustomVideoTipsBubblePopupView(
    var mContext: Context,
    var title: String,
) :
    BaseBubbleDialog<LayoutCustomTipsBubblePopBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()
        binding.tvTitle.text = title
        binding.tvTitle.postDelayed({
            //1000ms后消失 200动画过程
            dismiss()
        }, 4200)
    }


}