package com.jmbon.middleware.utils

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.config.Constant
import com.umeng.analytics.MobclickAgent

/**
 * @author : leimg
 * time   : 2022/1/24
 * desc   :
 * version: 1.0
 */
class MobAnalysisUtils {
    companion object {
        private val mContext = Utils.getApp()
        private val emptyMap = mutableMapOf<String, Any>()
        val mInstance: MobAnalysisUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MobAnalysisUtils()
        }
    }

    fun sendEvent(key: String) {
        sendEvent(key, mutableMapOf("user_id" to Constant.userInfo.id)) //基础属性
        // MobclickAgent.onEvent(mContext, key)
        printLog(key, "")
    }

    fun sendEvent(key: String, mapValue: MutableMap<String, Any>) {
        mapValue["user_id"] = Constant.userInfo.id //基础属性
        MobclickAgent.onEventObject(mContext, key, mapValue)
        printLog(key, mapValue.toString())
    }

    fun sendObject(key: String, mapKey: Array<String>, value: Array<Any>) {
        val map = mutableMapOf<String, Any>()
        if (mapKey.size == value.size) {
            mapKey.forEachIndexed { index, s ->
                if (value[index] is String) {
                    map[s] = value[index]
                } else map[s] = value[index].toString()
            }
        }
        map["user_id"] = Constant.userInfo.id //基础属性
        MobclickAgent.onEventObject(mContext, key, map)
        printLog(key, map.toString())
    }

    fun sendObject(key: String, mapKey: String, value: Any) {
        val map = mutableMapOf<String, Any>()
        map["user_id"] = Constant.userInfo.id //基础属性
        map[mapKey] = if (value is String) value else value.toString()
        MobclickAgent.onEventObject(mContext, key, map)
        printLog(key, map.toString())
    }


    private fun printLog(key: String, value: String) {
        LogUtils.d("event->$key:$value")
    }

}