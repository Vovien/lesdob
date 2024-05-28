package com.jmbon.middleware.extention

/******************************************************************************
 * Description: 通用性的扩展方法
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
inline fun <reified T : Enum<T>> getEnumByValue(value: Int): T {
    return enumValues<T>().run {
        find { it.ordinal == value } ?: get(0)
    }
}