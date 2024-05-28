package com.jmbon.middleware.arouter

import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.arouter.RouterHub.LOGIN
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.middleware.valid.action.Valid

class LoginValid : Valid {
    override fun check(): Boolean {
        return Constant.isLogin
    }

    override fun doValid() {
        ARouter.getInstance().build(LOGIN).withTransition(
            com.jmbon.middleware.R.anim.fade_in,
            com.jmbon.middleware.R.anim.fade_out).navigation()
        MobAnalysisUtils.mInstance.sendEvent("Event_Components_Login_Calculate_Trigger")
    }
}