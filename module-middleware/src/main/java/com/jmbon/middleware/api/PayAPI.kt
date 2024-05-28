package com.jmbon.middleware.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.RewardPayData
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpV3
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.RequestParamsUtils
import com.tencent.tpns.baseapi.base.util.Md5
import rxhttp.wrapper.param.toResponse
@Keep
object PayAPI {
    /**
     * 设置悬赏
     *token
     * amount           悬赏金额
     * question_id      问题id
     * type             alipay或wechat
     */
    suspend fun offerReward(amount: Float, question_id: Int): EmptyData {
        return Http.post("app/pay/offer_reward")
            .add("amount", amount)
            .add("question_id", question_id)
            .toResponse<EmptyData>().await()
    }

    /**
     * desc:设置打赏支付
     *@param passive_uid  被打赏者id
     *@param type   article或question或answer
     */
    suspend fun offerAnswerReward(
        itemId: Int,
        passiveUid: Int,
        amount: Int,
        type: String,
        title: String,
        description: String,
        payType: String
    ): RewardPayData {
        return HttpV3.post("pay/reward")
            .add("amount", if (BuildConfig.DEBUG && amount <= 5) 0.01 else amount)
            .add("passive_uid", passiveUid)
            .add("item_id", itemId)
            .add("title", title)
            .add("type", type)
            .add("description", description)
            .add("pay_type", payType)
            .add(
                "amount_sign",
                RequestParamsUtils.MD5(DateFormatUtil.getCurrentDate(DateFormatUtil.dateFormatY) + "${if (BuildConfig.DEBUG && amount <= 5) 0.01 else amount}" + "jmbon")
            )
            .toResponse<RewardPayData>().await()
    }
}


