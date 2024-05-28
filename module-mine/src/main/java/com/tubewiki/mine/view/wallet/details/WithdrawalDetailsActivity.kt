package com.tubewiki.mine.view.wallet.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityWithdrawalDetailsBinding
import com.tubewiki.mine.utils.StringFormat
import com.tubewiki.mine.view.model.WalletViewModel

/**
 * 支出详情
 */
@Route(path = "/mine/wallet/withdrawal/details")
class WithdrawalDetailsActivity :
    ViewModelActivity<WalletViewModel, ActivityWithdrawalDetailsBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var id: Int = 0


    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.withdrawal_details))
        setTitleBarColor(ColorUtils.getColor(R.color.ColorFAFA))
        StatusBarCompat.StatusBarLightModeWithColor(this, ColorUtils.getColor(R.color.ColorFAFA))
        initStateLayout(binding.stateLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        lifecycleScope.launchWhenResumed {
            viewModel.withdrawalDetails(id).next {
                LogUtils.e(this)
                val handleMoney = StringFormat.dataFormat("$amount", true)
                binding.apply {
                    if (status == -1) {
                        withdrawalAmount.text = handleMoney
                    } else {
                        withdrawalAmount.text = "-${handleMoney}"
                    }
                    textWithdrawalTime.text = TimeUtils.millis2String(createTime * 1000L)
                    // status 1 成功 0 审核
                    withdrawalStatus.text = when (status) {
                        1 -> "提现成功"
                        -1 -> "提现申请被拒绝"
                        else -> "提现审核中"
                    }
                    textOrderNumber.text = number
                    // type 1 支付宝 2 微信
                    imageType.setImageResource(if (type == 1) R.drawable.icon_alipay else R.drawable.icon_wechat)
                }

                showContentState()
            }
        }
    }

    override fun getData() {
    }
    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        initData()
    }
}