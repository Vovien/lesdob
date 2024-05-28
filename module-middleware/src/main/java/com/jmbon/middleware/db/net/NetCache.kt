package com.jmbon.middleware.db.net

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

/**
 * @author : leimg
 * time   : 2021/4/15
 * desc   : 网络缓存数据类
 * version: 1.0
 */
@Entity
@Keep
data class NetCache(
    @PrimaryKey(autoGenerate = true) val uid: Int, var urlKey: String, var content: String
) {

    inline fun <reified T> convertToBean(): T {
       return Gson().fromJson(content, T::class.java)
    }

}