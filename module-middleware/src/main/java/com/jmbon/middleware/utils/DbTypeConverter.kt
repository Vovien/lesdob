package com.jmbon.middleware.utils

import android.net.Uri
import androidx.room.TypeConverter
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken

class DbTypeConverter {

    @TypeConverter
    fun convertToString(charSequence: CharSequence?): String? {
        return charSequence?.toString()
    }

    @TypeConverter
    fun convertToString(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun convertToUri(str: String?): Uri? = str?.let(Uri::parse)

    @TypeConverter
    fun fromMap(map: Map<String, String?>?): String? {
        return GsonUtils.toJson(map)
    }

    @TypeConverter
    fun toMap(mapString: String?): Map<String, String?>? {
        return GsonUtils.fromJson(mapString, object: TypeToken<Map<String, String?>?>() {}.type)
    }
}