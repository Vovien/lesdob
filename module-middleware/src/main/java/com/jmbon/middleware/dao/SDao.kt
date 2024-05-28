package com.jmbon.middleware.dao

import androidx.room.*

interface SDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: Collection<T>): List<Long>

    @Delete
    suspend fun delete(entity: T): Int

    @Delete
    suspend fun delete(entities: Collection<T>): Int

    @Update
    suspend fun update(entity: T): Int

    @Update
    suspend fun update(entities: Collection<T>): Int

}