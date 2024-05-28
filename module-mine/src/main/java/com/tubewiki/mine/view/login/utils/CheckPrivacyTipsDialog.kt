package com.tubewiki.mine.view.login.utils

import android.content.Context
import com.apkdv.mvvmfast.base.dialog.BaseBubbleDialog
import com.jmbon.middleware.utils.dp
import com.tubewiki.mine.databinding.DialogCheckPrivacyTipsBinding

class CheckPrivacyTipsDialog(context: Context) :
    BaseBubbleDialog<DialogCheckPrivacyTipsBinding>(context) {
    init {
        isCanMoreThanTitleBar = true

    }

    override fun onCreate() {
        super.onCreate()
            binding.textTips.postDelayed({
                //1000ms后消失 200动画过程
                dismiss()
            }, 2200)
    }

    override fun getMaxWidth(): Int {
        return 175f.dp()
    }
}