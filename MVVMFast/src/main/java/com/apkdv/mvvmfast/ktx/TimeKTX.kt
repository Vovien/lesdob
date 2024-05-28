package com.apkdv.mvvmfast.ktx

import com.blankj.utilcode.util.TimeUtils

fun Long.toYMD(): String {
    return TimeUtils.millis2String(this * 1000L, "yyyy年MM月dd日")
}
fun Long.toY_M_D(): String {
    return TimeUtils.millis2String(this * 1000L, "yyyy-MM-dd")
}

fun Long.toY_M_D_hm(): String {
    return TimeUtils.millis2String(this * 1000L, "yyyy-MM-dd HH:mm")
}

fun Long.toDotYMD(): String {
    return TimeUtils.millis2String(this * 1000L, "yyyy.MM.dd")
}
fun Long.toFormat(format: String) {
    TimeUtils.millis2String(this * 1000L)
}