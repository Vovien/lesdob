package com.jmbon.middleware.model

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.api.ShareApi

class ShareToCircleViewModel : BaseViewModel() {

    fun getCircleShareList(circleId: String, page: Int) = launchWithFlow({
        ShareApi.getCircleShareList(circleId, page)
    }, {
        it.message.showToast()
    }, handleError = true)


    fun circleShare(
        circle_id: Int,
        type: Int,
        item_id: Int,
        answer_id: Int = 0,
    ) = launchWithFlow({
        ShareApi.circleShare(circle_id, type, item_id, answer_id)
    }, {
        it.message.showToast()
    }, handleError = false)
}