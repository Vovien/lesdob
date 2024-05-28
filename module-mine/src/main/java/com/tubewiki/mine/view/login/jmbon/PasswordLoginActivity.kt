package com.tubewiki.mine.view.login.jmbon

import android.graphics.Color
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.ActivityUtils
import com.jmbon.middleware.animator.CustomPopScaleAlphaAnimator
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityPasswordLoginBinding
import com.tubewiki.mine.view.login.QuickLoginActivity
import com.tubewiki.mine.view.login.utils.CheckPrivacyTipsDialog
import com.tubewiki.mine.view.login.utils.setPrivacyPolicy

@Route(path = "/mine/jmbon/password/login")
class PasswordLoginActivity : ViewModelActivity<AuthLoginViewModel, ActivityPasswordLoginBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        setTitleName("LesDoB授权")
        binding.apply {
            textCaptchaLogin.setOnSingleClickListener({
                this@PasswordLoginActivity.finish()
            })
            textPrivacyPolicy.setPrivacyPolicy()


            edPhone.addTextChangedListener {
                isEditEmpty()
            }
            edPwd.addTextChangedListener {
                isEditEmpty()
            }


            tvLogin.setOnSingleClickListener({
                if (binding.checkView.isChecked.not()) {
                    // 显示 check
                    val tipsView =
                        CheckPrivacyTipsDialog(this@PasswordLoginActivity).setBubbleBgColor(Color.TRANSPARENT)
                    XPopup.Builder(this@PasswordLoginActivity)
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
                    started {
                        viewModel.doLogin(
                            AuthLoginViewModel.LOGIN_BY_PASSWORD,
                            edPhone.text.toString(),
                            edPwd.text.toString()
                        ).next {
                            loginSuccess(this)
                        }
                    }
                }
            })
        }
    }


    private fun loginSuccess(it: UserData) {
//        loginView.bindDevice()
        Constant.saveUser(it)

        // 验证成功进入界面
        if (it.user.sex == 0 || it.user.pregnantStatus == 0) {
            getString(R.string.log_in_successfully).showToast()
            ARouter.getInstance().build("/mine/login/login_welcome")
                .withTransition(R.anim.activity_bottom_in, R.anim.activity_background)
                .navigation(this@PasswordLoginActivity)
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

    private fun ActivityPasswordLoginBinding.isEditEmpty() {
        tvLogin.isEnabled = edPhone.text.isNotNullEmpty() && edPwd.text.isNotNullEmpty()
    }

    override fun initData() {
        
    }

    override fun getData() {
        
    }
}