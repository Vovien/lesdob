package com.tubewiki.mine.view.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.checkTaskAndRun
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.widget.SplitEditText
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityInputCaptchaBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import org.greenrobot.eventbus.EventBus

/**
 * 验证码
 */
@Route(path = "/mine/login/captcha")
class InputCaptchaActivity : ViewModelActivity<CaptchaViewModel, ActivityInputCaptchaBinding>() {

    @Autowired
    @JvmField
    var phone: String? = null

    @Autowired
    @JvmField
    var type: Int = 0

    @Autowired(name = TagConstant.PAGE_TYPE)
    @JvmField
    var subType: Int = 0

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var openId: String = ""

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var nickName: String = ""

    @Autowired(name = TagConstant.PARAMS3)
    @JvmField
    var headUrl: String = ""

    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }


    override fun initView(savedInstanceState: Bundle?) {
        binding.textCountDown.setNormalText(StringUtils.getString(R.string.to_obtain))
            .setCountDownText("", "秒后重新获取")
            .setCloseKeepCountDown(false) //关闭页面保持倒计时开关
            .setCountDownClickable(false) //倒计时期间点击事件是否生效开关
            .setShowFormatTime(false) //是否格式化时间
            .setOnCountDownFinishListener {
                binding.textCountDown.setTextColor(ColorUtils.getColor(R.color.color_currency))
            }.setOnCountDownStartListener {
                binding.textCountDown.setTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
            }
            .setOnClickListener {
                binding.splitEditText.text?.clear()
                phone?.let {
                    // 除了登录和绑定微信以外的接口都传找回密码3
                    viewModel.sendCaptcha(
                        it,
                        when (type) {
                            CaptchaViewModel.RETRIEVE_PASSWORD -> CaptchaViewModel.RETRIEVE_PASSWORD
                            //更新手机号码时，如果是换新号码则传2，验证原号码传3
                            CaptchaViewModel.UPDATE_PHONE -> if (subType == 1) CaptchaViewModel.LOGIN else CaptchaViewModel.RETRIEVE_PASSWORD
                            else -> CaptchaViewModel.LOGIN
                        }
                    )
                }
                binding.textCountDown.startCountDown(60)
            }
        binding.textCountDown.startCountDown(60)

        binding.textPhone.text =
            StringUtils.getString(R.string.verification_code_has_been_sent, phone)

        binding.splitEditText.postDelayed(
            {
                binding.splitEditText.isFocusable = true
                binding.splitEditText.isFocusableInTouchMode = true
                binding.splitEditText.requestFocus()
                KeyboardUtils.showSoftInput(this)
            }, 500
        )
        //设置监听
        binding.splitEditText.setOnTextInputListener(object : SplitEditText.OnTextInputListener {
            override fun onTextInputChanged(text: String, length: Int) {
            }

            override fun onTextInputCompleted(text: String) {
                // 输入完成
                phone?.let {
                    if (type == CaptchaViewModel.LOGIN) {
                        started {
                            viewModel.authLogin(it, text).netCatch {
                                message.showToast()
                            }.next {
                                if (this != null) {
                                    loginSuccess(this)
                                }
                            }

                        }
                    } else if (type == CaptchaViewModel.BIND) {
                        //微信授权登录绑定手机号验证码验证成功后调用等接口
                        if (openId.isNotNullEmpty()) {
                            started {
                                viewModel.wxBindMobileLogin(
                                    it,
                                    text,
                                    openId,
                                    nickName,
                                    headUrl
                                ).netCatch {
                                    message.showToast()
                                }.next {
                                    loginSuccess(this)
                                }
                            }
                        }

                    } else if (type == CaptchaViewModel.LOG_OFF_ACCOUNT) {
                        // viewModel.checkAccount(text)

                        //注销账号
                        userAccountCancel(it, text)

                    } else if (type == CaptchaViewModel.UPDATE_PHONE) {
                        viewModel.verifyCaptcha(text, it)
                    } else {
                        viewModel.verify(3, text, it)
                    }
                }
            }

        })

    }

    fun userAccountCancel(phone: String, code: String) {
        started {
            viewModel.userAccountCancel(phone, code).netCatch {
                message.showToast()
            }.next {

                logoutJmbon()

                finish()
            }
        }
    }

    private fun loginSuccess(it: UserData) {

        loginView.bindDevice()
        Constant.saveUser(it)
        // 验证成功进入界面
//        if (it.user.guideStatus == 0) {
//            if (type == CaptchaViewModel.BIND) {
//                getString(R.string.phone_bind_successfully).showToast()
//                ActivityUtils.finishActivity(RetrievePasswordActivity::class.java)
//            } else {
//                getString(R.string.log_in_successfully).showToast()
//            }
//            ARouter.getInstance().navigation(IMiniToolProvider::class.java).toChooseHome(this, true)
//            finish()
//        } else {
//
//        }
//        if (!isNeedMain || index != 0) {
//            finish()
//            ActivityUtils.finishActivity(QuickLoginActivity::class.java)
//            ActivityUtils.finishActivity(LoginActivity::class.java)
//            if (index != 0) {
//                EventBus.getDefault().post(CommonEventHub.PageEvent(index))
//            }
//        } else {
//            ARouter.getInstance().build("/main/main")
//                .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                .navigation(this@InputCaptchaActivity)
//        }

        if (type == CaptchaViewModel.BIND) {
            getString(R.string.phone_bind_successfully).showToast()
        } else {
            getString(R.string.welcome_back).showToast()
        }
        checkTaskAndRun()
    }

    override fun initData() {
        viewModel.captchaVerify.observe(this) {
            if (it != null)
                loginSuccess(it)
//            else "登录失败".showToast()
        }
        viewModel.logOffCaptchaVerify.observe(this) {
            if (it.isNotNullEmpty()) {
                var intent = Intent()
                intent.putExtra("sign", it)
                setResult(101, intent)
                this@InputCaptchaActivity.finish()
            }
        }

        // 除登录以外的验证码
        viewModel.verifyCaptcha.observe(this) {
            if (type == CaptchaViewModel.UPDATE_PHONE) {
                if (subType == 1)
                    started {
                        viewModel.setNewMobile(phone ?: "", it).next {
                            if (phone.isNotNullEmpty()) {
                                Constant.userInfo.mobile = phone ?: ""
                            }
                            ARouter.getInstance().build("/mine/setting/password/success")
                                .withInt("type", type)
                                .navigation()
                            this@InputCaptchaActivity.finish()
                        }
                    }
                else {
                    ARouter.getInstance().build("/mine/setting/retrieve/password")
                        .withInt("type", type)
                        .withInt(TagConstant.PAGE_TYPE, 1)
                        .navigation()
                    this.finish()
                }
            }


        }
    }

    override fun getData() {
    }

    fun logoutJmbon() {
        // 直接退出
//        XGPushManager.unregisterPush(Utils.getApp())

        Constant.cleanLoginInfo()

        EventBus.getDefault().post(UserLoginEvent(false))

        ARouter.getInstance().build("/middleware/tort/submit")
            .withBoolean(
                TagConstant.PARAMS4,
                true
            ).withString(
                TagConstant.PARAMS,
                StringUtils.getString(R.string.see_you_again)
            )
            .withBoolean(TagConstant.PARAMS3, false)
            .navigation()

    }

}