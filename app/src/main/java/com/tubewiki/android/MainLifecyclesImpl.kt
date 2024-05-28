package com.tubewiki.android

import android.app.Application
import android.content.Context
import com.apkdv.mvvmfast.base.delegate.AppLifecycles

import com.tencent.android.tpush.XGPushConfig
import kotlinx.coroutines.MainScope


/**
 * desc : Applicaiton 生命周期的实现
 */
class MainLifecyclesImpl : AppLifecycles {

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }

    override fun attachBaseContext(base: Context) {
        XGPushConfig.setAutoInit(false)

      //  Log.e("XGPushConfig", "setAutoInit")
    }


    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {
    }


}
