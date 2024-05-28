package com.tubewiki.mine.view.login.utils

import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.tubewiki.mine.R

fun TextView.setPrivacyPolicy() {
    SpanUtils.with(this).append("已阅读并同意").append("《LesDoB用户协议》")
        .setClickSpan(ColorUtils.getColor(R.color.privacy_color), false) {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.USER_PROTOCOL)
                .withString("title", StringUtils.getString(R.string.about_terms_service))
                .withBoolean("enableTBS", false)
                .navigation()
        }
        .append("《隐私政策》").setClickSpan(ColorUtils.getColor(R.color.privacy_color), false) {
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.PRIVACY_PROTOCOL)
                .withString("title", StringUtils.getString(R.string.about_privacy_policy))
                .withBoolean("enableTBS", false)
                .navigation()
        }.append(".").create()
}