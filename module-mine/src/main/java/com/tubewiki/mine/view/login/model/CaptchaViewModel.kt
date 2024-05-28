package com.tubewiki.mine.view.login.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.network.config.HttpCode
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.UserData
import com.tubewiki.mine.R
import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel
import com.tubewiki.mine.bean.VerificationCodeToken

class CaptchaViewModel : MineViewModel() {
    companion object {
        const val REGISTER = 1
        const val LOGIN = 2
        const val BIND = 10

        // 找回密码
        const val RETRIEVE_PASSWORD = 3

        // 设置密码
        const val SET_NEW_PASSWORD = 4
        const val UPDATE_PASSWORD = 8

        // 设置邮箱
        const val SET_NEW_EMAIL = 5
        const val UPDATE_EMAIL = 7

        // 设置手机
        const val UPDATE_PHONE = 6

        // 注销账号
        const val LOG_OFF_ACCOUNT = 9
    }

    val captchaStatus = MutableLiveData<Boolean>()
    val verifyCaptcha = MutableLiveData<String>()
    val captchaVerify = MutableLiveData<UserData>()
    val logOffCaptchaVerify = MutableLiveData<String>()

    /**
     * 发送验证码
     * 除了找回密码和注销类型是3，其他都是2，因为没有注册逻辑
     *1 注册，2：登录,3:找回密码 ，3注销验证码 (注销还是3)
     */
    fun sendCaptcha(phone: String, type: Int) {
        launchOnlyResult({
            MineApi.sendVerificationCode(phone, type)
        }, {
            captchaStatus.postValue(true)
            R.string.verification_code_sent_successfully.showToast()
        }, {
            Log.e("TAG", "===异常")
            it.printStackTrace()
            Log.e("TAG", "===异常${it.code},${it.stackTrace.toString()}")

            captchaStatus.postValue(false)
            it.message.showToast()
        }, isShowDialog = false)
    }

    /**
     *类型：1:注册; 2:登录; 3:找回密码|修改手机号；默认3
     */
    fun verify(type: Int, code: String, phone: String) {
        launchOnlyResult({
            MineApi.userCaptchaAuth(
                type, phone, code
            )
        }, {
            captchaVerify.postValue(it)
        }, {
            if (it.code == HttpCode.FRAME_WORK.NETWORK_ERROR.toLong() || it.code == HttpCode.FRAME_WORK.TIMEOUT_ERROR.toLong()) {
                "登陆超时\n请检查网络连接".showToast()
            } else {
                it.message.showToast()
            }
            captchaVerify.postValue(null)
        }, isShowDialog = false, handleError = false)
    }

    /**
     * 验证手机验证码
     */
    fun verifyCaptcha(code: String, phone: String) {
        launchOnlyResult({
            MineApi.mobileCodeCheck(
                phone, code
            )
        }, {
            verifyCaptcha.postValue(code)
        }, {
            it.message.showToast()
        }, isShowDialog = false, handleError = true)
    }


    /**
     * 验证除登录以外的验证码
     */
    fun checkAccount(code: String) {
        launchOnlyResult({
            MineApi.checkAccount(code)
        }, {
            logOffCaptchaVerify.postValue(it.cancelSign)
        }, {
            it.message.showToast()
            logOffCaptchaVerify.postValue(null)
        }, isShowDialog = false, handleError = true)
    }

    fun setNewMobile(mobile: String, token: String) = launchWithFlow({
        MineApi.setMobile(token, mobile)
    }).netCatch { message.showToast() }

    /**
     * 微信绑定手机号并且登录
     */
    fun wxBindMobileLogin(wxSign: String, mobile: String, code: String) = launchWithFlow({
        MineApi.wxBindMobileLogin(wxSign, mobile, code)
    })

    /**
     * 手机号验证码登录
     */
    fun authLogin(
        mobile: String,
        password: String,
    ) = launchWithFlow({
        MineApi.auth(mobile, "1", password, "", "", "")
    })

    /**
     * 登录
     */
    fun wxBindMobileLogin(
        mobile: String,
        password: String,
        openid: String,
        username: String,
        avatar: String
    ) = launchWithFlow({
        MineApi.wxBind(mobile, password, openid, username, avatar)
    })

    /**
     * LesDoB注销账号
     */
    fun userAccountCancel(mobile: String, code: String) = launchWithFlow({
        MineApi.userAccountCancel(mobile, code)
    }, handleError = false)


}