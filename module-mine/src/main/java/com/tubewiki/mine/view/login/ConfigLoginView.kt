package com.tubewiki.mine.view.login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.isVisible
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import com.jmbon.middleware.utils.dp
import com.jmbon.pay.ext.loginWx
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig
import com.mobile.auth.gatewayauth.AuthUIConfig
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import com.tubewiki.mine.R
import java.util.Timer
import java.util.TimerTask


class ConfigLoginView(
    val context: Context,
    val mAuthHelper: PhoneNumberAuthHelper
) {
    var anchorView: View? = null

    init {
        //val scale = 118f / (734 + 44) //动态计算不屏幕比例
        val height = ScreenUtils.getScreenHeight()

        val scale = height.toFloat() / (734f + 44).dp() //690+44+44动态计算不屏幕比例

        Log.e("TAG", "${scale}")


        mAuthHelper.removeAuthRegisterViewConfig()
        mAuthHelper.removeAuthRegisterXmlConfig()
        var authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
        }

        mAuthHelper.addAuthRegisterXmlConfig(
            AuthRegisterXmlConfig.Builder()
                .setLayout(
                    R.layout.layout_quick_login,
                    object : AbstractPnsViewDelegate() {
                        override fun onViewCreated(view: View) {

                            view?.setOnClickListener {
                                goneAnchorView()
                            }

                            //动态调整位置
                            var textMobile = findViewById(R.id.text_mobile_title)
                            var params = textMobile.layoutParams as FrameLayout.LayoutParams
                            params.topMargin = (scale * 138f.dp()).toInt()
                            textMobile.layoutParams = params

                            var textCreateTips = findViewById(R.id.tv_create_tips)
                            params = textCreateTips.layoutParams as FrameLayout.LayoutParams
                            params.topMargin = (scale * 179f.dp()).toInt()
                            textCreateTips.layoutParams = params

                            var textUserOther = findViewById(R.id.text_use_other)
                            params = textUserOther.layoutParams as FrameLayout.LayoutParams
                            params.topMargin = (scale * 360f.dp()).toInt()
                            textUserOther.layoutParams = params

                            var llWx = findViewById(R.id.ll_wx_login)
                            params = llWx.layoutParams as FrameLayout.LayoutParams
                            params.topMargin = (scale * 500f.dp()).toInt()
                            llWx.layoutParams = params

                            anchorView = findViewById(R.id.anchor_view)
                          //  params = anchorView?.layoutParams as FrameLayout.LayoutParams
                          //  params.topMargin = (scale * 370f.dp()).toInt()
                         //   anchorView?.layoutParams = params

                            findViewById(R.id.text_use_other).setOnClickListener {

                                goneAnchorView()

                                ARouter.getInstance().build("/mine/login/quick").withBoolean("quick", false)
                                    .navigation()
//
//                                ActivityUtils.getTopActivity().finish()
//                                mAuthHelper.quitLoginPage()
                            }
                            findViewById(R.id.ll_wx_login).setOnClickListener {
                                if (anchorView?.isVisible() == true) {
                                    goneAnchorView()
                                    return@setOnClickListener
                                }
                                goneAnchorView()
                                if (!Constant.isCheckPrivacy) {
                                    showTipsDialog()
                                } else {
                                    loginWx(context)
                                }
                            }


                            if (!mmkv.decodeBool(
                                    MMKVKey.PRIVATE_ONE_KEY_LOGIN_PAGE,
                                    false
                                ) && !Constant.isCheckPrivacy
                            ) {
                                anchorView?.postDelayed({
                                    showTipsDialog()

                                }, 500)
                            }


                        }
                    })
                .build()
        )
        mAuthHelper.setAuthUIConfig(
            AuthUIConfig.Builder()
                .setAppPrivacyOne("《用户协议》", BuildConfig.USER_PROTOCOL)
                .setAppPrivacyTwo("《隐私政策》", BuildConfig.PRIVACY_PROTOCOL)
                .setPrivacyBefore(context.getString(R.string.login_register_indicates_that_you_have_read_agree_to_sisters))
                .setAppPrivacyColor(
                    Color.parseColor("#262626"),
                    ColorUtils.getColor(R.color.color_currency)
                )
                .setNavHidden(false)
                .setNavReturnHidden(false)
                .setNavColor(Color.WHITE)
                .setNavReturnImgPath("icon_nav_close")
                .setNavText("")
                .setLogoHidden(true)
                .setSloganHidden(true)
                .setSwitchAccHidden(true)
                .setPrivacyState(false)
                .setCheckedImgDrawable(context.getDrawable(R.drawable.icon_privacy_agreement_pressed))
                .setUncheckedImgDrawable(context.getDrawable(R.drawable.icon_privacy_agreement_normal))
                .setCheckboxHidden(false)
                .setCheckBoxHeight(24)
                .setCheckBoxWidth(24)
                .setPrivacyState(false)
                .setLogBtnToastHidden(true)
                .setLightColor(true)
                .setStatusBarColor(Color.WHITE)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_IMMERSIVE)
                .setWebNavColor(Color.WHITE)
                .setWebNavTextColor(ColorUtils.getColor(R.color.color_262626))
                .setWebNavTextSizeDp(17)
                .setLogBtnOffsetY((250 * scale).toInt())
                .setNumberLayoutGravity(Gravity.CENTER_HORIZONTAL)
                .setLogBtnLayoutGravity(Gravity.CENTER_HORIZONTAL)
                .setProtocolLayoutGravity(Gravity.CENTER_HORIZONTAL)
                .setNumFieldOffsetY((173 * scale).toInt())
                .setNumberLayoutGravity(Gravity.CENTER_HORIZONTAL)
                .setWebNavTextSizeDp(20)
                .setLogBtnMarginLeftAndRight(20)
                .setLogBtnTextSizeDp(16)
                .setLogBtnHeight(50)
                .setNumberSizeDp(32)
               // .setProtocolGravity(Gravity.TOP)
                .setLogBtnText(context.getString(R.string.key_to_machine_number_to_log_in))
                .setLogBtnBackgroundPath("button_10dp_corners_shape")
                .setNumberColor(Color.parseColor("#262626"))
                .setAuthPageActIn("activity_bottom_in", "activity_background")
                .setAuthPageActOut("activity_background", "activity_bottom_out")
//                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                //.setPrivacyOffsetY((395 * scale).toInt())
                .setScreenOrientation(authPageOrientation)
                .create()
        )

    }

    fun showTipsDialog() {
        anchorView?.let {
            if (it.isVisible()) {
                return@let
            }
            true.saveToMMKV(MMKVKey.PRIVATE_ONE_KEY_LOGIN_PAGE)
            it.alpha = 0f
            it.visible()

            val showAnimator =
                ObjectAnimator.ofFloat(anchorView, "alpha", 0f, 1f).setDuration(200)
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

            timer.schedule(object : TimerTask() {
                override fun run() {
                    it.post {
                        it.gone()
                    }
                }
            }, 4200)
        }

    }

    private var timer = Timer()

    fun goneAnchorView() {
        timer.cancel()
        timer = Timer()
        anchorView?.let {
            it.gone()
        }
    }

    fun anchorViewIsVisible(): Boolean {
        return anchorView?.isVisible() == true
    }

}