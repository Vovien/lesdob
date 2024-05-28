package com.jmbon.middleware.push;

import android.content.Context;
import android.text.TextUtils;

import com.apkdv.mvvmfast.utils.MmkvSpKt;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.heytap.msp.push.HeytapPushManager;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.jmbon.middleware.config.MMKVKey;

import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

/**
 * 用来保存厂商注册离线推送token的管理类示例，当登录im后，通过 setOfflinePushToken 上报证书 ID 及设备 token 给im后台。开发者可以根据自己的需求灵活实现
 */

public class ThirdPushTokenMgr {

    public static final boolean USER_GOOGLE_FCM = false;
    private static final String TAG = ThirdPushTokenMgr.class.getSimpleName();
    private String mThirdPushToken;

    public static ThirdPushTokenMgr getInstance() {
        return ThirdPushTokenHolder.instance;
    }

    public String getThirdPushToken() {
        String tokenOld = MmkvSpKt.getString(MMKVKey.INSTANCE.getDEVICE_ID_KEY());
        if (TextUtils.isEmpty(mThirdPushToken) && !TextUtils.isEmpty(tokenOld)) {
            mThirdPushToken = tokenOld;
        }
        return mThirdPushToken;
    }

    public void setThirdPushToken(String mThirdPushToken) {
        this.mThirdPushToken = mThirdPushToken;
    }

    public void setPushTokenToTIM() {
        String token = ThirdPushTokenMgr.getInstance().getThirdPushToken();
        if (TextUtils.isEmpty(token)) {
            LogUtils.i(TAG, "setPushTokenToTIM third token is empty");
            return;
        }

    }

    private static class ThirdPushTokenHolder {
        private static final ThirdPushTokenMgr instance = new ThirdPushTokenMgr();
    }


    public void prepareThirdPushToken(Context context) {
        if (context == null) {
            return;
        }
        HeytapPushManager.init(Utils.getApp(), true);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();


        if (BrandUtil.isBrandHuawei()) {
            // 华为离线推送
            new Thread() {
                @Override
                public void run() {
                    try {
                        // read from agconnect-services.json
                        String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
                        String token = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
                        LogUtils.i(TAG, "huawei get token:" + token);
                        if (!TextUtils.isEmpty(token)) {
                            ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                            ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                        }
                    } catch (ApiException e) {
                        LogUtils.e(TAG, "huawei get token failed, " + e);
                    }
                }
            }.start();
        } else if (BrandUtil.isBrandVivo()) {
            // vivo离线推送
            LogUtils.i(TAG, "vivo support push: " + PushClient.getInstance(context).isSupport());
            PushClient.getInstance(context).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    if (state == 0) {
                        String regId = PushClient.getInstance(context).getRegId();
                        LogUtils.i(TAG, "vivopush open vivo push success regId = " + regId);
                        ThirdPushTokenMgr.getInstance().setThirdPushToken(regId);
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                    } else {
                        String regId = PushClient.getInstance(context).getRegId();
                        LogUtils.i(TAG, "vivopush regId = " + state);
                        //10045 应用审核中不可发送正式消息
                        // 根据vivo推送文档说明，state = 101 表示该vivo机型或者版本不支持vivo推送，链接：https://dev.vivo.com.cn/documentCenter/doc/156
                        LogUtils.i(TAG, "vivopush open vivo push fail state = " + state);
                    }
                }
            });
        } else if (BrandUtil.isBrandOppo() && HeytapPushManager.isSupportPush(context)) {
            // oppo离线推送
            OPPOPushImpl oppo = new OPPOPushImpl();
            oppo.createNotificationChannel(context);
            HeytapPushManager.register(context, PrivateConstants.OPPO_PUSH_APPKEY, PrivateConstants.OPPO_PUSH_APPSECRET, oppo);
        }
    }

}
