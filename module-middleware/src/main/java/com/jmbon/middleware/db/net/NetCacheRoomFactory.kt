package com.jmbon.middleware.db.net

import android.content.Context
import androidx.room.Room

class NetCacheRoomFactory private constructor() {
    private object Holder {
        val instance = NetCacheRoomFactory()
    }

    fun init(application: Context?) {
        netCacheDataBase = Room.databaseBuilder(
            application!!,
            NetCacheDataBase::class.java,
            DB_NAME
        )
            .allowMainThreadQueries() //支持主线程
            .build()
    }

    companion object {
        private const val DB_NAME = "NetCacheDatabase.db"

        @Volatile
        var netCacheDataBase: NetCacheDataBase? = null
        val instance: NetCacheRoomFactory
            get() = Holder.instance
    }
}