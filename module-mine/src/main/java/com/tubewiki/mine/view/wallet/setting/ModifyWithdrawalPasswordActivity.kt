package com.tubewiki.mine.view.wallet.setting

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.PhoneUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.utils.PermissionUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.limitSoftInputMethod
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityModifyWithdrawalPasswordBinding
import com.tubewiki.mine.view.model.WalletViewModel
import com.jmbon.widget.CustomNumKeyView
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.jmbon.widget.textview.AsteriskPasswordTransformationMethod
import com.lxj.xpopup.XPopup

/**
 * 修改提现密码
 */
@Route(path = "/mine/wallet/modify/withdrawal/pwd")
class ModifyWithdrawalPasswordActivity :
    ViewModelActivity<WalletViewModel, ActivityModifyWithdrawalPasswordBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.modify_withdrawal_password))

        titleBarView.setRightText(
            getString(R.string.connect_kefu),
            resources.getColor(R.color.color_262626)
        )

        titleBarView.rightTextView.setOnClickListener {
            showCallPhoneDialog()


        }

        binding.apply {

            window.limitSoftInputMethod(firstPwd)
            lifecycleScope.launchWhenResumed {
                firstPwd.postDelayed({
                    customKeyView.visible()
                    customKeyView.showAnimal()
                    firstPwd.isFocusable = true
                    firstPwd.requestFocus()
                    firstPwd.isCursorVisible = true
                }, 300)
            }
            firstPwd.isFocusable = true
            firstPwd.isFocusableInTouchMode = true

            firstPwd.transformationMethod = AsteriskPasswordTransformationMethod()

            firstPwd.addTextChangedListener(afterTextChanged = {
                if (it.isNotNullEmpty() && it?.length!! >= 6) {
                    binding.sbConfirmation.isEnabled = true
                    binding.sbConfirmation.setUseShape()
                } else {
                    binding.sbConfirmation.isEnabled = false
                    binding.sbConfirmation.setUseShape()
                }
                tips.text = getString(R.string.enter_original_password)
                tips.setTextColor(resources.getColor(R.color.color_262626))

            })

            ClickUtils.applySingleDebouncing(
                sbConfirmation
            ) {
                //验证原密码
                viewModel.verifyWithdrawalPassword(firstPwd.text.toString())
            }


            customKeyView.setOnCallBack(object : CustomNumKeyView.CallBack {
                override fun clickNum(num: String?) {
                    firstPwd.append(num)

                }

                override fun deleteNum() {
                    if (!firstPwd.text.isNullOrEmpty()) {
                        firstPwd.text =
                            firstPwd.text.delete(firstPwd.text.length - 1, firstPwd.text.length)
                        firstPwd.setSelection(firstPwd.text.length)
                    }
                }

                override fun clickEmptyRect() {
                }


            })

        }

    }

    override fun initData() {

        viewModel.verifyPasswordResult.observe(this, {
            if (it.isNullOrEmpty()) {
                ARouter.getInstance().build("/mine/wallet/set/withdrawal/pwd")
                    .withString(
                        TagConstant.PARAMS,
                        getString(R.string.modify_withdrawal_password)
                    ).navigation()
                finish()
            } else {
                binding.sbConfirmation.isEnabled = false
                binding.sbConfirmation.setUseShape()
                binding.tips.text = it
                binding.tips.setTextColor(resources.getColor(R.color.colorFF5050))
            }
        })
    }

    override fun getData() {

    }

    private fun showCallPhoneDialog() {
        //同意拨打权限
        viewModel.getOfficialPhone {
            showCallDialog(it)
        }
    }


    private fun showCallDialog(number: String) {

        val listData = arrayListOf(
            CustomDialogTypeBean(
                resources.getString(R.string.call_kefu),
                CustomDialogTypeBean.TYPE_TITLE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                number,
                CustomDialogTypeBean.TYPE_MESSAGE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                resources.getString(R.string.currency_cancle), CustomDialogTypeBean.TYPE_CANCEL
            ) as MultiItemEntity,
        )
        XPopup.Builder(this)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                CustomListBottomDialog(this, listData) { select ->
                    if (select == 1)
                        PhoneUtils.dial(number)
                }
            ).show()
    }


}