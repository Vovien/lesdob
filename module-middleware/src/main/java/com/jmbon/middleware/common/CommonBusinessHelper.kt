package com.jmbon.middleware.common

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ScreenUtils
import com.jmbon.middleware.dialog.WeChatDialog
import kotlin.math.abs

/******************************************************************************
 * Description: 通用业务逻辑
 *
 * Author: jhg
 *
 * Date: 2023/9/26
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object CommonBusinessHelper {

    val screenHeight = ScreenUtils.getScreenHeight()

    /**
     * 显示弹框
     * @param scrollOffset: 页面的滚动距离
     * @param entryCount: 进入页面的次数
     * @param needShow: 弹框是否已显示
     * @param showAction: 弹框显示后的回调
     */
    inline fun showWechatDialog(scrollOffset: Int, entryCount: Int, needShow: Boolean, showAction: () -> Unit) {
        if (!needShow) {
            return
        }

        if (entryCount == 2) {
            // 从第二次开始, 每三次显示一次, 当用户开始滑动页面时显示弹框
            if (abs(scrollOffset) > 10) {
                realShowWechatDialog(showAction)
            }
        } else {
            // 不满足以上条件时, 页面滑动1.5个屏幕高度时显示弹框
            if (abs(scrollOffset) > screenHeight * 1.5f) {
                realShowWechatDialog(showAction)
            }
        }
    }

    /**
     * 显示弹框
     * @param showAction: 显示弹框后的回调
     */
    inline fun realShowWechatDialog(showAction: () -> Unit) {
        CommonViewModel.popupImageFlow.value?.pop_info?.apply {
            if (this.popupImg.isNullOrBlank()) {
                return
            }

            val dialog = WeChatDialog(ActivityUtils.getTopActivity(), this)
            dialog.showDialog()
            showAction()
        }
    }
}