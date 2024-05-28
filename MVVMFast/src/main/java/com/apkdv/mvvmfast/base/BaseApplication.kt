package com.apkdv.mvvmfast.base

import android.app.Application
import android.content.Context
import android.content.res.Resources
import cn.numeron.discovery.Discoveries
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.base.delegate.AppDelegate
import com.apkdv.mvvmfast.base.delegate.AppLifecycles
import com.apkdv.mvvmfast.gson.GsonFactory
import com.apkdv.mvvmfast.utils.JMBToast
import com.apkdv.mvvmfast.utils.abs.IInitializer
import com.apkdv.mvvmfast.utils.initContext
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.Utils
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.*


/**
 * desc :BaseApplication
 */
open class BaseApplication : Application() {
    private var mAppDelegate: AppLifecycles? = null
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) mAppDelegate = AppDelegate(base)
        mAppDelegate?.attachBaseContext(base)

    }

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }


    override fun onCreate() {
        super.onCreate()
        mCoroutineScope.launch(Dispatchers.Main.immediate) {
            val job = mutableListOf<Deferred<String>>()
            //非主线程初始化
            // 并行job
            job.add(async(Dispatchers.Default) {
                GsonUtils.setGsonDelegate(
                    GsonFactory.newGsonBuilder().serializeNulls().disableHtmlEscaping().create()
                )
                return@async "GsonFactory"
            })

            job.add(async(Dispatchers.Default) {
                ToastUtils.init(this@BaseApplication)
                ToastUtils.setStyle(JMBToast())
                return@async "ToastUtils"
            })

            if (BuildConfig.DEBUG) {
                ARouter.openLog()
                ARouter.openDebug()
                ARouter.printStackTrace()
            }

            ARouter.init(this@BaseApplication)
//            job.add(async(Dispatchers.Default) {
//                ARouter.init(this@BaseApplication)
//                return@async "ARouter"
//            })

            // 主线程初始化第三方依赖进行初始化
            Utils.init(this@BaseApplication)
            mAppDelegate?.onCreate(this@BaseApplication)
            // 3. 等待每一个子线程初始化的依赖完成
            job.forEach { it.await() }
        }
        initContext(this)
        initModules()
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
    }

    override fun getResources(): Resources {
        val resources = super.getResources()
        if (resources != null && resources.configuration.fontScale != 1.0f) {
            val configuration = resources.configuration
            configuration.fontScale = 1.0f
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return resources
    }

    private fun initModules() {
        Discoveries.getAllInstances<IInitializer>().forEach {
            it.init(this)
        }
    }

}