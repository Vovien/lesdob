package com.tubewiki.android.wxapi

import android.content.Intent

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.pay.ext.WX_APP_ID
import com.jmbon.pay.ext.WxPay.createWx
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import org.greenrobot.eventbus.EventBus


class WXPayEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    private var wxAPI: IWXAPI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxAPI = createWx(this)
        wxAPI?.registerApp(WX_APP_ID)
        wxAPI?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        wxAPI?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        finish()
    }

    override fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    //"支付成功".showToast()
                    EventBus.getDefault().post(resp)
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> "微信支付取消".showToast()
                else -> "微信支付失败:${resp.errStr}".showToast()
            }

        }
        overridePendingTransition(0, 0)
        finish()
    }
    override fun finish() {
        overridePendingTransition(-1, -1)
        super.finish()
        overridePendingTransition(0, 0)
    }
}