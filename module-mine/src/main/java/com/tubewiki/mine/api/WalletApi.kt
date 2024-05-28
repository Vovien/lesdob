package com.tubewiki.mine.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.HasWithdrawalData
import com.jmbon.middleware.bean.WalletSettingData
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.HttpV2
import com.tubewiki.mine.bean.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import rxhttp.async
import rxhttp.wrapper.param.toResponse
@Keep
object WalletApi {

    /**
     * APP获取用户余额
     */
    suspend fun getBalance(scope: CoroutineScope): Deferred<Balance> {
        return HttpV2.post("wallet/balance")
            .toResponse<Balance>().async(scope)
    }

    /**
     * APP收入详情
     */
    suspend fun incomeDetails(id: Int): IncomeDetails {
        return HttpV2.post("wallet/income_detail")
            .add("id", id)
            .toResponse<IncomeDetails>().await()
    }

    /**
     * APP提现详情
     */
    suspend fun withdrawalDetails(id: Int): WithdrawalDetails {
        return HttpV2.post("wallet/withdrawal_detail")
            .add("id", id)
            .toResponse<WithdrawalDetails>().await()
    }

    /**
     * 用户收入明细
     */
    suspend fun incomeList(page: Int, pageSize: Int = Constant.PAGE_SIZE): IncomeList {
        return HttpV2.post("wallet/income")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<IncomeList>().await()
    }

    /**
     * 获取悬赏列表
     * /api/v1/wallet/rewards
     */
    suspend fun rewardsList(page: Int, pageSize: Int = Constant.PAGE_SIZE): WithdrawalList {
        return HttpV2.post("wallet/withdrawal_list")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<WithdrawalList>().await()
    }

    /**
     * APP获取提现设置相关信息
     */
    suspend fun withdrawalSettingInfo(scope: CoroutineScope): Deferred<WalletSettingData> {
        return HttpV2.post("wallet/withdrawal_setting_info")
            .toResponse<WalletSettingData>().async(scope)
    }

    /**
     * APP获取提现设置相关信息
     */
    suspend fun withdrawalSettingInfo():WalletSettingData {
        return HttpV2.post("wallet/withdrawal_setting_info")
            .toResponse<WalletSettingData>().await()
    }


    /**
     * 设置提现密码
     */
    suspend fun setWithdrawalPassword(passWord: String): EmptyData {
        return HttpV2.post("wallet/set_withdrawal_password")
            .add("password", passWord)
            .toResponse<EmptyData>().await()
    }

    /**
     * 验证原密码
     */
    suspend fun verifyWithdrawalPassword(passWord: String): EmptyData {
        return HttpV2.post("wallet/verify_withdrawal_password")
            .add("password", passWord)
            .toResponse<EmptyData>().await()
    }


    /**
     * 获取客服联系电话
     */
    suspend fun getCustomerMobile(): PhoneData {
        return HttpV2.post("wallet/get_customer_mobile")
            .toResponse<PhoneData>().await()
    }

    /**
     * 解除支付宝微信绑定
     * @param type alipay或wechat两种
     */
    suspend fun untieBinding(password: String, type: String): EmptyData {
        return HttpV2.post("wallet/untie_binding")
            .add("password", password)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * 绑定支付宝微信
     * @param type alipay或wechat两种
     * @param openid 微信openid或支付宝alipay_user_id
     */
    suspend fun payBinding(name:String,openid: String, type: String): EmptyData {
        return HttpV2.post("wallet/binding")
            .add("name", name)
            .add("openid", openid)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * 是否有提现任务
     * type: alpay wehcat
     */
    suspend fun hasWithdrawal(payType:String): HasWithdrawalData {
        return HttpV2.post("wallet/has_withdrawal")
            .add("type", payType)
            .toResponse<HasWithdrawalData>().await()
    }

    /**
     * APP提现申请
     * /api/v1/wallet/withdrawal
     */
    suspend fun withdrawal(amount: String, password: String, type: String): EmptyData {
        return HttpV2.post("wallet/withdrawal")
            .add("amount", amount)
            .add("password", password)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

}