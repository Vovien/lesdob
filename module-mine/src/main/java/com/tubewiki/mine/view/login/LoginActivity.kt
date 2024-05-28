package com.tubewiki.mine.view.login

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
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
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.apkdv.mvvmfast.view.LoadingDialog
import com.blankj.utilcode.util.*
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.config.libinit.onekey.MobileSDKInit
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.valid.action.SingleCall
import com.jmbon.pay.bean.WxLoginEvent
import com.jmbon.pay.ext.loginWx
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.MobileLoginError
import com.tubewiki.mine.databinding.ActivityLoginBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.login.model.OneKeyLoginViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

@Route(path = "/mine/login")
class LoginActivity :
    ViewModelActivity<OneKeyLoginViewModel, ActivityLoginBinding>(),
    NetworkUtils.OnNetworkStatusChangedListener {
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

    var isFirst = true


    override fun beforeViewInit() {
        super.beforeViewInit()
        ImmersionBar.with(this)
            .transparentBar()
            .init()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
//        if (quick) {
//            getLoginToken()
//        }

//        ActivityUtils.finishAllActivitiesExceptNewest()
        NetworkUtils.registerNetworkStatusChangedListener(this)
    }

    override fun onDisconnected() {
    }

    override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        //激活上传失败就重新上传
        if (!Constant.isActiveChannel) {
//            TpushInit.init()
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        NetworkUtils.unregisterNetworkStatusChangedListener(this)
        super.onDestroy()
    }

    override fun initView(savedInstanceState: Bundle?) {
//        MobileSDKInit.init(this)
        registerUIChange(loginView)
        registerUIChange(captchaView)

        binding.tvClose.layoutParams
        val statusHeight =
            StatusBarCompat.getStatusBarHeight(this) + binding.tvClose.paddingTop
        binding.tvClose.setPadding(
            binding.tvClose.paddingStart,
            statusHeight,
            0, 0
        )

        binding.tvClose.setOnSingleClickListener({
            finish()
        })

        binding.checkView.setOnCheckedChangeListener { buttonView, isChecked ->
            Constant.isCheckPrivacy = isChecked
        }

        binding.sbPhone.setOnSingleClickListener({
            ARouter.getInstance().build("/mine/login/quick").withBoolean("quick", false)
                .navigation()
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


        binding.llWxLogin.setOnClickListener {
            if (!binding.checkView.isChecked) {
                showTipsDialog()
            } else {
                loginWx(this)
            }
        }

    }

    override fun initData() {
        //激活上传
        if (!Constant.isActiveChannel) {
//            TpushInit.init()
        }
    }

    private fun loginSuccess(it: UserData) {

        Constant.saveUser(it)
        MobileSDKInit.mPhoneNumberAuthHelper?.quitLoginPage()

        // 验证成功进入界面
//        if (it.user.guideStatus == 0) {
//            getString(R.string.log_in_successfully).showToast()
//            ARouter.getInstance().navigation(IMiniToolProvider::class.java).toChooseHome(this, true)
//            ARouter.getInstance().build("/mine/login/gender/selection")
//                .withTransition(R.anim.activity_bottom_in, R.anim.activity_background)
//                .navigation(this@LoginActivity)
//        } else {
//
//        }
        getString(R.string.welcome_back).showToast()
        this.checkTaskAndRun()
    }

    override fun onResume() {
        super.onResume()

        if (configLoginView?.isQuickLoginBack == true && !isFirst) {
            finish()
        }

        //binding.checkView.isChecked = Constant.isCheckPrivacy

        if (isFirst && !Constant.isCheckPrivacy
        ) {
            showTipsDialog()
        }
        isFirst = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!Constant.isLogin) {
            SingleCall.getInstance().clear()
        }
    }

    override fun getData() {
    }

    var configLoginView: ConfigLoginView2? = null

    /**
     * 拉起授权页
     * @param timeout 超时时间
     */
    private fun getLoginToken() {
        MobileSDKInit.mPhoneNumberAuthHelper?.let {
            if (Constant.PHONE_NUMBER_AUTH) {
                //  MobAnalysisUtils.mInstance.sendEvent("Event_OwnNumbersQuickLoginPage")
            }
            configLoginView = ConfigLoginView2(this, it) {
                finish()
            }
            //showLoading()
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
                            "授权失败".showToast()
                        } else if (error.code.toInt() == 600009) {
                            //无法判运营商 表示没有一键授权页面
                            if (!Constant.isCheckPrivacy) {
                                binding.checkView?.postDelayed({
                                    //showTipsDialog()
                                    showTipsDialog()
                                }, 500)
                            }
                        }

                        if (error.code.toInt() != 700000) {
                            configLoginView?.isQuickLoginBack = false
                        }
                    } catch (e: Exception) {
                        LogUtils.e(e)
                        "授权失败".showToast()
                    }


                    cancelLogin(it)

                }
            }
            it.setUIClickListener { code, context, jsonObj ->
                configLoginView?.let {
                    //点击返回，⽤户取消免密登录
                    if (code == "700000") {
                        //  this@LoginActivity.finish()
                        if (!Constant.isLogin) {
                            SingleCall.getInstance().clear()
                        }
                    } else if (code == "700001") {
                        //点击使用其他手机号
                        MobAnalysisUtils.mInstance.sendEvent(UMEventKey.Event_PhoneNumberButton_Click)
                    } else if (code == "700002") {


                        if (it.anchorViewIsVisible()) {
                            it.goneAnchorView()
                            return@setUIClickListener
                        }
                        //点击了一键登录按钮
                        if (!Constant.isCheckPrivacy) {
                            it.showTipsDialog()
                        }
                        // getString(R.string.please_check_user_agreement_privacy).showToast()
                    } else if (code == "700003") {
                        Constant.isCheckPrivacy = !Constant.isCheckPrivacy
                        it.goneAnchorView()
                    }
                }

            }
            it.setAuthListener(mTokenResultListener)
            it.getLoginToken(this, 5000)

        }
    }


    private fun cancelLogin(it: PhoneNumberAuthHelper) {
        // 唤醒失败
        // dismissLoading()
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

        MobileSDKInit.mPhoneNumberAuthHelper?.let {
            cancelLogin(it)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {
        if (event.login) {
            this@LoginActivity.finish()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun wxLoginEvent(event: WxLoginEvent) {
        event.either.data1?.let {
            if (it == null || it.openid.isNullOrEmpty()) {
                "微信授权失败".showToast()
                return@let
            }
            //验证当前微信号是否绑定手机号
            lifecycleScope.launch {
                viewModel.wxLoginCheck(it.openid, it.nickname, it.headimgurl)
                    .onStart {
                        //微信授权回来可能是WXEntryActivity页面，所以做个判断
                        if (ActivityUtils.getTopActivity().javaClass.simpleName == "WXEntryActivity") {
                            showLoading2(ActivityUtils.getActivityList()[1])
                        } else {
                            showLoading2(ActivityUtils.getTopActivity())
                        }
                    }.netCatch {
                        dismissLoading2()

                        if (code.toInt() == 10010) {
                            //未绑定手机号
                            //微信授权成功后绑定手机号
                            ARouter.getInstance().build("/mine/setting/retrieve/password")
                                .withInt("type", CaptchaViewModel.BIND)
                                .withInt(TagConstant.PAGE_TYPE, 2)
                                .withString(TagConstant.PARAMS, it.openid)
                                .withString(TagConstant.PARAMS2, it.nickname)
                                .withString(TagConstant.PARAMS3, it.headimgurl)
                                .navigation()
                        } else {
                            message.showToast()
                        }
                    }.next {
                        dismissLoading2()
                        //微信登录成功
                        loginView.bindDevice()
                        loginSuccess(this)

                    }
            }


        }
    }

    private fun showTipsDialog() {
        binding.anchorView.let {
            if (it.isVisible()) {
                return@let
            }
            true.saveToMMKV(MMKVKey.PRIVATE_ONE_KEY_LOGIN_PAGE)
            it.alpha = 0f
            it.visible()

            val showAnimator =
                ObjectAnimator.ofFloat(binding.anchorView, "alpha", 0f, 1f).setDuration(200)
            showAnimator.interpolator = EaseCubicInterpolator()
            val scaleAnimation = ScaleAnimation(
                0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                1f
            )
            scaleAnimation.interpolator = EaseCubicInterpolator()
            scaleAnimation.duration = 200
            it.startAnimation(scaleAnimation)

            showAnimator.start()

            MainScope().launch {
                delay(4200)
                it.gone()
            }
        }
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