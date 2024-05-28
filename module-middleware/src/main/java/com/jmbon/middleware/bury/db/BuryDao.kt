package com.jmbon.middleware.bury.db

import androidx.room.*

/******************************************************************************
 * Description: 埋点相关的数据操作
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Dao
internal interface BuryDao {

    @Query("SELECT * FROM $BURY_POINT_TABLE_NAME")
    suspend fun getAllRecords(): List<BuryPointInfo>?

    @Query("SELECT COUNT(*) FROM $BURY_POINT_TABLE_NAME")
    suspend fun getCount(): Int

    @Insert
    suspend fun insert(user: BuryPointInfo): Long

    @Update
    suspend fun update(user: BuryPointInfo)

    @Delete
    suspend fun delete(record: BuryPointInfo)

    @Delete
    suspend fun delete(recordList: List<BuryPointInfo>)
}