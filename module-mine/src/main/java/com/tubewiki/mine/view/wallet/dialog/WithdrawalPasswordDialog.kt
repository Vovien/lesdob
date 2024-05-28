package com.tubewiki.mine.view.wallet.dialog

import android.annotation.SuppressLint
import android.app.Activity
import androidx.core.widget.addTextChangedListener
import com.apkdv.mvvmfast.base.dialog.BaseFullScreenDialog
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.utils.limitSoftInputMethod
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.databinding.DialogWithdrawalPasswordBinding
import com.jmbon.widget.CustomNumKeyView
import com.lxj.xpopup.XPopup

@SuppressLint("ViewConstructor")
class WithdrawalPasswordDialog(val context: Activity, val result: (String) -> Unit) :
    BaseFullScreenDialog<DialogWithdrawalPasswordBinding>(context) {

    init {
        XPopup.setAnimationDuration(300)
    }

    //    val pwd = StringBuilder()
    var money: String = ""


    override fun onCreate() {
        binding.apply {
            imageBack.setOnSingleClickListener({
                dismiss()
            })
            textMoney.text = "￥$money"
            context.window.limitSoftInputMethod(splitEditText)
            splitEditText.text?.let { text ->
                customKeyView.setOnCallBack(object : CustomNumKeyView.CallBack {
                    override fun clickNum(num: String?) {
                        if (text.length < 6) {
                            text.append(num)
                        }

                    }

                    override fun deleteNum() {
                        if (text.isNotEmpty()) {
                            text.delete(text.length - 1, text.length)
                        }
                    }

                    override fun clickEmptyRect() {

                    }

                })
            }

            splitEditText.addTextChangedListener(afterTextChanged = {
                it?.apply {
                    if (this.length == 6) {
                        dismiss()
                        result.invoke(this.toString())
                    }
                }
            })
        }
    }

    override fun onDismiss() {
        binding.splitEditText.text?.clear()
//        splitEditText
//        pwd.clear()
        super.onDismiss()
    }

    override fun doShowAnimation() {
        binding.textMoney.text = "￥$money"
        super.doShowAnimation()
    }

    override fun doAfterShow() {
        super.doAfterShow()
        binding.shadowLinearLayout.post {
            LogUtils.e(binding.shadowLinearLayout.visibility == android.view.View.VISIBLE)
        }
    }

}