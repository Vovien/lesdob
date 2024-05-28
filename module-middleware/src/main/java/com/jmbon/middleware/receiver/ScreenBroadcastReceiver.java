package com.jmbon.middleware.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.jmbon.middleware.bean.event.ScreenActionBean;

import org.greenrobot.eventbus.EventBus;

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = ScreenBroadcastReceiver.class.getSimpleName();
    private static boolean isRegisterReceiver = false;
    private static Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            // 开屏
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            // 锁屏
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            // 解锁
            EventBus.getDefault().post(new ScreenActionBean(true));
        }
    }

    public void registerScreenBroadcastReceiver(Context mContext) {
        if (!isRegisterReceiver) {
            isRegisterReceiver = true;
            context = mContext;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(ScreenBroadcastReceiver.this, filter);
        }
    }

    public void unRegisterScreenBroadcastReceiver(Context mContext) {
        if (isRegisterReceiver && context != null && context.hashCode() == mContext.hashCode()) {
            context = null;
            isRegisterReceiver = false;
            mContext.unregisterReceiver(ScreenBroadcastReceiver.this);
        }
    }
}