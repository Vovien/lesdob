package com.jmbon.widget.dialog

import android.app.Activity
import com.apkdv.mvvmfast.base.dialog.BaseCenterDialog
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.jmbon.widget.databinding.DialogAdoptAnswerSuccessLayoutBinding


/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   :退出提示dialog
 * version: 1.0
 */
class AdoptAnswerSuccessDialog(
    var mContext: Activity,
    var result: () -> Unit,
) :
    BaseCenterDialog<DialogAdoptAnswerSuccessLayoutBinding>(mContext) {

    override fun onCreate() {
        super.onCreate()
        binding.tvOk.setOnClickListener {
            dismiss()
            result()
        }
        binding.tvCancel.setOnClickListener {
            //保存用户是否点击过不再提示
            true.saveToMMKV("USER_ACCEPT_RESULT_STATUS")
            dismiss()
            result()
        }

    }

}