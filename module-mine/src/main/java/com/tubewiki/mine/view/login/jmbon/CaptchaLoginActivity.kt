package com.tubewiki.mine.view.login.jmbon

import android.graphics.Color
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.animator.CustomPopScaleAlphaAnimator
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.Color
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityCaptchaLoginBinding
import com.tubewiki.mine.view.login.QuickLoginActivity
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.login.utils.CheckPrivacyTipsDialog
import com.tubewiki.mine.view.login.utils.setPrivacyPolicy


@Route(path = "/mine/jmbon/captcha/login")
class CaptchaLoginActivity : ViewModelActivity<CaptchaViewModel, ActivityCaptchaLoginBinding>() {


    @Autowired(name = TagConstant.IS_CHECK)
    @JvmField
    var checked: Boolean = false

    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }


    override fun initView(savedInstanceState: Bundle?) {
        setTitleName("LesDoB授权")
        titleBarView.leftImageButton.setImageResource(R.drawable.icon_nav_close)
        binding.apply {

            textCountDown.setFirstText(StringUtils.getString(R.string.get_verification_code))
                .setNormalText(StringUtils.getString(R.string.to_obtain))
                .setCountDownText("", "秒后重新获取")
                .setCloseKeepCountDown(false) //关闭页面保持倒计时开关
                .setCountDownClickable(false) //倒计时期间点击事件是否生效开关
                .setShowFormatTime(false) //是否格式化时间
                .setOnCountDownFinishListener {
                    binding.textCountDown.setTextColor(ColorUtils.getColor(R.color.color_currency))
                }.setOnCountDownStartListener {
                    binding.textCountDown.setTextColor(ColorUtils.getColor(R.color.text_26_color))
                }.setOnCountDownCancelListener {
                    if (RegexUtils.isMobileExact(edPhone.text.toString())) {
                        textCountDown.setTextColor(if (textCountDown.isEnabled) R.color.color_currency.Color else R.color.text_26_color.Color)
                    } else {
                        textCountDown.setTextColor(R.color.text_26_color.Color)
                    }
                }
                .setOnClickListener {
                    if (!RegexUtils.isMobileExact(edPhone.text.toString())) {
                        return@setOnClickListener
                    }
                    if (edPhone.text.isNotNullEmpty()) {
                        binding.textCountDown.startCountDown(60)
                        viewModel.sendCaptcha(
                            edPhone.text.toString(),
                            CaptchaViewModel.LOGIN)
                        // captchaView.sendCaptcha(
                        //                    binding.editPhone.text.toString(),
                        //                    CaptchaViewModel.LOGIN, 0
                        //                )
                    } else {
                        "请输入手机号码".showToast()
                    }

                }
//            textCountDown.
            //
            textPrivacyPolicy.setPrivacyPolicy()

            textLoginByPasswd.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/jmbon/password/login").navigation()
            })
            edPhone.addTextChangedListener {
                isEditEmpty()
                if (RegexUtils.isMobileExact(edPhone.text.toString())) {
                    textCountDown.setTextColor(if (textCountDown.isEnabled) R.color.color_currency.Color else R.color.text_26_color.Color)
                } else {
                    textCountDown.setTextColor(R.color.text_26_color.Color)
                }
            }
            edCaptcha.addTextChangedListener {
                isEditEmpty()
            }

            tvLogin.setOnSingleClickListener({
                if (binding.checkView.isChecked.not()) {
                    // 显示 check
                    val tipsView =
                        CheckPrivacyTipsDialog(this@CaptchaLoginActivity).setBubbleBgColor(Color.TRANSPARENT)
                    XPopup.Builder(this@CaptchaLoginActivity)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(binding.checkView)
                        .isRequestFocus(false)
                        .autoOpenSoftInput(false)
                        .popupAnimation(PopupAnimation.ScaleAlphaFromLeftBottom)
                        .customAnimator(
                            CustomPopScaleAlphaAnimator(
                                tipsView.popupContentView,
                                200,
                                PopupAnimation.ScaleAlphaFromLeftBottom
                            )
                        )
                        .offsetX(8f.dp())
                        .offsetY((-8f).dp())
                        .animationDuration(200)
                        .hasShadowBg(false) // 去掉半透明背景
                        .asCustom(tipsView)
                        .show()
                } else {
//                    viewModel.verify(edCaptcha.text.toString(), edPhone.text.toString())
                }
            })
            if (checked) {
                binding.checkView.isChecked = true
            }

        }
    }


    private fun loginSuccess(it: UserData) {
        loginView.bindDevice()
        Constant.saveUser(it)

        // 验证成功进入界面
        if (it.user.sex == 0 || it.user.pregnantStatus == 0) {
            getString(R.string.log_in_successfully).showToast()
            ARouter.getInstance().build("/mine/login/login_welcome")
                .withTransition(R.anim.activity_bottom_in, R.anim.activity_background)
                .navigation(this@CaptchaLoginActivity)
        } else if (it.user.cityId == 0 && it.user.countryId == 0) {
            getString(R.string.log_in_successfully).showToast()
            ARouter.getInstance().build("/mine/login/initial/setup")
                .withBoolean(TagConstant.FROM_JMB, true)
                .navigation()
        } else {
            getString(R.string.welcome_back).showToast()
            ARouter.getInstance().build("/main/main")
                .navigation()
        }
        ActivityUtils.finishActivity(QuickLoginActivity::class.java)
        finish()
    }

    private fun ActivityCaptchaLoginBinding.isEditEmpty() {
        tvLogin.isEnabled = edPhone.text.isNotNullEmpty() && edCaptcha.text.isNotNullEmpty()
    }

    override fun initData() {
        viewModel.captchaVerify.observe(this) {
            if (it != null)
                loginSuccess(it)
//            else "登录失败".showToast()
        }

        viewModel.captchaStatus.observe(this) {
            if (!it) {
                binding.textCountDown.cancel()
                return@observe
            }
        }
    }

    override fun getData() {
    }
}