package com.apkdv.mvvmfast.utils

import android.content.Context
import android.os.Parcelable
import com.blankj.utilcode.util.GsonUtils.toJson
import com.blankj.utilcode.util.LogUtils
import com.tencent.mmkv.MMKV
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * use:
 *  private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }
 * private var boolean by mmkv.boolean(key = "boolean", defaultValue = false)
 * private var int by mmkv.int(key = "int", defaultValue = 0)
 * private var long by mmkv.long("long", 0L)
 * private var float by mmkv.float(key = "float", defaultValue = 0.0F)
 * private var double by mmkv.double(key = "double", defaultValue = 0.0)
 * private var byteArray by mmkv.byteArray(key = "byteArray")
 * private var string by mmkv.string(key = "string")
 * private var stringSet by mmkv.stringSet(key = "stringSet")
 * private var parcelable by mmkv.parcelable<UserData>("parcelable")
 *
 * 使用前必须确保MMKV已经初始化
 * 代理模式 适用于需要动态改变的场景
 */
private inline fun <T> MMKV.delegate(
    key: String? = null,
    defaultValue: T,
    crossinline getter: MMKV.(String, T) -> T,
    crossinline setter: MMKV.(String, T) -> Boolean
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            setter(key ?: property.name, value)
        }
    }

fun MMKV.boolean(
    key: String? = null,
    defaultValue: Boolean = false
): ReadWriteProperty<Any, Boolean> =
    delegate(key, defaultValue, MMKV::decodeBool, MMKV::encode)

fun MMKV.int(key: String? = null, defaultValue: Int = 0): ReadWriteProperty<Any, Int> =
    delegate(key, defaultValue, MMKV::decodeInt, MMKV::encode)

fun MMKV.long(key: String? = null, defaultValue: Long = 0L): ReadWriteProperty<Any, Long> =
    delegate(key, defaultValue, MMKV::decodeLong, MMKV::encode)

fun MMKV.float(key: String? = null, defaultValue: Float = 0.0F): ReadWriteProperty<Any, Float> =
    delegate(key, defaultValue, MMKV::decodeFloat, MMKV::encode)

fun MMKV.double(key: String? = null, defaultValue: Double = 0.0): ReadWriteProperty<Any, Double> =
    delegate(key, defaultValue, MMKV::decodeDouble, MMKV::encode)

private inline fun <T> MMKV.nullAbleDefaultValueDelegate(
    key: String? = null,
    defaultValue: T?,
    crossinline getter: MMKV.(String, T?) -> T?,
    crossinline setter: MMKV.(String, T) -> Boolean
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue)!!

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            setter(key ?: property.name, value)
        }
    }

fun MMKV.byteArray(
    key: String? = null,
    defaultValue: ByteArray? = null
): ReadWriteProperty<Any, ByteArray> =
    nullAbleDefaultValueDelegate(key, defaultValue, MMKV::decodeBytes, MMKV::encode)

fun MMKV.string(key: String? = null, defaultValue: String? = null): ReadWriteProperty<Any, String> =
    nullAbleDefaultValueDelegate(key, defaultValue, MMKV::decodeString, MMKV::encode)

fun MMKV.stringSet(
    key: String? = null,
    defaultValue: Set<String>? = null
): ReadWriteProperty<Any, Set<String>> =
    nullAbleDefaultValueDelegate(key, defaultValue, MMKV::decodeStringSet, MMKV::encode)

inline fun <reified T : Parcelable> MMKV.list(
    key: String? = null,
    defaultValue: ArrayList<T>? = null
): ReadWriteProperty<Any, ArrayList<T>> =
    object : ReadWriteProperty<Any, ArrayList<T>> {
        override fun getValue(thisRef: Any, property: KProperty<*>): ArrayList<T> =
            getListFromString(decodeString(key ?: property.name), defaultValue)


        override fun setValue(thisRef: Any, property: KProperty<*>, value: ArrayList<T>) {
            encode(key ?: property.name, saveParcelableListToString(value))
        }
    }


fun saveParcelableListToString(T: List<Parcelable>): String {
    return if (T.isNullOrEmpty())
        "" else
        toJson(T)
}

/**
 * 保存的Parcelable的数据
 */
fun <T : Parcelable> T?.saveParcelable(key: String) {
    mmkv.encode(key, this)
}

/**
 * 保存的Parcelable list 的数据
 */
fun <T : List<Any>> T?.saveParcelableList(key: String) {
    mmkv.encode(key, toJson(this))
}

/**
 * 读取Parcelable的数据
 */
inline fun <reified T : Parcelable> String?.getParcelable(defaultValue: T? = null): T? {
    return mmkv.decodeParcelable(this, T::class.java, defaultValue)
}

/**
 * 读取Parcelable list的数据
 */
inline fun <reified T : Parcelable> String?.getParcelableList(): ArrayList<T> {
    return getListFromString(mmkv.decodeString(this), arrayListOf())
}

inline fun <reified T : Parcelable> getListFromString(
    json: String?,
    defaultValue: ArrayList<T>?
): ArrayList<T> {
    return if (json.isNullOrEmpty()) {
        return if (defaultValue.isNullOrEmpty())
            arrayListOf()
        else
            defaultValue
    } else {
        GsonUtil.gson().fromJson(json, GsonUtil.getListType(T::class.java))
    }
}

// for java
val mmkv by lazy { MMKV.mmkvWithID("jmtuan", MMKV.MULTI_PROCESS_MODE) }

fun Any?.saveToMMKV(key: String) {
    if (this != null)
        if (this is Collections || this is Parcelable) {
            throw Exception("cant't not save Collections or Parcelable")
        } else
            when (this) {
                is String -> if (this.isNotEmpty()) mmkv.encode(key, this)
                is Float -> mmkv.encode(key, this)
                is Boolean -> mmkv.encode(key, this)
                is Int -> mmkv.encode(key, this)
                is Long -> mmkv.encode(key, this)
                is Double -> mmkv.encode(key, this)
                is ByteArray -> mmkv.encode(key, this)
                is Nothing -> return
            }
}


fun String?.getInt(): Int {
    return mmkv.decodeInt(this, 0)
}

fun String?.getDouble(): Double {
    return mmkv.decodeDouble(this, 0.00)
}


fun String?.getLong(): Long {
    return mmkv.decodeLong(this, 0L)
}

fun String?.getBoolean(): Boolean {
    return mmkv.decodeBool(this, false)
}

fun String?.getBoolean(defaultValue: Boolean): Boolean {
    return mmkv.decodeBool(this, defaultValue)
}

fun String?.getFloat(): Float {
    return mmkv.decodeFloat(this, 0F)
}

fun String?.getByteArray(): ByteArray? {
    return mmkv.decodeBytes(this, byteArrayOf())
}

fun String?.getString(): String? {
    return mmkv.decodeString(this, "")
}


fun String?.getStringSet(): Set<String>? {
    return mmkv.decodeStringSet(this, Collections.emptySet())
}

fun String?.cleanMMKV() {
    this?.let {
        mmkv.removeValueForKey(it)
    }
}

//lateinit var mmkv: MMKV
fun initMMKV(context: Context) {
    MMKV.initialize(context)

}
