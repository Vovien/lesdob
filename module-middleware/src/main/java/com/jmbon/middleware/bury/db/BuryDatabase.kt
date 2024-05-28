package com.jmbon.middleware.bury.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/******************************************************************************
 * Description: 埋点数据库
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Database(entities = [BuryPointInfo::class], version = 3, exportSchema = false)
internal abstract class BuryDatabase : RoomDatabase() {

    abstract fun buryDao(): BuryDao

    companion object {

        @Volatile
        private var INSTANCE: BuryDatabase? = null

        fun getDatabase(context: Context): BuryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BuryDatabase::class.java,
                    "bury_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}