package com.jmbon.middleware

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.widget.arouter.MobEventService

/**
 * @author : leimg
 * time   : 2022/1/24
 * desc   :
 * version: 1.0
 */
@Route(path = "/middleware/mobevent/service", name = "mob event")
class MobEventServiceImp : MobEventService {
    override fun sendEvent(key: String) {
        MobAnalysisUtils.mInstance.sendEvent(key)
    }

    override fun sendEvent(key: String, mapValue: MutableMap<String, Any>) {
        MobAnalysisUtils.mInstance.sendEvent(key, mapValue)
    }

    override fun init(context: Context?) {
    }
}