package com.jmbon.middleware.arouter

import android.app.Activity
import com.alibaba.android.arouter.facade.template.IProvider

interface VideoActivityService : IProvider {
    fun toVideoPage(activity: Activity?)
}