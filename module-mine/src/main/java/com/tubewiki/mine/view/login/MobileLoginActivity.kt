package com.tubewiki.mine.view.login

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.animator.CustomPopScaleAlphaAnimator
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.dialog.CustomLoginTipsBubblePopupView
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.checkTaskAndRun
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityMobileNumberLoginBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.jmbon.widget.progress_button.JmbonButton
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import kotlinx.coroutines.flow.onCompletion
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 手机-密码登录
 */
@Route(path = "/mine/login/mobile")
class MobileLoginActivity : ViewModelActivity<LoginViewModel, ActivityMobileNumberLoginBinding>(),
    TextWatcher {
    private var showPassword = false

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var phone: String = ""
    var isFirst = true

    override fun initView(savedInstanceState: Bundle?) {
        titleBarView.setRightView(R.layout.custom_titlebar_setting_view)
        binding.imageSeePass.setOnClickListener {
            showPassword = !showPassword
            binding.imageSeePass.setImageResource(if (showPassword) R.drawable.login_pwd_show_icon else R.drawable.login_pwd_hide_icon)
            binding.editPasswd.transformationMethod =
                if (showPassword) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            binding.editPasswd.setSelection(binding.editPasswd.text?.length ?: 0)
        }
        // 自动输入手机号
        if (phone.isNotNullEmpty())
            binding.editPhone.setText(phone)

        titleBarView.rightCustomView.setOnClickListener {
            ARouter.getInstance().build("/mine/about/activity").navigation()
        }
        binding.textRetrievePassword.setOnClickListener {
            ARouter.getInstance().build("/mine/setting/retrieve/password")
                .withInt("type", CaptchaViewModel.RETRIEVE_PASSWORD)
                .navigation()
        }

        binding.editPhone.setOnFocusChangeListener { v, hasFocus ->
            binding.llPhone.setBackgroundResource(if (hasFocus) R.drawable.shape_input_login_focus else R.drawable.shape_input_login)
        }
        binding.editPasswd.setOnFocusChangeListener { v, hasFocus ->
            binding.framePass.setBackgroundResource(if (hasFocus) R.drawable.shape_input_login_focus else R.drawable.shape_input_login)
        }

        binding.sbLogin.setOnClickListener {
            if (binding.checkView.isChecked) {
                started {
                    viewModel.verify(
                        binding.editPasswd.text.toString(),
                        binding.editPhone.text.toString()
                    )
                        .netCatch {
                            if (code.toInt() != 802)
                                binding.textPassError.text = message ?: ""
                            else binding.textPassError.text =
                                StringUtils.getString(R.string.account_password_error)
                            binding.textPassError.visible()
                        }.onCompletion {
                            binding.sbLogin.stateButton()
                        }.next {
                            binding.textPassError.gone()
                            viewModel.bindDevice()
                            loginSuccess(this)
                        }
                }

                binding.sbLogin.stateLoadingNetClick()
            } else {
                //showTipsDialog()
                showTipsDialog2(this@MobileLoginActivity, binding.checkView)
                //getString(R.string.please_check_user_agreement_privacy).showToast()
                KeyboardUtils.hideSoftInput(this@MobileLoginActivity)
                binding.sbLogin.performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
            }
        }

        binding.textUserAgreement.setOnClickListener {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.USER_PROTOCOL)
                .withString("title", getString(R.string.about_terms_service))
                .navigation()
        }
        binding.textPrivacyPolicy.setOnClickListener {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.PRIVACY_PROTOCOL)
                .withString("title", getString(R.string.about_privacy_policy))
                .navigation()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirst && !com.apkdv.mvvmfast.utils.mmkv.decodeBool(
                MMKVKey.PRIVATE_PASSWORD_LOGIN_PAGE,
                false
            )
        ) {
            //showTipsDialog()
            showTipsDialog2(this@MobileLoginActivity, binding.checkView)
        }
        isFirst = false
    }

    override fun initData() {
        binding.editPhone.addTextChangedListener(this)
        binding.editPasswd.addTextChangedListener(this)
    }

    private fun loginSuccess(it: UserData) {
        Constant.saveUser(it)
        // 验证成功进入界面
        getString(R.string.welcome_back).showToast()
        this.checkTaskAndRun()
//        if (it.user.guideStatus == 0) {
//            getString(R.string.log_in_successfully).showToast()
//            ARouter.getInstance().navigation(IMiniToolProvider::class.java).toChooseHome()
//            this.finish()
//        } else {
//
//        }
    }

    override fun getData() {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (binding.sbLogin.getStatus() == JmbonButton.LoginStatus.IS_BUTTON_STATE) {
            binding.sbLogin.isEnabled =
                binding.editPasswd.text?.length ?: 0 >= 8 && binding.editPhone.text?.length == 11
        }

        binding.editPhone.typeface =
            if (binding.editPhone.text.isNotNullEmpty()) Typeface.defaultFromStyle(Typeface.BOLD) else Typeface.defaultFromStyle(
                Typeface.NORMAL
            )

        binding.editPasswd.typeface =
            if (binding.editPasswd.text.isNotNullEmpty()) Typeface.defaultFromStyle(Typeface.BOLD) else Typeface.defaultFromStyle(
                Typeface.NORMAL
            )


    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
        ARouter.getInstance().inject(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {
        if (event.login) {
            finish()
        }
    }

    fun showTipsDialog() {
        binding.anchorView?.let {
            if (it.isVisible()) {
                return@let
            }
            true.saveToMMKV(MMKVKey.PRIVATE_PASSWORD_LOGIN_PAGE)
            it.alpha = 0f
            it.visible()

            val showAnimator =
                ObjectAnimator.ofFloat(it, "alpha", 0f, 1f).setDuration(200)
            showAnimator.interpolator = EaseCubicInterpolator()
            var scaleAnimation = ScaleAnimation(
                0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                1f
            )
            scaleAnimation.interpolator = EaseCubicInterpolator()
            scaleAnimation.duration = 200
            it.startAnimation(scaleAnimation)

            showAnimator.start()

            it.postDelayed({
                it.gone()
            }, 1200)
        }

    }

    /**
     * dialog形式
     */
    fun showTipsDialog2(context: Context, anchorView: View) {

        var title = "请勾选用户协议和隐私\n政策"

//        if (mmkv.decodeBool(MMKVKey.PRIVATE_PASSWORD_LOGIN_PAGE, false)) {
//            return
//        }
        true.saveToMMKV(MMKVKey.PRIVATE_PASSWORD_LOGIN_PAGE)

        var popupView = CustomLoginTipsBubblePopupView(
            context,
            title
        )
            .setBubbleBgColor(context.resources.getColor(R.color.transparent))  //气泡背景


        XPopup.Builder(context)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            .atView(anchorView)
            .offsetX(15f.dp())
            // .offsetY(-30f.dp())
            .isClickThrough(true)
            .isRequestFocus(false)
            .autoOpenSoftInput(false)
            .popupAnimation(PopupAnimation.ScaleAlphaFromLeftBottom)
            .customAnimator(
                CustomPopScaleAlphaAnimator(
                    popupView.popupContentView,
                    200,
                    PopupAnimation.ScaleAlphaFromLeftBottom
                )
            )
            .animationDuration(200)
            .hasShadowBg(false) // 去掉半透明背景
            .asCustom(
                popupView
            )
            .show()

    }
}