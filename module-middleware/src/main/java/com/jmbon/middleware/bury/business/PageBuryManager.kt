package com.jmbon.middleware.bury.business

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/******************************************************************************
 * Description: 页面埋点管理器
 *
 * Author: jhg
 *
 * Date: 2023/7/21
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object PageBuryManager {

    const val TAG = "PageLifecycle:"

    /**
     * 是否已初始化, 避免多次初始化
     */
    private var isInit = false

    /**
     * App切换到前台的时间
     */
    private var startTime = System.currentTimeMillis()

    /**
     * App的停留时长
     */
    private var stayTime = 0L

    /**
     * App是否在前台
     */
    private var isAppForeground = true

    /**
     * 是否是页面跳转
     */
    private var isPageJump = true

    /**
     * 判断页面跳转的延时时间
     */
    private const val delayTime = 200L

    fun init(application: Application) {
        if (isInit) {
            // 当温启动的时候需要将这些数据重新初始化
            startTime = System.currentTimeMillis()
            stayTime = 0
            return
        }

        isInit = true
        // 监听全局生命周期
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                isPageJump = true
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                // 只有页面跳转或从其他页面返回时才调用进入事件, 从后台返回不调用
                LogUtils.i("=================isPageJump==${isPageJump}==${activity.localClassName}")
                if (isPageJump) {
                    pageOnStart(activity)
                } else {
                    applicationOnStart()
                }
            }

            override fun onActivityPaused(activity: Activity) {
                isPageJump = activity.isFinishing
                LogUtils.i("=================isPageJump==${isPageJump}==${activity.localClassName}")
                GlobalScope.launch {
                    delay(delayTime)
                    if (isPageJump) {
                        // 跳转到新页面
                        pageOnStop(activity)
                    } else if (!activity.isFinishing || (ActivityUtils.getActivityList().size <= 1)) {
                        // 切到后台
                        applicationOnStop()
                    } else {
                        // 从其他页面返回
                        isPageJump = true
                    }
                }
            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }

    /**
     * 从其他页面进入时触发, 从后台切回时不触发
     */
    private fun pageOnStart(activity: Activity) {
        LogUtils.i(TAG, "进入了页面==${activity.localClassName}")
    }

    /**
     * 跳转其他页面时触发, 切换到后台时不触发
     */
    private fun pageOnStop(activity: Activity) {
        LogUtils.i(TAG, "离开了页面==${activity.localClassName}")
    }

    /**
     * App是否在前台
     */
    fun isAppForeground() = isAppForeground

    /**
     * App被切到前台时调用, 即埋点约定的bgOnStart
     */
    fun applicationOnStart() {
        isAppForeground = true
        startTime = System.currentTimeMillis()
        LogUtils.i(TAG, "回到前台了==${ActivityUtils.getTopActivity()}")
    }

    /**
     * App被切到前台时调用, 即埋点约定的bgOnStop
     */
    fun applicationOnStop() {
        isAppForeground = false
        LogUtils.i(TAG, "回到后台了")
    }
}