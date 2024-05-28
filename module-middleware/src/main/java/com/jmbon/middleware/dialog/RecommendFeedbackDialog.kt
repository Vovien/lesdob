package com.jmbon.middleware.dialog

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.jmbon.middleware.R
import com.jmbon.middleware.arouter.RouterHub
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.DialogRecommendFeedbackLayoutBinding
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action


/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   :推荐反馈提示dialog
 * version: 1.0
 */
class RecommendFeedbackDialog(
    var mContext: Context
) :
    BaseCenterDialog<DialogRecommendFeedbackLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()

        binding.tvOk.setOnClickListener {
            dismiss()
        }

        binding.jbFeedback.setOnClickListener {
            Action {
                ARouter.getInstance().build("/mine/setting/feedback").withInt(TagConstant.TYPE, 1)
                    .navigation()
            }.logInToIntercept()
            dismiss()
        }
    }

}