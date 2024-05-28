package com.tubewiki.mine.view.wallet.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.PhoneUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityModifyWithdrawalPasswordBinding
import com.tubewiki.mine.databinding.ActivitySetWithdrawalPasswordBinding
import com.tubewiki.mine.databinding.ActivityUnbindWithdrawalPasswordBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.tubewiki.mine.view.model.WalletViewModel
import com.jmbon.widget.CustomNumKeyView
import com.jmbon.widget.CustomWithDrawalNumKeyView
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 解绑输入提现密码
 */
@Route(path = "/mine/wallet/unbind/withdrawal/pwd")
class UnbindWithdrawalPasswordActivity :
    ViewModelActivity<WalletViewModel, ActivityUnbindWithdrawalPasswordBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var type: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.unbind_account))

        binding.apply {

            window.limitSoftInputMethod(firstPwd)

            firstPwd.isFocusable = true
            firstPwd.isFocusableInTouchMode = true

            firstPwd.addTextChangedListener(afterTextChanged = {
                if (it.isNotNullEmpty() && it?.length!! >= 6) {
                    binding.sbConfirmation.isEnabled = true
                    binding.sbConfirmation.setUseShape()
                } else {
                    binding.sbConfirmation.isEnabled = false
                    binding.sbConfirmation.setUseShape()
                }
                if (it?.length!! > 0) {
                    binding.toastText.visibility = View.GONE
                }
            })

            sbConfirmation.setOnClickListener {
                //验证原密码
                viewModel.verifyWithdrawalPassword(firstPwd.text.toString())

            }

            customKeyView.setOnCallBack(object : CustomNumKeyView.CallBack {
                override fun clickNum(num: String?) {
                    firstPwd.append(num)

                }

                override fun deleteNum() {

                    firstPwd.text?.let {
                        if (it.isNullOrEmpty()) {
                            return@let
                        }

                        firstPwd.text =
                            it.delete(it.length - 1, it.length)
                        firstPwd.setSelection(firstPwd.text.toString().length)
                    }

                }

                override fun clickEmptyRect() {
                }


            })

        }

    }

    override fun initData() {

        viewModel.verifyPasswordResult.observe(this) {
            if (it.isNullOrEmpty()) {

                if (type.isNullOrEmpty()) {
                    "支付类型错误".showToast()
                    return@observe
                }

                //跳转解绑确认页面
                ARouter.getInstance().build("/mine/wallet/unbind/unbind_tips")
                    .withString(TagConstant.PARAMS, type)
                    .withString(TagConstant.PARAMS3, binding.firstPwd.text.toString()).navigation()

                finish()

            } else {
                binding.sbConfirmation.isEnabled = false
                binding.sbConfirmation.setUseShape()
                binding.toastText.text = it
                binding.firstPwd.text?.clear()
                binding.toastText.visibility = View.VISIBLE
            }
        }


    }

    override fun getData() {

    }
}