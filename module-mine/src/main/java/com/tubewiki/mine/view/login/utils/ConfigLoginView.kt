package com.tubewiki.mine.view.login.utils

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import com.apkdv.mvvmfast.BuildConfig
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.tubewiki.mine.R
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig
import com.mobile.auth.gatewayauth.AuthUIConfig
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate

class ConfigLoginView(
    val context: Context,
    val mAuthHelper: PhoneNumberAuthHelper
) {
    init {
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
//                            val title = findViewById(R.id.text_mobile_title)
//                            title.setPadding(
//                                0,
//                                SizeUtils.dp2px(32f),
//                                0,
//                                0
//                            )

                            findViewById(R.id.text_use_other).setOnClickListener {
//                                ARouter.getInstance().build(RouterHub.LOGIN)
//                                    .withBoolean("quick", false).navigation()
                                ActivityUtils.getTopActivity().finish()
                                mAuthHelper.quitLoginPage()
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
                    Color.parseColor("#002E00"),
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
                .setPrivacyState(true)
                .setCheckboxHidden(false)
                .setCheckedImgDrawable(context.getDrawable(R.drawable.icon_jmtuan_privacy_agreement_pressed))
                .setUncheckedImgDrawable(context.getDrawable(R.drawable.icon_privacy_agreement_normal))
                .setLogBtnToastHidden(true)
                .setPrivacyBefore("同意LesDoB")
                .setLightColor(true)
                .setStatusBarColor(Color.WHITE)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_IMMERSIVE)
                .setWebNavColor(Color.WHITE)
                .setWebNavTextColor(ColorUtils.getColor(R.color.color_262626))
                .setWebNavTextSizeDp(17)
                .setLogBtnOffsetY(195)
                .setNumFieldOffsetY(122)
                .setNumberFieldOffsetX(20)
                .setNumberLayoutGravity(Gravity.LEFT)
                .setWebNavTextSizeDp(20)
                .setLogBtnMarginLeftAndRight(20)
                .setNumberSizeDp(24)
                .setLogBtnText(context.getString(R.string.key_to_machine_number_to_log_in))
                .setLogBtnBackgroundPath("button_8dp_corners_shape")
                .setNumberColor(Color.parseColor("#262626"))
                .setAuthPageActIn("activity_bottom_in", "activity_background")
                .setAuthPageActOut("activity_background", "activity_bottom_out")
//                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setScreenOrientation(authPageOrientation)
                .create()
        )

    }
}