package com.jmbon.middleware.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jmbon.middleware.BuildConfig;
import com.jmbon.middleware.arouter.MiniProgramService;
import com.jmbon.middleware.dialog.ToastType;
import com.jmbon.middleware.utils.MobAnalysisUtils;

public class NotificationClickReceiver extends BroadcastReceiver {
    private final MiniProgramService service = (MiniProgramService) ARouter.getInstance().build("/miniprogram/start/service").navigation();

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getStringExtra("url");
        String toolId = intent.getStringExtra("toolId");
        String type = intent.getStringExtra("type");

        if ("medication_push".equals(type)) {
            //点击用药管理通知
            MobAnalysisUtils.Companion.getMInstance().sendEvent("Event_SystemPush_MedicationAssistant");
            //用药管理
            service.startMiniProgram("2021111700000004", null);
            MobAnalysisUtils.Companion.getMInstance().sendEvent("Event_SmallTools_MedicationAssistant");
        } else if ("menstrual".equals(type)) {
            //点击姨妈经期通知
            MobAnalysisUtils.Companion.getMInstance().sendEvent("Event_SystemPush_AImenstruation");
            //跳转经期小程序
            service.startMiniProgram(BuildConfig.MENSES_ID, null);
            MobAnalysisUtils.Companion.getMInstance().sendEvent("Event_SmallTools_AImenstruation");
        } else if (ToastType.CIRCLE.name().equals(type)) {
            Intent mIntent = intent.getParcelableExtra("intent");
            mIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
            );
            context.startActivity(mIntent);
            MobAnalysisUtils.Companion.getMInstance().sendEvent("Event_SystemPush_AskHelp");
        } else if (ToastType.QUESTIONS.name().equals(type)) {
            Intent mIntent = intent.getParcelableExtra("intent");
            mIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
            );
            context.startActivity(mIntent);
            MobAnalysisUtils.Companion.getMInstance().sendEvent("Event_SystemPush_QA");
        }

    }
}