package com.tubewiki.mine.view.wallet.setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.ClickUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityUnbindAccountTipsLayoutBinding
import com.tubewiki.mine.view.model.WalletViewModel

/**
 * 解绑提现密码提示页面
 */
@Route(path = "/mine/wallet/unbind/unbind_tips")
class  UnbindWithdrawalAccountTipsActivity :
    ViewModelActivity<WalletViewModel, ActivityUnbindAccountTipsLayoutBinding>() {
    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var type: String = ""

    @Autowired(name = TagConstant.PARAMS3)
    @JvmField
    var password: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.unbind_account))
        binding.apply {
            if (type == "alipay") {
                ivType.setImageResource(R.drawable.icon_alipay)
            } else {
                ivType.setImageResource(R.drawable.icon_wechat)
            }

            tvAccount.text = if (type == "wechat")  Constant.walletSetting.user.wechatName else Constant.walletSetting.user.alipayName

            ClickUtils.applyGlobalDebouncing(sbConfirmation) {
                //执行解绑操作
                viewModel.untieBinding(password, type)
            }

        }

    }

    override fun initData() {
        viewModel.unBindingResult.observe(this) {
            if (it) {
                //解绑成功
                if (type == "wechat") {
                    Constant.walletSetting.hasWechat = false
                } else {
                    Constant.walletSetting.hasAlipay = false
                }
                ARouter.getInstance().build("/mine/wallet/result")
                    .withString(TagConstant.PARAMS, getString(R.string.cancel_binding))
                    .withString(TagConstant.PARAMS2, getString(R.string.unbind_success))
                    .withInt(TagConstant.BTN_PADDING, 16f.dp())
                    .navigation()
                finish()

            } else {

            }
        }
    }

    override fun getData() {
    }
}