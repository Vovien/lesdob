package com.tubewiki.mine.view.setting

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.*
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityLoginOffIdentifyLayoutBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.model.SettingViewModel


/**
 * 注销身份验证页面
 */
@Route(path = "/mine/setting/login_off_identify")
class LogOffIdentifyActivity :
    ViewModelActivity<SettingViewModel, ActivityLoginOffIdentifyLayoutBinding>() {
    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }
    private val captchaView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(CaptchaViewModel::class.java)
    }

    private var lastPhone = ""
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)

    }


    override fun initView(savedInstanceState: Bundle?) {
        registerUIChange(captchaView)
        binding.editPhone.addTextChangedListener(loginView.textWatch)
        binding.sbGetCaptcha.setOnClickListener {
            if (RegexUtils.isMobileExact(binding.editPhone.text.toString())) {
                if (Constant.userInfo.mobile != binding.editPhone.text.toString()) {
                    "输入手机号非当前登录账号".showToast()
                    return@setOnClickListener
                }

                // 手机
                captchaView.sendCaptcha(
                    binding.editPhone.text.toString(),
                    CaptchaViewModel.RETRIEVE_PASSWORD
                )


            } else {
                R.string.please_enter_correct_phone_number.showToast()
            }
        }

        binding.textCountDown.setNormalText(StringUtils.getString(R.string.to_obtain))
            .setCountDownText("", "秒后重新获取")
            .setNormalText("")
            .setCloseKeepCountDown(false) //关闭页面保持倒计时开关
            .setCountDownClickable(false) //倒计时期间点击事件是否生效开关
            .setShowFormatTime(false)
            .setOnCountDownTickListener {
                binding.sbGetCaptcha.isEnabled = 0 == it.toInt()
                binding.sbGetCaptcha.text =
                    if (0 == it.toInt()) StringUtils.getString(R.string.get_verification_code) else binding.textCountDown.text.toString()

            }

    }

    override fun initData() {
        loginView.textSize.observe(this) {
            binding.apply {
                if (it == 11) {
                    val phone = editPhone.text.toString()
                    if (textCountDown.text.isEmpty()) {
                        sbGetCaptcha.isEnabled = true
                        sbGetCaptcha.text = StringUtils.getString(R.string.get_verification_code)
                    } else {
                        if (lastPhone != phone) {
                            textCountDown.cancel()
                            sbGetCaptcha.isEnabled = true
                            sbGetCaptcha.text =
                                StringUtils.getString(R.string.get_verification_code)
                        }
                    }
                    lastPhone = phone

                    //输入手机号
                    MobAnalysisUtils.mInstance.sendEvent("Event_QuickLoginPage_PhoneNumbers_Input")

                } else {
                    sbGetCaptcha.isEnabled = false
                }
            }
        }
        captchaView.captchaStatus.observe(this) {
            if (!it) {
                binding.sbGetCaptcha.isEnabled = true
                binding.sbGetCaptcha.text = StringUtils.getString(R.string.get_verification_code)
                return@observe
            }
            firstLogin()
        }

    }

    private fun firstLogin() {
        ARouter.getInstance().build("/mine/login/captcha")
            .withString("phone", binding.editPhone.text.toString())
            .withInt("type", CaptchaViewModel.LOG_OFF_ACCOUNT)
            .navigation(this, 100)
        binding.textCountDown.startCountDown(60)
    }

    override fun getData() {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == 101) {
            "身份验证成功".showToast()
            setResult(101, data)
            finish()
        }
    }


}