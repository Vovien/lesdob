package com.jmbon.middleware.push;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.tencent.android.mzpush.MZPushMessageReceiver;

public class MEIZUPushReceiver extends MZPushMessageReceiver {

    private static final String TAG = MEIZUPushReceiver.class.getSimpleName();

    @Override
    public void onMessage(Context context, String s) {
        super.onMessage(context,s);
        LogUtils.i(TAG, "onMessage method1 msg = " + s);
    }

    @Override
    public void onMessage(Context context, String message, String platformExtra) {
        super.onMessage(context,message,platformExtra);
        LogUtils.i(TAG, "onMessage method2 msg = " + message + ", platformExtra = " + platformExtra);
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        super.onMessage(context,intent);
        String content = intent.getExtras().toString();
        LogUtils.i(TAG, "flyme3 onMessage = " + content);
    }

    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        super.onUpdateNotificationBuilder(pushNotificationBuilder);
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        onNotificationClicked(context,mzPushMessage);
        LogUtils.i(TAG, "onNotificationClicked mzPushMessage " + mzPushMessage.toString());
    }

    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationArrived(context, mzPushMessage);
    }

    @Override
    public void onNotificationDeleted(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationDeleted(context, mzPushMessage);
    }

    @Override
    public void onNotifyMessageArrived(Context context, String s) {
        super.onNotifyMessageArrived(context, s);
    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
      //  super.onPushStatus(context,pushSwitchStatus);
    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        LogUtils.i(TAG, "onRegisterStatus token = " + registerStatus.getPushId());
        ThirdPushTokenMgr.getInstance().setThirdPushToken(registerStatus.getPushId());
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();

    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
     //   super.onUnRegisterStatus(context,unRegisterStatus);
    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {
     //   super.onSubTagsStatus(context,subTagsStatus);
    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {
     //   super.onSubAliasStatus(context,subAliasStatus);

    }

    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context,s);

    }

    @Override
    public void onUnRegister(Context context, boolean b) {
        super.onUnRegister(context,b);

    }
}
