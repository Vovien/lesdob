package com.jmbon.middleware.config

import com.apkdv.mvvmfast.utils.getString
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.GsonUtils
import java.lang.reflect.Type

object CacheConfig {
    const val SQUARE_CIRCLE = "square_circle"
    const val SQUARE_LIST = "square_list"
    const val SQUARE_TAB = "square_tab"
    const val GROUP_TOP_QUESTION_ANSWER = "group_top_qa_answer" //群问答置顶缓存
    const val GROUP_QUESTION_ANSWER = "group_qa_answer" //群问答缓存
    const val GROUP_QA_TAB = "group_qa_tab" //群问答tab缓存


//    const val MAIN_CIRCLE= "main_circle"//首页圈子
//    const val MAIN_MINI= "main_mini"//首页小程序
//    const val MAIN_SEARCH_HOT= "main_search_hot"//首页搜索热词
    const val MAIN_HEADER_INFO= "main_header_info"//首页搜索热词

    const val prefix = "cache_CacheConfig_"


    fun saveCache(key: String, json: String) {
       json.saveToMMKV( "$prefix$key")
    }

    inline fun <reified T> getCache(key: String, type: Type): T? {
        val json = "$prefix$key".getString()
        return GsonUtils.fromJson(json, type)
    }

    fun cleanAllCache() {
        mmkv.allKeys()?.forEach {
            if (it.startsWith(prefix)) {
                mmkv.removeValueForKey(it)
            }
        }
    }
}