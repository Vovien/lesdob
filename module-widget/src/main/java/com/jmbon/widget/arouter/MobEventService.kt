package com.jmbon.widget.arouter

import com.alibaba.android.arouter.facade.template.IProvider

interface MobEventService : IProvider {
    fun sendEvent(key: String)
    fun sendEvent(key: String, mapValue: MutableMap<String, Any>)
}