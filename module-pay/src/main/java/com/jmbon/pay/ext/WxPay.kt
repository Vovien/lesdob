package com.jmbon.pay.ext

import android.content.Context
import androidx.annotation.Keep
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.pay.ext.WxPay.createWx
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.json.JSONObject

const val WX_APP_ID: String = "wx3dca91f34e5559d9"
const val WX_APP_SECRET: String = "4b9671170e0889362955c51d0676d704"

object WxPay {
    private var wxAPI: IWXAPI? = null
    fun createWx(context: Context): IWXAPI? {
        if (wxAPI != null) {
            return wxAPI!!
        }
        val createWXAPI = WXAPIFactory.createWXAPI(context, WX_APP_ID, false)
        return if (createWXAPI.isWXAppInstalled) {
            wxAPI = createWXAPI
            wxAPI?.registerApp(WX_APP_ID)
            wxAPI
        } else {
            "请您先安装微信".showToast()
            null
        }
    }
}

fun payWx(context: Context, wxbody: Wxbody) {
    val request = PayReq()
    request.appId = wxbody.appId
    request.partnerId = wxbody.partnerId
    request.prepayId = wxbody.prepayId
    request.packageValue = wxbody.packageValue
    request.nonceStr = wxbody.nonceStr
    request.timeStamp = wxbody.timeStamp
    request.sign = wxbody.sign
    createWx(context)?.sendReq(request)
}

fun payWx(context: Context, jsonStr: String) {
    val request = PayReq()

    val json = JSONObject(jsonStr)
    request.appId = json.getString("appid")
    request.partnerId = json.getString("partnerid")
    request.prepayId = json.getString("prepayid")
    request.packageValue = json.getString("package")
    request.nonceStr = json.getString("noncestr")
    request.timeStamp = (json.getLong("timestamp")).toString()
    request.sign = json.getString("sign")
    createWx(context)?.sendReq(request)
}

@Keep
data class Wxbody(
    val appId: String,
    val nonceStr: String,
    val packageValue: String,
    val partnerId: String,
    val prepayId: String,
    val sign: String,
    val timeStamp: String
)
