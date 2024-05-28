package com.tubewiki.mine.view.login

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.view.LoadingDialog
import com.blankj.utilcode.util.*
import com.jmbon.middleware.animator.CustomPopScaleAlphaAnimator
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.libinit.onekey.MobileSDKInit
import com.jmbon.middleware.dialog.CustomLoginTipsBubblePopupView
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.SingleCall
import com.jmbon.pay.ext.loginWx
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.MobileLoginError
import com.tubewiki.mine.databinding.ActivityQuickLoginBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.login.model.OneKeyLoginViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = "/mine/login/quick")
class QuickLoginActivity :
    ViewModelActivity<OneKeyLoginViewModel, ActivityQuickLoginBinding>() {
    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }

    private val captchaView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(CaptchaViewModel::class.java)
    }

    @Autowired
    @JvmField
    var quick: Boolean = true

    private var lastPhone = ""

    var isShowTips = false
    var isFirst = true

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
//        if (quick)
//            getLoginToken()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun initView(savedInstanceState: Bundle?) {
//        MobileSDKInit.init(this)
        registerUIChange(loginView)
        registerUIChange(captchaView)

        binding.textZipCode.centerTextView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        binding.editPhone.addTextChangedListener(loginView.textWatch)

        binding.checkView.setOnCheckedChangeListener { buttonView, isChecked ->
            Constant.isCheckPrivacy = isChecked
        }

        binding.sbGetCaptcha.setOnSingleClickListener({
            if (RegexUtils.isMobileExact(binding.editPhone.text.toString())) {
                // 手机
                if (binding.checkView.isChecked) {
                    captchaView.sendCaptcha(
                        binding.editPhone.text.toString(),
                        CaptchaViewModel.LOGIN
                    )

                } else {
                    //showTipsDialog()
                    showTipsDialog2(this, binding.checkView)
                    //getString(R.string.please_check_user_agreement_privacy).showToast()
                    KeyboardUtils.hideSoftInput(this@QuickLoginActivity)
                    binding.sbGetCaptcha.performHapticFeedback(
                        HapticFeedbackConstants.LONG_PRESS,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                    )
                }

            } else {
                R.string.please_enter_correct_phone_number.showToast()
            }
        })
        binding.textUserAgreement.setOnClickListener {
            // 用户协议
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.USER_PROTOCOL)
                .withString("title", getString(R.string.about_terms_service))
                .navigation()
        }
        binding.textPrivacyPolicy.setOnClickListener {
            // 隐私
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.PRIVACY_PROTOCOL)
                .withString("title", getString(R.string.about_privacy_policy))
                .navigation()
        }

        binding.textUseOther.setOnClickListener {
            val phone = binding.editPhone.text.toString()
            val phoneOK = RegexUtils.isMobileExact(phone)
            ARouter.getInstance().build("/mine/login/mobile")
                .withString(TagConstant.PARAMS, if (phoneOK) phone else "")
                .navigation()
        }

        binding.llWxLogin.setOnClickListener {
            if (!binding.checkView.isChecked) {
                showTipsDialog2(this, binding.checkView)
            } else {
                loginWx(this)
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

        KeyboardUtils.showSoftInput(binding.editPhone)

    }

    override fun initData() {
        loginView.textSize.observe(this) {
            binding.apply {
                if (it > 0) {
                    editPhone.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                } else {
                    editPhone.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }

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


    private fun loginSuccess(it: UserData) {
        Constant.saveUser(it)
        MobileSDKInit.mPhoneNumberAuthHelper?.quitLoginPage()
        // 验证成功进入界面
        getString(R.string.welcome_back).showToast()
        this.checkTaskAndRun()
//        if (it.user.guideStatus == 0) {
//            getString(R.string.log_in_successfully).showToast()
//            ARouter.getInstance().navigation(IMiniToolProvider::class.java).toChooseHome()
//        } else {
//
//            // this@QuickLoginActivity.finish()
//        }
    }

    override fun onResume() {
        super.onResume()

        //binding.checkView.isChecked = Constant.isCheckPrivacy

        if (isFirst && !Constant.isCheckPrivacy
        ) {
            // showTipsDialog()
            showTipsDialog2(this, binding.checkView)
        }
        isFirst = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!Constant.isLogin) {
            SingleCall.getInstance().clear()
        }
    }

    private fun firstLogin() {
        ARouter.getInstance().build("/mine/login/captcha")
            .withString("phone", binding.editPhone.text.toString())
            .withInt("type", CaptchaViewModel.LOGIN)
            .navigation()
        binding.textCountDown.startCountDown(60)
    }

    override fun getData() {
    }


    /**
     * 拉起授权页
     * @param timeout 超时时间
     */
    private fun getLoginToken() {
        MobileSDKInit.mPhoneNumberAuthHelper?.let {
            if (Constant.PHONE_NUMBER_AUTH) {
                MobAnalysisUtils.mInstance.sendEvent("Event_OwnNumbersQuickLoginPage")
            }
            var configLoginView = ConfigLoginView(this, it)
            showLoading()
            val mTokenResultListener = object : TokenResultListener {
                override fun onTokenSuccess(s: String) {
                    dismissLoading()
                    var tokenRet: TokenRet? = null
                    try {
                        tokenRet = TokenRet.fromJson(s)
                        if (ResultCode.CODE_SUCCESS == tokenRet.code) {
                            // 获取 token 成功
                            getResultWithToken(tokenRet.token)
                            MobileSDKInit.mPhoneNumberAuthHelper?.setAuthListener(null)

                        }
                    } catch (e: Exception) {
                        LogUtils.e(e)
                    }
                }

                override fun onTokenFailed(s: String) {
                    //{"carrierFailedResultData":"","code":"600009","msg":"无法判运营商","requestCode":0,"requestId":"4f556698-59ff-48f8-8ff9-f3f555a87c5e","vendorName":"unknown"}
                    // Log.e("TAG", s)
                    //统一提示失败
                    try {
                        val error = GsonUtils.fromJson(s, MobileLoginError::class.java)
                        //600008  "移动网络未开启"
                        if ((Constant.PHONE_NUMBER_AUTH && error.code.toInt() != 700000) || error.code.toInt() == 600008) {
                            // "授权失败".showToast()  //小米审核
                        } else if (error.code.toInt() == 600009) {
                            //无法判运营商 表示没有一键授权页面
                            KeyboardUtils.showSoftInput(binding.editPhone)
                            if (!Constant.isCheckPrivacy) {
                                binding.anchorView?.postDelayed({
                                    //showTipsDialog()
                                    showTipsDialog2(this@QuickLoginActivity, binding.checkView)
                                }, 500)
                            }
                        }
                    } catch (e: Exception) {
                        LogUtils.e(e)
                        // "授权失败".showToast()
                    }

//                    try {
//                        val error = GsonUtils.fromJson(s, MobileLoginError::class.java)
//                        if (Constant.PHONE_NUMBER_AUTH && error.code.toInt() > 600004) {
//                            //error.msg.showToast()
//                            "授权失败".showToast()
//                        }
//                    } catch (e: Exception) {
//                        LogUtils.e(e)
//                    }
                    cancelLogin(it)

                }
            }
            it.setUIClickListener { code, context, jsonObj ->
                //点击返回，⽤户取消免密登录
                if (code == "700000") {
                    this@QuickLoginActivity.finish()
                    if (!Constant.isLogin) {
                        SingleCall.getInstance().clear()
                    }
                } else if (code == "700001") {
                    //点击使用其他手机号
                    MobAnalysisUtils.mInstance.sendEvent("Event_OwnNumbersQuickLoginPage_OtherMobileNumbers_Button_Click")
                } else if (code == "700002") {

                    if (configLoginView.anchorViewIsVisible()) {
                        configLoginView.goneAnchorView()
                        return@setUIClickListener
                    }
                    //点击了一键登录按钮
                    MobAnalysisUtils.mInstance.sendEvent("Event_OwnNumbersQuickLoginPage_OneClicLogin_Button_Click")
                    if (!Constant.isCheckPrivacy) {
                        configLoginView.showTipsDialog()
                    }
                    // getString(R.string.please_check_user_agreement_privacy).showToast()
                } else if (code == "700003") {
                    Constant.isCheckPrivacy = !Constant.isCheckPrivacy
                    configLoginView.goneAnchorView()
                }
            }
            it.setAuthListener(mTokenResultListener)
            it.getLoginToken(this, 5000)
        }
    }

    private fun cancelLogin(it: PhoneNumberAuthHelper) {
        // 唤醒失败
        dismissLoading()
        //如果环境检查失败 使用其他登录方式
        it.quitLoginPage()
        it.setAuthListener(null)
    }

    /**
     * 授权已经成功，可以获取电话号码了
     */
    fun getResultWithToken(token: String?) {
        if (token.isNotNullEmpty()) {
            lifecycleScope.launch {
                viewModel.quickLogin(token!!).netCatch {
                    // 异常
                    message.showToast()
                    MobileSDKInit.mPhoneNumberAuthHelper?.let {
                        cancelLogin(it)
                    }
                }.next {
                    loginView.bindDevice()
                    loginSuccess(this)
                }
            }
        }
    }

    override fun finish() {
        super.finish()

//        if (quick) {
//            MobileSDKInit.mPhoneNumberAuthHelper?.let {
//                cancelLogin(it)
//            }
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {
        if (event.login) {
            this@QuickLoginActivity.finish()
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun wxLoginEvent(event: WxLoginEvent) {
//        event.either.data1?.let {
//
//            //验证当前微信号是否绑定手机号
//            lifecycleScope.launch {
//                viewModel.wxLoginCheck(it.openid, it.nickname, it.headimgurl)
//                    .onStart {
//                        //微信授权回来可能是WXEntryActivity页面，所以做个判断
//                        if (ActivityUtils.getTopActivity().javaClass.simpleName == "WXEntryActivity") {
//                            showLoading2(ActivityUtils.getActivityList()[1])
//                        } else {
//                            showLoading2(ActivityUtils.getTopActivity())
//                        }
//                    }.netCatch {
//                        dismissLoading2()
//                        message.showToast()
//                    }.next {
//                        dismissLoading2()
//                        if (type == 1) {
//                            //微信登录成功
//                            loginView.bindDevice()
//                            loginSuccess(this)
//                        } else {
//                            //微信授权成功后绑定手机号
//                            ARouter.getInstance().build("/mine/setting/retrieve/password")
//                                .withInt("type", CaptchaViewModel.BIND)
//                                .withInt(TagConstant.PAGE_TYPE, 2)
//                                .withString(TagConstant.PARAMS, sign)
//                                .navigation()
//                        }
//                    }
//            }
//
//
//        }
//    }

    /**
     * view形式
     */
    fun showTipsDialog() {
        binding.anchorView?.let {
            if (it.isVisible()) {
                return@let
            }
            isShowTips = true
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

//        if (mmkv.decodeBool(MMKVKey.PRIVATE_QUICK_LOGIN_PAGE, false)) {
//            return
//        }

        var popupView = CustomLoginTipsBubblePopupView(
            context,
            title
        )
            .setBubbleBgColor(context.resources.getColor(R.color.transparent))  //气泡背景


        XPopup.Builder(context)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            .atView(anchorView)
            .offsetX(10f.dp())
            .isRequestFocus(false)
            .isClickThrough(true)
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

    private var loadingPopup2: BasePopupView? = null

    /**
     * 打开等待框 兼容天翼认证登录界面
     */
    fun showLoading2(context: Context) {
        if (loadingPopup2 == null) {
            loadingPopup2 = XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .hasShadowBg(false)
                .asCustom(LoadingDialog(context))
        }
        loadingPopup2?.apply {
            if (!isShow)
                show()
        }
    }

    /**
     * 关闭等待框
     */
    fun dismissLoading2() {
        loadingPopup2?.dismiss()
    }


}