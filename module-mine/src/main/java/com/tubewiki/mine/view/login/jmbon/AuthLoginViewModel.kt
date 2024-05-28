package com.tubewiki.mine.view.login.jmbon

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.network.config.HttpCode
import com.tubewiki.mine.api.MineApi

class AuthLoginViewModel : BaseViewModel() {
    companion object {
        const val LOGIN_BY_PASSWORD = 2
        const val LOGIN_BY_CAPTCHA = 1
        const val JMB_LOGIN = 2
    }


    /**
     * 密码登录
     * 1:验证码登录，2：密码登录
     * type为1时传验证码，type为2时传密码
     */
    fun doLogin(type: Int, phone: String, password: String) = launchWithFlow({
        MineApi.JMBuserCaptchaAuth(
            type, phone, password
        )
    }, handleError = false).netCatch {
        if (this.code == HttpCode.FRAME_WORK.NETWORK_ERROR.toLong() || this.code == HttpCode.FRAME_WORK.TIMEOUT_ERROR.toLong()) {
            "登陆超时\n请检查网络连接".showToast()
        } else {
            this.message.showToast()
        }
    }
}
