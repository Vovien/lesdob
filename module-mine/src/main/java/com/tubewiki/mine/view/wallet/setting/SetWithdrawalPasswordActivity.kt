package com.tubewiki.mine.view.wallet.setting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.limitSoftInputMethod
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivitySetWithdrawalPasswordBinding
import com.tubewiki.mine.view.model.WalletViewModel
import com.jmbon.widget.CustomNumKeyView
import com.jmbon.widget.textview.AsteriskPasswordTransformationMethod

/**
 * 设置提现密码
 */
@Route(path = "/mine/wallet/set/withdrawal/pwd")
class SetWithdrawalPasswordActivity :
    ViewModelActivity<WalletViewModel, ActivitySetWithdrawalPasswordBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var title: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (title.isNotNullEmpty()) {
            setTitleName(title)
        } else {
            setTitleName(getString(R.string.set_withdrawal_password))
        }
        binding.apply {
            if (title.isNotNullEmpty()){
                textPassTips.text = "设置新密码（6位纯数字）"
            }
            customKeyView.tag = firstPwd
            firstPwd.tag = ""
            confirmPwd.tag = ""
            firstPwd.isCursorVisible = true
            firstPwd.transformationMethod = AsteriskPasswordTransformationMethod()
            confirmPwd.transformationMethod = AsteriskPasswordTransformationMethod()
            lifecycleScope.launchWhenResumed {
                firstPwd.postDelayed({
                    customKeyView.visible()
                    customKeyView.showAnimal()
                    firstPwd.isFocusable = true
                    firstPwd.requestFocus()
                    firstPwd.isCursorVisible = true
                }, 300)
            }
            firstPwd.addTextChangedListener(textWatcher)

            confirmPwd.addTextChangedListener(textWatcher)

            sbConfirmation.setOnClickListener {
                if (!binding.firstPwd.text.toString().equals(binding.confirmPwd.text.toString())) {
                    binding.toastText.visibility = View.VISIBLE
                } else {
                    //设置提现密码
                    viewModel.setWithdrawalPassword(binding.firstPwd.text.toString())

                }
            }


            customKeyView.setOnCallBack(object : CustomNumKeyView.CallBack {
                override fun clickNum(num: String?) {
                    val editView = customKeyView.tag as EditText
                    //editView.setTag(editView.getTag() as String + num)
                    editView.append(num)
                }

                override fun deleteNum() {
                    val editView = customKeyView.tag as EditText
                    if (!editView.text.isNullOrEmpty()) {

                        editView.text =
                            editView.text.delete(editView.text.length - 1, editView.text.length)
                        editView.setSelection(editView.text.length)

                    }

                }

                override fun clickEmptyRect() {
                }

            })

            firstPwd.setOnFocusChangeListener { v, hasFocus ->
                firstPwd.isCursorVisible = true
                confirmPwd.isCursorVisible = false
                customKeyView.tag = firstPwd
            }

            confirmPwd.setOnFocusChangeListener { v, hasFocus ->
                firstPwd.isCursorVisible = false
                confirmPwd.isCursorVisible = true
                customKeyView.tag = confirmPwd
            }


            window.limitSoftInputMethod(firstPwd)
            window.limitSoftInputMethod(confirmPwd)
        }
    }

    override fun initData() {
        viewModel.setPasswordResult.observe(this, {
            if (it) {
                Constant.walletSetting.hasPassword = true
                ARouter.getInstance().build("/mine/wallet/result")
                    .withString(
                        TagConstant.PARAMS,
                        if (title.isNotNullEmpty()) title else getString(R.string.set_withdrawal_password)
                    )
                    .withString(
                        TagConstant.PARAMS2,
                        if (title.isNotNullEmpty()) getString(R.string.modify_success) else getString(
                            R.string.setting_success
                        )
                    ).withString(TagConstant.PARAMS4, "完成")
                    .navigation()
                finish()
            } else {

            }
        })
    }

    override fun getData() {

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.toastText.visibility = View.GONE
            if (binding.firstPwd.text.isNotEmpty() && binding.firstPwd.text.length == 6){
                binding.confirmPwd.isFocusable = true
                binding.confirmPwd.requestFocus()
                binding.confirmPwd.isCursorVisible = true
            }
            if (binding.firstPwd.text.isNotEmpty() && binding.firstPwd.text.length == 6 &&
                binding.confirmPwd.text.isNotEmpty() && binding.confirmPwd.text.length == 6
            ) {

                binding.sbConfirmation.isEnabled = true
                binding.sbConfirmation.setUseShape()
            } else {
                binding.sbConfirmation.isEnabled = false
                binding.sbConfirmation.setUseShape()
            }
        }
    }
}