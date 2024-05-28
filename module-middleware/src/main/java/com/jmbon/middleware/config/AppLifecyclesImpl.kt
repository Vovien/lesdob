package com.jmbon.middleware.config

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.delegate.AppLifecycles
import com.apkdv.mvvmfast.utils.initMMKV
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.config.network.RxHttpManager
import com.jmbon.middleware.interpolator.EaseCubicInterpolator
import com.jmbon.middleware.utils.getRealChannel
import com.jmbon.widget.refresh.JmbonRefreshFooter
import com.jmbon.widget.refresh.JmbonRefreshHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


/**
 * desc : Applicaiton 生命周期的实现
 */
class AppLifecyclesImpl : AppLifecycles {
    private val activityLifecyclesImpl by lazy {
        ActivityLifecyclesImpl()
    }

    override fun attachBaseContext(base: Context) {
    }

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }
    override fun onCreate(application: Application) {
        // 错误拦截 打开会屏蔽原始错误信息
//        if (BuildConfig.DEBUG){
//            val postcard = ARouter.getInstance().build("/main/welcome")
//            LogisticsCenter.completion(postcard)
//            UncaughtExceptionHandlerImpl.getInstance().init(
//                application,  true, 0,
//                postcard.destination
//            )
//        }


        if (BuildConfig.DEBUG) {
            ARouter.openLog() // 开启日志
            ARouter.openDebug() // 使用InstantRun的时候，需要打开该开关，上线之后关闭，否则有安全风险
            ARouter.printStackTrace() // 打印日志的时候打印线程堆栈
        }
        mCoroutineScope.launch(Dispatchers.Main.immediate) {
            val job = mutableListOf<Deferred<String>>()
            // 1. 对非必须在主线程初始化的第三方依赖发起并行初始化
            // 并行job
            job.add(async(Dispatchers.Default) {
                initMMKV(application)

                return@async "MMKV"
            })

            job.add(async(Dispatchers.Default) {
                RxHttpManager(application)
                return@async "RxHttpManager"
            })
            job.add(async(Dispatchers.Default) {
                initRefresh()
                return@async "initRefresh"
            })

            job.add(async(Dispatchers.Default) {
                LogUtils.getConfig().run {
                    globalTag = "JNM"
                    isLogSwitch = BuildConfig.DEBUG
                    setSingleTagSwitch(true)
                }
                return@async "LogUtils"
            })
            job.add(async(Dispatchers.Default) {
//                友盟预初始化
                //设置LOG开关，默认为false
                UMConfigure.setLogEnabled(BuildConfig.DEBUG)

                UMConfigure.preInit(
                    application,
                    BuildConfig.UMENG_API_KEY,
                    getRealChannel()
                )
                return@async "UMConfigure"
            })
            // 3. 等待每一个子线程初始化的依赖完成
            job.awaitAll()
        }

        application.registerActivityLifecycleCallbacks(activityLifecyclesImpl)
    }

    private fun initRefresh() {
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setEnableLoadMoreWhenContentNotFull(false)
//            layout.setEnableAutoLoadMore(false)
            layout.setHeaderHeight(56f)
            layout.setFooterHeight(56f)
            layout.setHeaderTriggerRate(1f)
            layout.setFooterTriggerRate(1f)
            layout.setHeaderMaxDragRate(3f)
            layout.setFooterMaxDragRate(3f)
            layout.setReboundInterpolator(EaseCubicInterpolator())
            layout.setReboundDuration(200)
            //layout.setPrimaryColorsId(R.color.ColorFAFA)//全局设置主题颜色
        }
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            JmbonRefreshHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            JmbonRefreshFooter(context)
        }
    }

    override fun onTerminate(application: Application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecyclesImpl)
    }

}
