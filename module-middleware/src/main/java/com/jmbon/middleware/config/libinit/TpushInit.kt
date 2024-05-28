package com.jmbon.middleware.config.libinit

import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.api.API
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.push.ThirdPushTokenMgr
import com.jmbon.middleware.utils.getRealChannel
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

object TpushInit {
    fun init() {
        // 注意这里填入的是 Oppo 的 AppKey，不是AppId
        XGPushConfig.setOppoPushAppId(Utils.getApp(), "8c28e849abab417f8f331f2eabf8f263")
        // 注意这里填入的是 Oppo 的 AppSecret，不是 AppKey
        XGPushConfig.setOppoPushAppKey(Utils.getApp(), "630d26a8644e4d9c8f04a6be89a86bd6")


        //小米通道
        XGPushConfig.setMiPushAppId(Utils.getApp(), "2882303761519905863")
        XGPushConfig.setMiPushAppKey(Utils.getApp(), "5251990591863")
        //魅族通道
        XGPushConfig.setMzPushAppId(Utils.getApp(), "141080")
        XGPushConfig.setMzPushAppKey(Utils.getApp(), "26a358a2164c4b12ab6e440ab70baede")

        //打开第三方推送
        XGPushConfig.enableOtherPush(Utils.getApp(), true)

        XGPushManager.registerPush(Utils.getApp(), object : XGIOperateCallback {
            override fun onSuccess(data: Any, flag: Int) {
                //token在设备卸载重装的时候有可能会变
                LogUtils.d("TPush", "注册成功，设备token为：$data")
                val key = data.toString()
                Constant.DEVICE_ID = key
                //本地保存device key,防止有可能获取不到的情况

                if (!key.isEmpty()) {
                    key.saveToMMKV(MMKVKey.DEVICE_ID_KEY)
                }
                ThirdPushTokenMgr.getInstance().thirdPushToken = key


                if (!Constant.isActiveChannel) {
                    appActive(
                        getRealChannel()
                    )
                }


                registerDeviceId(key)

            }

            override fun onFail(data: Any, errCode: Int, msg: String) {
                LogUtils.d("TPush", "注册失败，错误码：$errCode,错误信息：$msg")
            }
        })
    }


    private fun registerDeviceId(deviceId: String) {
        MainScope().launch {
            flow<EmptyData> {
                emit(API.bindDevice(deviceId))
            }.netCatch {
                LogUtils.e(this)
            }.next {
                LogUtils.e("设备绑定成功")
            }
        }
    }

    private fun appActive(channel: String) {
        MainScope().launch {
            flow<EmptyData> {
                emit(API.appActive(channel))
            }.netCatch {
                LogUtils.e(this)
            }.next {
                Constant.isActiveChannel = true
                LogUtils.e("渠道激活绑定成功")
            }
        }
    }

}