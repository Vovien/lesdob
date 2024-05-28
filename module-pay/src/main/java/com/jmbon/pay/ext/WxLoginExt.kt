package com.jmbon.pay.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.config.network.Http
import com.jmbon.pay.ext.WxPay.createWx
import com.tencent.mm.opensdk.modelmsg.SendAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import rxhttp.toClass
import rxhttp.wrapper.param.toResponse

private const val SCOPE = "snsapi_userinfo"
fun loginWx(context: Context) {
    val req = SendAuth.Req()
    req.scope = SCOPE
    req.state = "jmbon_snsapi_userinfo"
    createWx(context)?.sendReq(req)
}

@SuppressLint("CheckResult")
fun Activity.loginWxRequest(
    code: String,
    result: (ResultTwoData<WxInfo, Throwable>) -> Unit
) {
    flow {
        emit(code)
    }.map {
        return@map String.format(
            "https://api.weixin.qq.com/sns/oauth2/access_token?"
                    + "appid=%s&secret=%s&code=%s&grant_type=authorization_code",
            WX_APP_ID, WX_APP_SECRET, it
        )
    }.map {
        // 获取微信的 授权
        Http.get(it).toClass<WxResult>().await()
    }.map { wxResult ->
        val url = String.format(
            "https://api.weixin.qq.com/sns/userinfo?" + "access_token=%s&openid=%s",
            wxResult.access_token, wxResult.openid
        )
        Http.get(url).toClass<WxInfo>().await()
    }.flowOn(Dispatchers.IO)
        .catch {
            "获取微信授权出错".showToast()
            result(ResultTwoData(null, WxThrowable(it.message ?: "")))
        }.onEach {
            result(ResultTwoData(it, null))
        }.launchIn(MainScope())


}

@Keep
data class WxResult(
    val access_token: String,
    val expires_in: Long,
    val refresh_token: String,
    val openid: String,
    val scope: String,
    val unionid: String,
    val errcode: Int,
    val errmsg: String,
    var wxInfo: WxInfo?
)

@Keep
@Parcelize
data class WxInfo(
    val city: String = "",
    val country: String = "",
    val headimgurl: String = "",
    val language: String = "",
    val nickname: String = "",
    val openid: String = "",
    val privilege: List<String>,
    val province: String = "",
    val sex: Int = 0,
    val unionid: String = ""
) : Parcelable

class WxThrowable(error: String) : Throwable(error)