package com.tubewiki.mine.view.login

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.animation.addListener
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.middleware.utils.dp
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityLoginWelcomeBinding
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.model.UserInfoViewModel


/**
 * 首次登录欢迎页面
 */
@Route(path = "/mine/login/login_welcome")
class LoginWelcomeActivity :
    ViewModelActivity<UserInfoViewModel, ActivityLoginWelcomeBinding>() {

    private var isFirst = true
    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }


    override fun initView(savedInstanceState: Bundle?) {
        registerUIChange(loginView)


        MobAnalysisUtils.mInstance.sendEvent("Event_WelcomePage")
    }

    override fun initData() {
        binding.sbSelectOk2.setOnClickListener {
            ARouter.getInstance().navigation(IMiniToolProvider::class.java).toChooseHome(this, true)

            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        if (isFirst) {
            binding.sbSelectOk2.postDelayed({
                doAnimator()
            }, 200)
        }
        isFirst = false

    }

    private fun doAnimator() {
        val imageAlphaObgAnimator =
            ObjectAnimator.ofFloat(binding.ivFace, "alpha", 0f, 1.0f)

        imageAlphaObgAnimator.duration = 550
        imageAlphaObgAnimator.interpolator = EaseCubicInterpolator()


        var imageObgAnimator =
            ObjectAnimator.ofFloat(binding.ivFace, "translationY", 0f, -102f.dp().toFloat())

        //缩放动画
        val textAlpha = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f)
        val textTranslationY =
            PropertyValuesHolder.ofFloat("translationY", 0f, -110f.dp().toFloat())
        val objectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(binding.llWord, textAlpha, textTranslationY)
        objectAnimator.duration = 670
        objectAnimator.interpolator = EaseCubicInterpolator(0f, 0.47f, 0.63f, 1f)
        imageObgAnimator.interpolator = EaseCubicInterpolator(0f, 0.47f, 0.63f, 1f)
        imageObgAnimator.duration = 700

        imageAlphaObgAnimator.addListener(
            onEnd = {
                imageObgAnimator.start()
                binding.sbSelectOk2.postDelayed({ objectAnimator.start() }, 30)
            }
        )


        var buttonObgAnimator =
            ObjectAnimator.ofFloat(binding.sbSelectOk2, "alpha", 0.0f, 1.0f)
        buttonObgAnimator.interpolator = EaseCubicInterpolator()
        buttonObgAnimator.duration = 300

        imageAlphaObgAnimator.start()

        binding.sbSelectOk2.postDelayed({ buttonObgAnimator.start() }, 1150)

    }

    override fun getData() {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.activity_background, R.anim.activity_bottom_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.overridePendingTransition(R.anim.activity_background, R.anim.activity_bottom_out)
    }

}