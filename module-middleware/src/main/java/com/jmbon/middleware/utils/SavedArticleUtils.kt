package com.jmbon.middleware.utils

import com.apkdv.mvvmfast.utils.getString
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.jmbon.middleware.bean.ArticleList
import com.jmbon.middleware.bean.event.UserArticleChangedEvent
import com.jmbon.middleware.config.Constant
import org.greenrobot.eventbus.EventBus

object SavedArticleUtils {
    // KEY
    val USER_ARTICLE = "user_article"

    fun saveArticle(item: ArticleList) {
        if (Constant.isLogin) {
            val map = LinkedHashMap<Int, ArticleList>()
            val key = "${Constant.userInfo.id}" + USER_ARTICLE
            val saveData = key.getString()
            if (saveData.isNotNullEmpty()) {
                map.putAll(
                    GsonUtils.fromJson<LinkedHashMap<Int, ArticleList>>(
                        saveData,
                        object : TypeToken<LinkedHashMap<Int, ArticleList>>() {}.type
                    )
                )
            }

            map[item.id] = item
            GsonUtils.toJson(map, LinkedHashMap::class.java).saveToMMKV(key)
            EventBus.getDefault().post(UserArticleChangedEvent())
        }
    }

    fun getUserArticle(): LinkedHashMap<Int, ArticleList> {
        if (!Constant.isLogin)
            return linkedMapOf()
        val map = LinkedHashMap<Int, ArticleList>()
        if (Constant.isLogin) {
            val key = "${Constant.userInfo.id}" + USER_ARTICLE
            val saveData = key.getString()
            if (saveData.isNotNullEmpty()) {
                map.putAll(
                    GsonUtils.fromJson<LinkedHashMap<Int, ArticleList>>(
                        saveData,
                        object : TypeToken<LinkedHashMap<Int, ArticleList>>() {}.type
                    )
                )
            }
        }
        return map
    }

    fun deleteUserArticle(detailID: Int) {
        if (Constant.isLogin) {
            val key = "${Constant.userInfo.id}" + USER_ARTICLE
            val map = getUserArticle()
            map.remove(detailID)
            GsonUtils.toJson(map, LinkedHashMap::class.java).saveToMMKV(key)
        }
    }
}