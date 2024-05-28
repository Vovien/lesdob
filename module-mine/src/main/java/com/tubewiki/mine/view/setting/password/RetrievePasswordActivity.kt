package com.tubewiki.mine.view.setting.password

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityRetrievePasswordBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 找回密码
 * type 1
 * 设置新密码
 * type 2
 *
 * 手机号 身份验证
 */
@Route(path = "/mine/setting/retrieve/password")
class RetrievePasswordActivity :
    ViewModelActivity<CaptchaViewModel, ActivityRetrievePasswordBinding>() {

    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }


    @Autowired
    @JvmField
    var type: Int = 0

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var openId: String = ""

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var nickName: String = ""

    @Autowired(name = TagConstant.PARAMS3)
    @JvmField
    var headUrl: String = ""

    @Autowired(name = TagConstant.PAGE_TYPE)
    @JvmField
    var subType: Int = 0


    private var lastPhone = ""
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun initView(savedInstanceState: Bundle?) {

        if (subType == 1) {
            //登录
            setNewPhoneNumber()
        } else if (subType == 2) {
            //授权后绑定手机号
            setBindPhoneNumber()
        } else {
            if (type == CaptchaViewModel.RETRIEVE_PASSWORD) retrievePassword()
            else
                setNewPassword()
        }

        binding.editPhone.post {
            KeyboardUtils.showSoftInput(binding.editPhone)
        }

        binding.sbGetCaptcha.setOnClickListener {
            if (RegexUtils.isMobileExact(binding.editPhone.text.toString())) {
                // 手机 验证码 发送都是一个接口
                viewModel.sendCaptcha(
                    binding.editPhone.text.toString(),
                    when (type) {
                        CaptchaViewModel.RETRIEVE_PASSWORD -> CaptchaViewModel.RETRIEVE_PASSWORD
                        //更新手机号码时，如果是换新号码则传2，验证原号码传3
                        CaptchaViewModel.UPDATE_PHONE -> if (subType == 1) CaptchaViewModel.LOGIN else CaptchaViewModel.RETRIEVE_PASSWORD
                        else -> CaptchaViewModel.LOGIN
                    } //除了找回密码和注销类型是3，其他都是2，因为没有注册逻辑
                )

            } else {
                R.string.please_enter_correct_phone_number.showToast()
            }
        }
        binding.editPhone.addTextChangedListener(loginView.textWatch)
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

    private fun setNewPhoneNumber() {
        binding.textMobileTitle.text = getString(R.string.enter_new_phone_number)
        binding.textDescription.text = getString(R.string.new_phone_number_will_be_as_login_account)

    }

    private fun setBindPhoneNumber() {
        binding.textMobileTitle.text = getString(R.string.bind_new_phone_number)
        binding.textDescription.text = getString(R.string.bind_phone_tips)

    }

    private fun setNewPassword() {
        binding.textMobileTitle.text = getString(R.string.authentication)
        binding.textDescription.text =
            getString(R.string.input_current_for_phone_number_to_receive_verification_code)
    }

    private fun retrievePassword() {


    }

    override fun initData() {
        viewModel.captchaStatus.observe(this) {
            if (it) {
                if (type == CaptchaViewModel.BIND && openId.isNotNullEmpty()) {

                    ARouter.getInstance().build("/mine/login/captcha")
                        .withString("phone", binding.editPhone.text.toString())
                        .withInt("type", type)
                        .withInt(TagConstant.PAGE_TYPE, subType)
                        .withString(TagConstant.PARAMS, openId)
                        .withString(TagConstant.PARAMS2, nickName)
                        .withString(TagConstant.PARAMS3, headUrl)
                        .navigation()

                } else {
                    ARouter.getInstance().build("/mine/login/captcha")
                        .withString("phone", binding.editPhone.text.toString())
                        .withInt("type", type)
                        .withInt(TagConstant.PAGE_TYPE, subType)
                        .navigation()
                }

                binding.textCountDown.startCountDown(60)
            }
        }
        loginView.textSize.observe(this)
        {
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
                } else {
                    sbGetCaptcha.isEnabled = false
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {
        if (event.login) {
            this@RetrievePasswordActivity.finish()
        }
    }

    override fun getData() {
    }
}