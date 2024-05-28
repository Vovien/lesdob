package com.tubewiki.mine.view.setting.password

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityPasswordSetSuccessBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel

/**
 * 设置密码成功
 * 提现成功
 * 设置邮箱成功
 */
@Route(path = "/mine/setting/password/success")
class PasswordSetSuccessActivity : AppBaseActivity<ActivityPasswordSetSuccessBinding>() {


    @Autowired
    @JvmField
    var type: Int = 0

    private val WITHDRAWAL_REQUEST = 0x00123

    override fun beforeViewInit() {
        ARouter.getInstance().inject(this)
    }


    override fun initView(savedInstanceState: Bundle?) {
        when (type) {
            CaptchaViewModel.UPDATE_PHONE -> setNewPhone()
            CaptchaViewModel.RETRIEVE_PASSWORD, CaptchaViewModel.UPDATE_PASSWORD -> binding.textTitle.text =
                "密码更新成功"
            CaptchaViewModel.SET_NEW_PASSWORD -> binding.textTitle.text = "密码设置成功"
            CaptchaViewModel.SET_NEW_EMAIL -> binding.textTitle.text = "邮箱绑定成功"
            CaptchaViewModel.UPDATE_EMAIL -> binding.textTitle.text = "邮箱修改成功"
            WITHDRAWAL_REQUEST -> {
                setTitleName("提现结果")
                setWithdrawal()
            }
        }

        binding.btnCommit.setOnClickListener {
            if (type == CaptchaViewModel.RETRIEVE_PASSWORD) {
                ARouter.getInstance().build("/mine/login/mobile")
                    .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .navigation(this)
            } else {
                //其他操作返回安全登录界面
                ARouter.getInstance().build("/mine/setting/login_safe")
                    .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .navigation()
            }
            this.finish()
        }
    }

    override fun enableBack(): Boolean {
        return false
    }

    private fun setNewPhone() {
        binding.apply {
            binding.textInfo.visibility = View.VISIBLE
            binding.textTitle.text = getString(R.string.mobile_phone_is_changed)
            binding.textInfo.text =
                getString(R.string.please_log_in_using_new_phone_number_sisters_nations_next_time)
        }
    }

    private fun setWithdrawal() {
        binding.apply {
            binding.textInfo.visibility = View.VISIBLE
            binding.textTitle.text = getString(R.string.withdrawal_application_is_successful)
            binding.textInfo.text = getString(R.string.instant_to_account_after_approval)
            binding.btnCommit.text = getString(R.string.back_to_purse)
        }
    }

    override fun initData() {
    }

    override fun getData() {
    }
}