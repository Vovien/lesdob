package com.yxbabe.lesdob.wxapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.sharesdk.wechat.utils.WechatHandlerActivity
import com.blankj.utilcode.util.LogUtils
import com.jmbon.pay.bean.WxLoginEvent
import com.jmbon.pay.ext.WxPay.createWx
import com.jmbon.pay.ext.loginWxRequest
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import org.greenrobot.eventbus.EventBus

class WXEntryActivity : WechatHandlerActivity(), IWXAPIEventHandler {
    private var wxAPI: IWXAPI? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxAPI = createWx(this)
        wxAPI?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        wxAPI?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
       // finish()
        Log.w("wxentry onReq", req.type.toString() + "---")
    }

    override fun onResp(resp: BaseResp) {
        Log.w(
            "wxentry", "resp.errCode:" + resp.errCode.toString() + ",resp.errStr:"
                    + resp.errStr
        )
        when (resp.type) {
            ConstantsAPI.COMMAND_SENDAUTH -> {
                when (resp.errCode) {
                    BaseResp.ErrCode.ERR_OK -> LogUtils.e("token获取成功")
                    else -> {
                        LogUtils.e("token获取 ${resp.errStr}")
                        finish()
                    }
                }
                val authResp = resp as SendAuth.Resp
                loginWxRequest(authResp.code) {
                    EventBus.getDefault().post(WxLoginEvent(it))
                    finish()
                }
            }
            else -> {
                finish()
            }
        }

    }
    override fun finish() {
        overridePendingTransition(-1, -1)
        super.finish()
        overridePendingTransition(0, 0)
    }
}