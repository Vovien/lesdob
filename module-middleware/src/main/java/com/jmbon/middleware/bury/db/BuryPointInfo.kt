package com.jmbon.middleware.bury.db

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/

const val BURY_POINT_TABLE_NAME = "table_bury_point_info"

@Keep
@Parcelize
@Entity(tableName = "$BURY_POINT_TABLE_NAME")
@TypeConverters(MapTypeConverter::class)
data class BuryPointInfo @Ignore constructor (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    /**
     * 事件类型
     */
    var event_id: String = "",
    /**
     * 事件发生的时间, 时间戳格式
     */
    var timestamp: Long = System.currentTimeMillis(),
    /**
     * 事件对应的参数, Json格式
     */
    var params: Map<String, String?>? = mutableMapOf(),
) : Parcelable {

    constructor() : this(0)
}