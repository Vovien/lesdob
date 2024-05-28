package com.jmbon.middleware.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifeScope
import androidx.lifecycle.lifecycleScope
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.jmbon.middleware.dialog.CopyWechatDialog
import com.jmbon.middleware.extention.getActivity
import com.jmbon.middleware.extention.getLifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/******************************************************************************
 * Description: 第三方应用跳转工具类
 *
 * Author: jhg
 *
 * Date: 2023/4/21
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ThirdAppUtils {

    companion object {

        /**
         * 打开微信, 并复制数据
         * @param data: 打开微信时需要携带的数据
         */
        fun openWechatWithData(context: Context, data: String?): Boolean {
            if (data.isNullOrBlank()) {
                "微信号为空, 请稍后重试~".showToast()
                return false
            }

            val wechatPackageName = "com.tencent.mm"
            if (!AppUtils.isAppInstalled(wechatPackageName)) {
                " LesDoB检查到\n您未安装微信".showToast()
                return false
            }

            ClipboardUtils.copyText("$data")

            context.getLifecycleOwner()?.lifecycleScope?.launch {
                val dialog = CopyWechatDialog(context)
                dialog.showDialog()
                // 至少停留2秒后再发生跳转
                delay(1500)
                dialog.dismiss()
                realToWechat(context, wechatPackageName)
                delay(1000)
                dialog.dismiss()
            } ?: kotlin.run {
                realToWechat(context, wechatPackageName)
            }

            return true
        }

        private fun realToWechat(context: Context, wechatPackageName: String) {
            Intent().apply {
                `package` = wechatPackageName
                component = ComponentName(wechatPackageName, "${wechatPackageName}.ui.LauncherUI")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }
    }
}