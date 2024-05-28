package com.tubewiki.mine.view.login.model

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.jmbon.middleware.arouter.LoginCallBack
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.libinit.TpushInit
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel
import org.greenrobot.eventbus.EventBus

class LoginViewModel : MineViewModel() {

    val textSize = MutableLiveData<Int>()


    val textWatch by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                textSize.postValue(s?.length)
            }

        }
    }

    fun checkTaskAndRun(activity: Activity) {
        while (!LoginCallBack.queue.isNotNullEmpty()) {
            LoginCallBack.queue.pop().run()
        }
        EventBus.getDefault().post(UserLoginEvent(true))
        activity.finish()

    }

    /**
     * 密码登录
     * 1:验证码登录，2：密码登录
     * type为1时传验证码，type为2时传密码
     */
    fun verify(password: String, phone: String) = launchWithFlow({
        MineApi.userCaptchaAuth(
            2, phone, password
        )
    }, handleError = false)

    fun bindDevice() {
//        TpushInit.init()
    }

    /**
     * status 设置状态【2：设置为已扫码，3：设置为取消登陆，4：设置为已登陆】
     */
    fun qrcodeLogin(qrcodeToken: String, status: Int) = launchWithFlow({
        MineApi.qrcodeLogin(qrcodeToken, status)
    })

    /**
     * 注销登录
     */
    fun cancelAccount(cancel_sign: String, is_force: Int) = launchWithFlow({
        MineApi.cancelAccount(
            cancel_sign, is_force, Constant.getDeviceId()
        )
    }, handleError = false)

}