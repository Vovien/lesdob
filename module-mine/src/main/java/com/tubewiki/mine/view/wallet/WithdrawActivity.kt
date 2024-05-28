package com.tubewiki.mine.view.wallet

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.network.config.HttpCode
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.event.WithdrawalSuccess
import com.tubewiki.mine.databinding.ActivityWithdrawBinding
import com.tubewiki.mine.utils.StringFormat
import com.tubewiki.mine.view.model.WalletViewModel
import com.tubewiki.mine.view.wallet.dialog.WithdrawalPasswordDialog
import com.jmbon.widget.CustomWithDrawalNumKeyView
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.impl.ConfirmPopupView
import com.lxj.xpopup.impl.LoadingPopupView
import com.lxj.xpopup.interfaces.SimpleCallback


/**
 * 提现
 */
@Route(path = "/mine/withdraw")
class WithdrawActivity : ViewModelActivity<WalletViewModel, ActivityWithdrawBinding>() {

    @Autowired(name = TagConstant.FULL_AMOUNT)
    @JvmField
    var fullAmount: Float = 0f

    // 1 支付宝 2 微信
    var withdrawType: String = ALIPAY

    private val POINTER_LENGTH = 2
    private val POINTER = "."
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    val allMoney = StringBuilder()

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.withdrawal))
        setToolbarBackground(ColorUtils.getColor(R.color.ColorFAFA))

        StatusBarCompat.StatusBarLightModeWithColor(this, ColorUtils.getColor(R.color.ColorFAFA))
        binding.apply {
//            val typeface = Typeface.createFromAsset(assets, "fonts/roboto_black.ttf")
//            editMoney.typeface = typeface
//
//            tvRmb.typeface = typeface

            // 先选择支付宝
            payTypeSwitch(if (Constant.walletSetting.hasAlipay) 1 else 2)
            imageMore.visibility =
                if (Constant.walletSetting.hasAlipay && Constant.walletSetting.hasWechat) View.VISIBLE else View.GONE

            window.limitSoftInputMethod(editMoney)

            lifecycleScope.launchWhenResumed {
                editMoney.postDelayed({
                    customKeyView.visible()
                    customKeyView.showAnimal()
                    editMoney.isFocusable = true
                    editMoney.requestFocus()
                    editMoney.isCursorVisible = true
                    editMoney.performClick()
                }, 300)
            }


            textBalance.text = setBalance()
            /**
             * 使用 flow 防止双击
             */
            textFullAmount.setOnSingleClickListener({
                editMoney.text.clear()
                editMoney.setText(StringFormat.dataFormat(fullAmount.toString(), true))
                customKeyView.setWithDrawalStatus(fullAmount != 0f)
                allMoney.clear()
                allMoney.append(fullAmount.toString())
                editMoney.setSelection(editMoney.text.length)
            })

            /**
             * 使用传统方式防止双击
             */
            if (Constant.walletSetting.hasAlipay && Constant.walletSetting.hasWechat)
                llPayType.setOnSingleClickListener({
                    val listData = arrayListOf(
                        CustomDialogTypeBean(getString(R.string.withdraw_to), 1),
                        CustomDialogTypeBean(
                            resources.getString(R.string.ali_pay),
                            2
                        ) as MultiItemEntity,
                        CustomDialogTypeBean(
                            resources.getString(R.string.wechat),
                            2
                        ) as MultiItemEntity,
                        CustomDialogTypeBean(
                            resources.getString(R.string.currency_cancle),
                            3
                        ) as MultiItemEntity,
                    )
                    XPopup.Builder(this@WithdrawActivity)
                        .asCustom(
                            CustomListBottomDialog(this@WithdrawActivity, listData) {
                                payTypeSwitch(it)
                            }
                        )
                        .show()

                })
            customKeyView.setOnCallBack(object : CustomWithDrawalNumKeyView.CallBack {
                override fun clickNum(num: String?) {
                    //已经输入小数点的情况下，只能输入数字
                    if (allMoney.contains(POINTER)) {
                        if (POINTER != num) { //只能输入一个小数点
                            //验证小数点精度，保证小数点后只能输入两位
                            val index: Int = allMoney.indexOf(POINTER)
                            val length: Int = allMoney.length - index
                            if (length <= POINTER_LENGTH && allMoney.length < 8) {
                                allMoney.append(num)
                            }
                        }
                    } else {
                        //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点
                        if (allMoney.isEmpty()) {
                            if (num != ".") {
                                allMoney.append(num)
                            } else {
                                allMoney.append("0")
                                allMoney.append(num)
                            }
                        } else if (allMoney.length == 1 && allMoney.toString() == "0") { //如果第一个数字为0，第二个不为点，就不允许输入
                            if (num.equals(".")) { //第二个不为点时候返回空
                                allMoney.append(num)
                            }
                        } else if (allMoney.length < 8)
                            allMoney.append(num)
                    }

                    setMoney()
                }

                override fun deleteNum() {
                    if (allMoney.isNotEmpty()) {
                        allMoney.delete(allMoney.length - 1, allMoney.length)
                        setMoney()
                    }

                }

                override fun withDrawal() {
                    if (allMoney.endsWith(".", true)) {
                        "提现金额不规范".showToast()
                        return
                    }

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastTime > FAST_CLICK_THRSHOLD) {
                        lastTime = currentTime
                        showPasswdDialog()
                    }
                }
            }
            )
        }
    }

    val FAST_CLICK_THRSHOLD = 300L
    var lastTime = 0L


    private fun showPasswdDialog() {
        passwdDialog.money = allMoney.toString()
        withDrawalPopup.show()
    }

    private val passwdDialog by lazy {
        WithdrawalPasswordDialog(this@WithdrawActivity) {
            // 输入了密码
            lifecycleScope.launchWhenStarted {
                viewModel.withdrawal(allMoney.toString(), it, withdrawType)
                    .netCatch {
                        LogUtils.e("测试Activity ViewModel错误回调并存的情况，$this")
                        when {
                            this.code.toInt() == HttpCode.FRAME_WORK.NETWORK_ERROR -> {
                                "网络错误，请检查网络后\n重新申请提现".showToast()
                            }
                            "提现密码错误" == message -> {
                                reSetDialog.show()
                            }
                            else -> {
                                message.showToast()
                            }
                        }
                    }.next {
                        // 提现成功后修改界面为提现明细界面
                        KotlinBus.post(WithdrawalSuccess())
                        // 提现成功
                        ARouter.getInstance().build("/mine/wallet/result")
                            .withString(TagConstant.PARAMS, getString(R.string.withdraw_result))
                            .withString(
                                TagConstant.PARAMS2,
                                getString(R.string.withdrawal_application_is_successful)
                            )
                            .withString(
                                TagConstant.PARAMS3,
                                getString(R.string.instant_to_account_after_approval)
                            )
                            .withString(TagConstant.PARAMS4, getString(R.string.back_to_wallet))
                            .navigation()
                        finish()
                    }
            }
        }
    }


    /**
     * 输入密码
     */
    private val withDrawalPopup: BasePopupView by lazy {
        XPopup.Builder(this@WithdrawActivity)
            .dismissOnTouchOutside(true)
            .isDestroyOnDismiss(false)
            .asCustom(passwdDialog)
    }

    /**
     * 错误重试
     */
    private val reSetDialog: ConfirmPopupView by lazy {
        XPopup.Builder(this)
            .dismissOnTouchOutside(true)
            .isDestroyOnDismiss(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onCreated(popupView: BasePopupView?) {
                    super.onCreated(popupView)
                    popupView?.popupImplView?.apply {
                        findViewById<TextView>(R.id.tv_content).gone()
                        findViewById<TextView>(R.id.view_line2).gone()
//                        findViewById<TextView>(R.id.tv_content).gone()
                    }
                }
            })
            .asConfirm(
                "提现密码错误，请重试",
                "",
                "",
                "重试",
                {
                    showPasswdDialog()
                },
                null,
                true,
                R.layout.dialog_general_layout
            )
    }

    private fun payTypeSwitch(it: Int) {
        when (it) {
            1 -> {
                withdrawType = ALIPAY
                setPayType(resources.getString(R.string.ali_pay), R.drawable.icon_alipay)
            }
            2 -> {
                withdrawType = WECHAT
                setPayType(resources.getString(R.string.wechat), R.drawable.icon_wechat)
            }
        }
    }


    private fun ActivityWithdrawBinding.setMoney() {
        editMoney.setText(if (allMoney.isNotEmpty()) StringFormat.dataFormat(allMoney.toString()) else "")
        editMoney.setSelection(editMoney.text.length)
        binding.apply {
            if (allMoney.isNotNullEmpty()) {
                val realInt = if (allMoney.indexOf(".") > 0) { // 有小数
                    allMoney.toString().toFloat()
                } else allMoney.toString().toInt()

                if (realInt.toFloat() < 0.01f) {
                    textFullAmount.visible()
                    textBalance.setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
                    textBalance.text = setBalance()
                    customKeyView.setWithDrawalStatus(false)
                    return@apply
                }

                // 提现按钮是否可用
                try {
                    if (realInt.toFloat() in 0.01f..fullAmount) {
                        customKeyView.setWithDrawalStatus(true)
                        textFullAmount.visible()
                        textBalance.setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
                        textBalance.text = setBalance()
                    } else {
                        customKeyView.setWithDrawalStatus(false)
                        textFullAmount.gone()
                        textBalance.setTextColor(ColorUtils.getColor(R.color.colorFF5050))
                        textBalance.text = getString(R.string.withdrawal_amount_is_beyond_balance)
                    }
                } catch (e: Exception) {
                    customKeyView.setWithDrawalStatus(false)
                }
            } else {
                textFullAmount.visible()
                textBalance.setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
                textBalance.text = setBalance()
                customKeyView.setWithDrawalStatus(false)
            }
        }
    }

    private fun setBalance() = "当前余额${StringFormat.dataFormat(fullAmount.toString(), true)}元，"

    override fun initData() {
    }

    override fun getData() {
    }

    private fun setPayType(name: String, @DrawableRes image: Int) {
        binding.apply {
            textPayName.text = name
            imagePayType.setImageResource(image)
        }
    }


    companion object {
        private const val ALIPAY = "alipay"
        private const val WECHAT = "wechat"
    }

    private var loadingPopup: LoadingPopupView? = null
    override fun showLoading() {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(this)
                .dismissOnBackPressed(false)
                .isDestroyOnDismiss(false)
                .animationDuration(0)
                .asLoading(getString(R.string.withdrawaling), R.layout.dialog_withdrawal_loading)
        }
        loadingPopup?.apply {
            loadingPopup?.setTitle(getString(R.string.withdrawaling))
            if (!isShow)
                show()
        }
    }

    override fun dismissLoading() {
        loadingPopup?.dismiss()
    }

}