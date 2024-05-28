package com.apkdv.mvvmfast.ktx

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.event.UserActionEvent
import com.apkdv.mvvmfast.network.config.HttpCode
import com.apkdv.mvvmfast.network.exception.ApiException
import com.blankj.utilcode.util.LogUtils
import org.greenrobot.eventbus.EventBus

/***
 * 处理onError中错误
 * @param ex ApiException
 */
fun ApiException?.handleError(defLayout: BaseViewModel.LayoutChange? = null) {
    if (this == null) {
        return
    }
    when (this.code) {
        HttpCode.HTTP.INNER_EXCEPTION.toLong(),
        HttpCode.HTTP.SERVICE_UNUSEFUL.toLong() -> {
            "出了点问题".showToast()
            defLayout?.showError?.call()
        }
        HttpCode.FRAME_WORK.NETWORK_ERROR.toLong(),
        HttpCode.FRAME_WORK.TIMEOUT_ERROR.toLong() -> defLayout?.showNoNet?.call()
        HttpCode.HTTP.UNAUTHORIZED.toLong() -> {
            message ?: "登录信息异常，请重新登录".showToast()
            EventBus.getDefault().post(UserActionEvent(UserActionEvent.CLEAN_LOGIN_USER))
        }
        HttpCode.FRAME_WORK.REQUEST_CANCEL.toLong() -> {
            LogUtils.e("请求取消")
        }
        else -> {
            LogUtils.e(this.message)
            defLayout?.showError?.call()
        }

    }
}