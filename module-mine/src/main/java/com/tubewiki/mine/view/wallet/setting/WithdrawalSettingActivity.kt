package com.tubewiki.mine.view.wallet.setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityWithdrawalSettingBinding
import com.tubewiki.mine.view.model.WalletViewModel
import com.jmbon.pay.bean.WxLoginEvent
import com.jmbon.pay.ext.getUserInfo2
import com.jmbon.pay.ext.loginWx
import com.jmbon.pay.utils.AuthUtils
import com.jmbon.widget.progress_button.JmbonButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 提现设置
 */
@Route(path = "/mine/wallet/withdrawal/setting")
class WithdrawalSettingActivity :
    ViewModelActivity<WalletViewModel, ActivityWithdrawalSettingBinding>() {

    private val WECHAT = "wechat"
    private val ALIPAY = "alipay"

    var type: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.withdrawal_setting))
        setToolbarBackground(ColorUtils.getColor(R.color.ColorFAFA))

        StatusBarCompat.StatusBarLightModeWithColor(this, ColorUtils.getColor(R.color.ColorFAFA))
        binding.apply {
            sbAlipayBind.setOnSingleClickListener({
                type = ALIPAY
                //判断是否设置密码
                bindOrUnbindWxAlipay()
            })
            sbWechatBind.setOnSingleClickListener({
                type = WECHAT
                bindOrUnbindWxAlipay()
            })
            stRebate.setOnSingleClickListener({
                //设置提现密码
                if (!Constant.walletSetting.hasPassword) {
                    //未设置提现密码
                    ARouter.getInstance().build("/mine/wallet/set/withdrawal/pwd").navigation()
                } else {
                    //已设置提现密码
                    ARouter.getInstance().build("/mine/wallet/modify/modify_tips").navigation()

                }
            })

        }

    }

    /**
     * type : alipay或wechat两种
     */
    private fun bindOrUnbindWxAlipay() {

        var isBind = false //是否绑定
        if (type == WECHAT) {
            isBind = Constant.walletSetting.hasWechat
        } else {
            isBind = Constant.walletSetting.hasAlipay
        }
        if (!Constant.walletSetting.hasPassword) {
            R.string.please_setting_money_password.showToast()
            return
        }


        if (!isBind) {
            //先登录获取openid
            if (type == WECHAT) {
                loginWx(this)
            } else {
                AuthUtils.setAliAuth("", this)
                AuthUtils().setAliAuthBack { authResult ->
                    getUserInfo2(authResult) {
                        if (it.data1.data.code != "10000") {
                            "获取openId失败".showToast()
                            return@getUserInfo2
                        }
                        val name = it.data1.data.nickName
                        viewModel.payBinding(
                            if (name.isEmpty()) "未知昵称" else name,
                            it.data1.data.userId, type
                        )
                    }
                }
            }

        } else {
            //解绑
            //解绑前判断是否有提现
            viewModel.hasWithdrawal(type)
        }
    }

    private fun buttonUIStatus() {
        binding.apply {
            if (Constant.walletSetting.hasAlipay) {
                sbAlipayBind.isSelected = true
                sbAlipayBind.setBtnStyle(JmbonButton.BLACK_BORDER)
                sbAlipayBind.setIsShowLoadingWhenClick(false)
            } else {
                sbAlipayBind.isSelected = false
                sbAlipayBind.setBtnStyle(JmbonButton.GREEN_FULL)
                sbAlipayBind.setIsShowLoadingWhenClick(true)
            }
            if (Constant.walletSetting.hasWechat) {
                sbWechatBind.isSelected = true
                sbWechatBind.setBtnStyle(JmbonButton.BLACK_BORDER)
                sbWechatBind.setIsShowLoadingWhenClick(false)
            } else {
                sbWechatBind.isSelected = false
                sbWechatBind.setBtnStyle(JmbonButton.GREEN_FULL)
                sbWechatBind.setIsShowLoadingWhenClick(true)
            }

            stRebate.setRightString(if (Constant.walletSetting.hasPassword) "已设置" else "未设置")
            stRebate.setRightTextColor(
                if (!Constant.walletSetting.hasPassword) resources.getColor(R.color.colorFF5050) else resources.getColor(
                    R.color.color_BFBFBF
                )
            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: WxLoginEvent) {
        event.either.data1?.openid?.let {
            val name = event.either.data1?.nickname!!
            viewModel.payBinding(
                if (name.isEmpty()) "未知昵称" else name,
                it,
                type
            )
        }
    }

    override fun onResume() {
        super.onResume()

        buttonUIStatus()

    }

    override fun initData() {

        viewModel.hasWithdrawalResult.observe(this) {
            Constant.walletSetting.hasMission = it
            //判断是否有提现在审核中是否可以解绑
            if (it) {
                R.string.cancel_binding_failed.showToast()
            } else {
                //解绑先跳转输入密码页面
                ARouter.getInstance().build("/mine/wallet/unbind/withdrawal/pwd")
                    .withString(TagConstant.PARAMS, type).navigation()
            }
        }

        viewModel.payBindingResult.observe(this) {
            if (it.data1) {
                if (type == WECHAT) {
                    Constant.walletSetting.hasWechat = true
                    Constant.walletSetting.user.wechatName = it.data2
                } else {
                    Constant.walletSetting.hasAlipay = true
                    Constant.walletSetting.user.alipayName = it.data2
                }
                buttonUIStatus()
            }
        }
    }

    override fun getData() {

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

    }

}