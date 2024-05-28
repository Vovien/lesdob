package com.jmbon.middleware.db.net

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @author : leimg
 * time   : 2021/4/15
 * desc   : 网络缓存数据表
 * version: 1.0
 */
@Database(entities = arrayOf(NetCache::class), version = 1,exportSchema = false)
abstract class NetCacheDataBase : RoomDatabase() {
    abstract fun netCacheDao(): NetCacheDao
}
