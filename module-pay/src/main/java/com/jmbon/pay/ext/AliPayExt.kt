package com.jmbon.pay.ext

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
//import com.alipay.sdk.app.PayTask
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.TimeUtils
import com.jmbon.middleware.config.network.Http
import com.jmbon.pay.bean.AliUserAuth
import com.jmbon.pay.bean.AliUserInfo
import com.jmbon.pay.bean.AuthResult
import com.jmbon.pay.utils.AuthUtils.RSA2_PRIVATE
import com.jmbon.pay.utils.OrderInfoUtil2_0
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import rxhttp.toClass
import rxhttp.wrapper.param.toResponse

const val ALI_PAY_SUCCESSFUL = "9000"
const val ALI_PAY_BEING = "8000"
const val ALI_PAY_FAILED = "4000"
const val ALI_PAY_REPEAT_REQUESTS = "5000"
const val ALI_PAY_USER_CANCELLATION = "6001"
const val ALI_PAY_NETWORK_ERROR = "6002"
const val ALI_PAY_UNKNOWN = "6004"

const val ALIPAY_ID = "2021001185618757"

@Keep
data class PayResult(
    var resultStatus: String? = null,
    var result: String? = null,
    var memo: String? = null
)

@SuppressLint("CheckResult")
fun AppCompatActivity.payAli(
    orderInfo: String,
    result: (result: PayResult) -> Unit
) = flow {
    emit(orderInfo)
}.map {
    return@map HashMap<String,String>()
   // return@map PayTask(this).payV2(orderInfo, true)
}.map { rawResult ->
    val payResult = PayResult()
    for (key in rawResult.keys) {
        when {
            TextUtils.equals(key, "resultStatus") -> {
                payResult.resultStatus = rawResult[key]
            }
            TextUtils.equals(key, "result") -> {
                payResult.result = rawResult[key]
            }
            TextUtils.equals(key, "memo") -> {
                payResult.memo = rawResult[key]
            }
        }
    }
    payResult
}.flowOn(Dispatchers.IO)
    .catch {
        "支付遇到问题".showToast()
    }.onEach {
//        when (it.resultStatus) {
//            // ALI_PAY_SUCCESSFUL -> "支付成功"
//            ALI_PAY_BEING -> "正在处理中"
//            // ALI_PAY_FAILED -> "支付失败"
//            ALI_PAY_REPEAT_REQUESTS -> "重复请求"
//            ALI_PAY_USER_CANCELLATION -> "支付取消"
//            ALI_PAY_NETWORK_ERROR -> "网络连接出错"
//            ALI_PAY_UNKNOWN -> "支付结果未知"
//             else -> "其它支付错误"
//        }.showToast()

        result(it)
    }.launchIn(lifecycleScope)


fun AppCompatActivity.getUserInfo(
    authResult: AuthResult,
    result: (ResultTwoData<AliUserInfo, String>) -> Unit
) = flow<AuthResult> {
    emit(authResult)
}.map {
    val values = HashMap<String, String>()

    values["app_id"] = authResult.appId
    values["method"] = "alipay.system.oauth.token"
    values["charset"] = "utf-8"
    values["sign_type"] = "RSA2"
    values["timestamp"] = TimeUtils.getNowString()
    values["grant_type"] = "authorization_code"
    values["version"] = "1.0"
    values["code"] = authResult.authCode
    val value = OrderInfoUtil2_0.buildOrderParam(values)
    val sign = OrderInfoUtil2_0.getSign(values, RSA2_PRIVATE, true)

    Http.get("https://openapi.alipay.com/gateway.do?$value&$sign").toClass<AliUserAuth>()
        .await()

}.map {

    val values = HashMap<String, String>()
    values["app_id"] = authResult.appId
    values["method"] = "alipay.user.userinfo.share"
    values["charset"] = "utf-8"
    values["sign_type"] = "RSA2"
    values["timestamp"] = TimeUtils.getNowString()
    values["version"] = "1.0"
    values["auth_token"] = it.alipaySystemOauthTokenResponse.accessToken

    val value = OrderInfoUtil2_0.buildOrderParam(values)
    val sign = OrderInfoUtil2_0.getSign(values, RSA2_PRIVATE, true)

    Http.get("https://openapi.alipay.com/gateway.do?$value&$sign").toResponse<AliUserInfo>()
        .await()

}.flowOn(
    Dispatchers.IO
).catch {
    "获取支付宝用户信息失败".showToast()
    result(ResultTwoData(AliUserInfo(), it.message))
}.onEach {
    result(ResultTwoData(it, ""))
}.launchIn(lifecycleScope)

fun AppCompatActivity.getUserInfo2(
    authResult: AuthResult,
    result: (ResultTwoData<AliUserInfo, String>) -> Unit
) = flow<AuthResult> {
    emit(authResult)
}.map {
    Http.post("app/v2/pay/get_alipay_user_info")
        .add("code", authResult.authCode)
        .toResponse<AliUserInfo>().await()
}.flowOn(
    Dispatchers.IO
).catch {
    "获取支付宝用户信息失败".showToast()
    result(ResultTwoData(AliUserInfo(), it.message))
}.onEach {
    result(ResultTwoData(it, ""))
}.launchIn(lifecycleScope)