package com.jmbon.middleware.oiad

import com.apkdv.mvvmfast.utils.getBoolean
import com.apkdv.mvvmfast.utils.getString
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.bury.BuryHelper
import com.jmbon.middleware.bury.db.BuryPointInfo
import com.jmbon.middleware.bury.event.AppEventEnum
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.MMKVKey
import com.umeng.commonsdk.UMConfigure
import java.util.UUID

/******************************************************************************
 * Description: 获取OAID
 *
 * Author: jhg
 *
 * Date: 2023/10/18
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class OAIDUtils {

    companion object {

        var OAID = ""
        get() {
            if (!field.isNullOrBlank()) {
                return field
            }
            field = MMKVKey.OAID.getString() ?: ""
            if (!field.isNullOrBlank()) {
                return field
            }
            return ""
        }

        /**
         * 上报OAID
         */
        fun uploadOAID() {
            val savedOAID = MMKVKey.OAID.getString()
            if (savedOAID.isNullOrBlank()) {
                loadOAID {
                    BuryHelper.addEvent(BuryPointInfo(event_id = AppEventEnum.EVENT_START_APP.value), uploadDirectly = true)
                }
            } else {
                if (!MMKVKey.OAID_UPLOAD_SUCCESS.getBoolean()) {
                    CommonViewModel.activeDevice(savedOAID)
                }
                BuryHelper.addEvent(BuryPointInfo(event_id = AppEventEnum.EVENT_START_APP.value), uploadDirectly = true)
            }
        }
        
        /**
         * 载入OAID
         * @param action: 获取OAID后的操作, 默认为绑定设备
         */
        private fun loadOAID(action: ((oaid: String?) -> Unit)?  = null) {
            UMConfigure.getOaid(Utils.getApp()) {
                val realOAID = if (it.isNullOrBlank()) {
                    val androidId = DeviceUtils.getAndroidID()
                    if (!androidId.isNullOrBlank()) {
                        androidId
                    } else {
                        UUID.randomUUID().toString()
                    }
                } else {
                    it
                }
                realOAID.saveToMMKV(MMKVKey.OAID)
                action?.invoke(realOAID)
                CommonViewModel.activeDevice(realOAID)
            }
        }
    }
}