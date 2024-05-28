package com.jmbon.middleware.push;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class HUAWEIHmsMessageService extends HmsMessageService {

    private static final String TAG = HUAWEIHmsMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        LogUtils.i(TAG, "onMessageReceived message=" + message);
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
        LogUtils.i(TAG, "onMessageSent msgId=" + msgId);
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        super.onSendError(msgId, exception);
        LogUtils.i(TAG, "onSendError msgId=" + msgId);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        LogUtils.i(TAG, "onNewToken token=" + token);
        ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    @Override
    public void onTokenError(Exception exception) {
        onTokenError(exception);
        LogUtils.i(TAG, "onTokenError exception=" + exception);
    }

    @Override
    public void onMessageDelivered(String msgId, Exception exception) {
        onMessageDelivered(msgId, exception);
        LogUtils.i(TAG, "onMessageDelivered msgId=" + msgId);
    }


    public static void updateBadge(final Context context, final int number) {
        if (!BrandUtil.isBrandHuawei()) {
            return;
        }
        LogUtils.i(TAG, "huawei badge = " + number);
        try {
            Bundle extra = new Bundle();
            extra.putString("package", "com.tencent.qcloud.tim.tuikit");
            extra.putString("class", "com.tencent.qcloud.tim.demo.SplashActivity");
            extra.putInt("badgenumber", number);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, extra);
        } catch (Exception e) {
            LogUtils.w(TAG, "huawei badge exception: " + e.getLocalizedMessage());
        }
    }
}
