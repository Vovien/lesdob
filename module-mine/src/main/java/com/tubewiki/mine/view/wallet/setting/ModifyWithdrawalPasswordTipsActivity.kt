package com.tubewiki.mine.view.wallet.setting

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.ClickUtils
import com.jmbon.middleware.bean.CategoryList
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityModifyPasswordTipsLayoutBinding
import com.tubewiki.mine.databinding.ActivitySetWithdrawalPasswordBinding
import com.tubewiki.mine.databinding.ActivityWalletResultLayoutBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.tubewiki.mine.view.model.WalletViewModel

/**
 * 修改提现密码提示页面
 */
@Route(path = "/mine/wallet/modify/modify_tips")
class ModifyWithdrawalPasswordTipsActivity :
    ViewModelActivity<WalletViewModel, ActivityModifyPasswordTipsLayoutBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.money_password))
        binding.apply {


            ClickUtils.applySingleDebouncing(sbConfirmation) {
                //点击修改密码
                ARouter.getInstance().build("/mine/wallet/modify/withdrawal/pwd").navigation()
                finish()
            }
        }

    }

    override fun initData() {
    }

    override fun getData() {
    }
}