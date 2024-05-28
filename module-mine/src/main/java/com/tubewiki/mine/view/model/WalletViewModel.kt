package com.tubewiki.mine.view.model

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.network.config.HttpCode
import com.blankj.utilcode.util.LogUtils
import com.tubewiki.mine.api.WalletApi

class WalletViewModel : BaseViewModel() {
    val setPasswordResult = SingleLiveEvent<Boolean>()
    val verifyPasswordResult = SingleLiveEvent<String>()
    val hasWithdrawalResult = SingleLiveEvent<Boolean>()
    val payBindingResult = SingleLiveEvent<ResultTwoData<Boolean, String>>()
    val unBindingResult = SingleLiveEvent<Boolean>()


    // 并发方式 请求接口
    fun getBalance() = launchWithFlow({
        ResultTwoData(
            // 余额
            WalletApi.getBalance(viewScope()).await().balance,
            // 钱包设置
            WalletApi.withdrawalSettingInfo(viewScope()).await().data
        )
    })

    var incomePage = 1
    var rewardsPage = 1

    // 收入明细
    fun incomeList(refresh: Boolean) = launchWithFlow({
        if (refresh)
            incomePage = 1
        val data = WalletApi.incomeList(incomePage)
        val result = ResultTwoData(data.incomes, incomePage >= data.pageCount)
        incomePage++
        result
    }, handleError = true)

    // 提现列表
    fun rewardsList(refresh: Boolean) = launchWithFlow(
        {
            if (refresh)
                rewardsPage = 1
            val data = WalletApi.rewardsList(rewardsPage)
            val result = ResultTwoData(data.withdrawals, rewardsPage >= data.pageCount)
            rewardsPage++
            result
        }, handleError = true
    )

    fun incomeDetails(id: Int) = launchWithFlow({
        WalletApi.incomeDetails(id).income
    })

    fun withdrawalDetails(id: Int) = launchWithFlow({
        WalletApi.withdrawalDetails(id).withdrawal
    })


   fun withdrawalSettingInfo() = launchWithFlow({
       // 钱包设置
       WalletApi.withdrawalSettingInfo().data
    })


    /**
     *  设置提现密码
     */
    fun setWithdrawalPassword(passWord: String) {
        launchOnlyResult({
            WalletApi.setWithdrawalPassword(passWord)
        }, {
            setPasswordResult.postValue(true)
        }, {
            it.message.showToast()
            setPasswordResult.postValue(false)
        }, handleError = false, isShowDialog = false)
    }

    /**
     *  设置提现密码
     */
    fun verifyWithdrawalPassword(passWord: String) {
        launchOnlyResult({
            WalletApi.verifyWithdrawalPassword(passWord)
        }, {
            verifyPasswordResult.postValue("")
        }, {
            if (it.code.toInt() == HttpCode.FRAME_WORK.NETWORK_ERROR) {
                it.message.showToast()
            } else {
                verifyPasswordResult.postValue(it.message)
            }
        }, handleError = true, isShowDialog = false)
    }

    /**
     * 获取客服电话
     */
    fun getOfficialPhone(result: (phone: String) -> Unit) {
        launchOnlyResult({
            WalletApi.getCustomerMobile()
        }, {
            result(it.mobile)
        }, {
            it.message.showToast()
        }, isShowDialog = false)
    }


    /**
     *  是否有提现任务
     */
    fun hasWithdrawal(payType:String) {
        launchOnlyResult({
            WalletApi.hasWithdrawal(payType)
        }, {
            hasWithdrawalResult.postValue(it.hasWithdrawal)
        }, {
            it.message.showToast()
            hasWithdrawalResult.postValue(false)
        }, handleError = true, isShowDialog = false)
    }

    /**
     *  绑定支付宝微信
     */
    fun payBinding(name: String, openId: String, type: String) {
        launchOnlyResult({
            WalletApi.payBinding(
                name,
                openId,
                type
            )
        }, {
            payBindingResult.postValue(ResultTwoData(true, name))
        }, {
            it.message.showToast()
            payBindingResult.postValue(ResultTwoData(false, ""))
        }, handleError = true, isShowDialog = false)
    }

    /**
     *  解绑支付宝微信
     */
    fun untieBinding(password: String, type: String) {
        launchOnlyResult({
            WalletApi.untieBinding(password, type)
        }, {
            unBindingResult.postValue(true)
        }, {
            it.message.showToast()
            unBindingResult.postValue(false)
        }, handleError = true, isShowDialog = false)
    }

    /**
     *  提现
     */
    fun withdrawal(amount: String, password: String, type: String) = launchWithFlow(
        { WalletApi.withdrawal(amount, password, type) }, {
            LogUtils.e("测试ViewModel错误回调，$it")
        }, isShowDialog = false
    )
}