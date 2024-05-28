package com.apkdv.mvvmfast.utils.abs

import android.app.Application
import cn.numeron.discovery.Discoverable

@Discoverable
interface IInitializer {
    fun init(application: Application)
}