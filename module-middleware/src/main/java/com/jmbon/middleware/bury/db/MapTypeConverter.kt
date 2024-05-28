package com.jmbon.middleware.bury.db

import androidx.room.TypeConverter
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken

/******************************************************************************
 * Description: Map结构转换器, 便于数据库存储
 *
 * Author: jhg
 *
 * Date: 2023/8/1
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class MapTypeConverter {
    @TypeConverter
    fun fromMap(map: Map<String, String?>?): String? {
        return GsonUtils.toJson(map)
    }

    @TypeConverter
    fun toMap(mapString: String?): Map<String, String?>? {
        return GsonUtils.fromJson(mapString, object: TypeToken<Map<String, String?>?>() {}.type)
    }
}