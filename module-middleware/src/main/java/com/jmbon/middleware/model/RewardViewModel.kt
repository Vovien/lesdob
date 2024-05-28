package com.jmbon.middleware.model

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.api.PayAPI
import com.jmbon.middleware.api.ShareApi

class RewardViewModel : BaseViewModel() {


    /**
     * 文章回答设置打赏
     */
    fun offerAnswerReward2(
        itemId: Int,
        passiveUid: Int,
        amount: Int,
        type: String,
        title: String,
        description: String,
        payType: String
    ) = launchWithFlow({
        ResultTwoData(
            payType, PayAPI.offerAnswerReward(
                itemId,
                passiveUid,
                amount,
                type,
                title,
                description,
                payType
            )
        )

    }, handleError = false)

}