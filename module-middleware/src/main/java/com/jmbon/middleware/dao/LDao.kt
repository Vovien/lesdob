package com.jmbon.middleware.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface LDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: Collection<T>): List<Long>

    @Delete
    suspend fun delete(entity: T)

    @Delete
    suspend fun delete(entities: Collection<T>)

    @Update
    suspend fun update(entity: T): Int

    @Update
    suspend fun update(entities: Collection<T>): Int

}