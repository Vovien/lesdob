package com.jmbon.middleware.db.net

import androidx.room.*

/**
 * @author : leimg
 * time   : 2021/4/15
 * desc   :
 * version: 1.0
 */
@Dao
interface NetCacheDao {
    // 根据文章id和用户id查询文章草稿
    @Query("SELECT * FROM netcache WHERE urlKey = :urlKey")
    fun getNetCache(urlKey: String): NetCache

    // 增
    @Insert
    fun insert(vararg netCache: NetCache)

    // 删
    @Delete
    fun delete(netCache: NetCache)

    // 改
    @Update
    fun update(netCache: NetCache): Int

}