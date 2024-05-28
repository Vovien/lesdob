package com.tubewiki.mine.view.setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityValidationEmailBinding
import com.tubewiki.mine.view.model.SettingViewModel

/**
 * 绑定邮箱验证
 */
@Route(path = "/mine/setting/validation/email")
class ValidationEmailActivity :
    ViewModelActivity<SettingViewModel, ActivityValidationEmailBinding>() {

    @Autowired
    @JvmField
    var msnToken: String = ""

    @Autowired
    @JvmField
    var email: String = ""

    @Autowired
    @JvmField
    var emailToken: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.binding_email))
        binding.textMailNumber.text =
            StringUtils.getString(R.string.already_email_to_mailbox, email)
        binding.textCountDown
            .setCloseKeepCountDown(false) //关闭页面保持倒计时开关
            .setCountDownClickable(false) //倒计时期间点击事件是否生效开关
            .setShowFormatTime(false) //是否格式化时间
            .setOnCountDownFinishListener {
                binding.notReceivedMail.text = getString(R.string.not_yet_received_mail)
                binding.notReceivedMail.setTextColor(ColorUtils.getColor(R.color.color_262626))
            }.setOnCountDownStartListener {
                binding.notReceivedMail.text = StringUtils.getString(R.string.email_to_send, 60)
                binding.notReceivedMail.setTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
            }.setOnCountDownTickListener {
                binding.notReceivedMail.isEnabled = it.toInt() == 0
                binding.notReceivedMail.text =
                    StringUtils.getString(R.string.email_to_send, it.toInt())
                binding.notReceivedMail.setTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
            }
        binding.notReceivedMail.setOnClickListener {
            binding.textCountDown.startCountDown(60)
            binding.notReceivedMail.isEnabled = false
            binding.textMailNumber.text =
                StringUtils.getString(R.string.already_re_email_to_mailbox, email)
            started {
                viewModel.sendVerifyEmail(email, msnToken).next {
                    emailToken = data1
                    email = data2
                }
            }
        }
        binding.clickVerificationLink.setOnClickListener {
            viewModel.setEmail(emailToken)
        }
    }

    override fun initData() {
    }

    override fun getData() {
        viewModel.setEmailStatus.observe(this, {
            binding.clickVerificationLink.stateButton()
            if (it) {
                "邮箱验证成功".showToast()
                ARouter.getInstance().build("/mine/setting/login_safe").navigation()
                finish()
            }
        })

    }
}