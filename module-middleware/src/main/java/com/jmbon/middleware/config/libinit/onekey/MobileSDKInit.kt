package com.jmbon.middleware.config.libinit.onekey

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.config.Constant
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.PreLoginResultListener
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet

object MobileSDKInit {
    var mPhoneNumberAuthHelper: PhoneNumberAuthHelper?=null
    fun init(context: Context) {
        val mTokenResultListener = object : TokenResultListener {
            override fun onTokenSuccess(s: String) {
                try {
                    val pTokenRet = TokenRet.fromJson(s)
                    if (ResultCode.CODE_ERROR_ENV_CHECK_SUCCESS == pTokenRet.code) {
                        accelerateLoginPage(5000)
                    }
                } catch (e: Exception) {
                    LogUtils.e(e.toString())
                }
            }

            override fun onTokenFailed(s: String) {
                Constant.PHONE_NUMBER_AUTH = false
            }
        }
        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener)
        mPhoneNumberAuthHelper?.reporter?.setLoggerEnable(true)
        mPhoneNumberAuthHelper?.setAuthSDKInfo(BuildConfig.AUTH_SECRET)
        /*
        *  4.检测终端⽹络环境是否⽀持⼀键登录或者号码认证，根据回调结果确定是否可以使⽤⼀ 键登录功能
        */
        mPhoneNumberAuthHelper?.checkEnvAvailable(PhoneNumberAuthHelper.SERVICE_TYPE_LOGIN)

    }

    /**
     * 在不是一进app就需要登录的场景 建议调用此接口 加速拉起一键登录页面
     * 等到用户点击登录的时候 授权页可以秒拉
     * 预取号的成功与否不影响一键登录功能，所以不需要等待预取号的返回。
     * @param timeout
     */
    fun accelerateLoginPage(timeout: Int) {
        mPhoneNumberAuthHelper?.accelerateLoginPage(timeout, object : PreLoginResultListener {
            override fun onTokenSuccess(s: String) {
                Constant.PHONE_NUMBER_AUTH = true
            }

            override fun onTokenFailed(s: String, s1: String) {
                Constant.PHONE_NUMBER_AUTH = false
                LogUtils.i("一键登录失败=${s1}")
            }
        })
    }
}
