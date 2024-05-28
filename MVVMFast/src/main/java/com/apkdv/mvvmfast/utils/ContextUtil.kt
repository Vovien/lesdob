@file:JvmName("ContextUtil")

package com.apkdv.mvvmfast.utils

import android.content.Context
import android.content.ContextWrapper

private class GlobalContext(context: Context) : ContextWrapper(context)

private lateinit var sGlobalContext: GlobalContext

fun initContext(context: Context) {
    if (!::sGlobalContext.isInitialized) {
        sGlobalContext = GlobalContext(context.applicationContext)
    }
}

val context: Context
    get() = sGlobalContext