package com.jmbon.middleware.extend

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

/**
 * 运行在主线程上的、生命周期与MainActivity绑定的协程范围
 */
object AppRuntimeScope : CoroutineScope, LifecycleEventObserver {

    override lateinit var coroutineContext: CoroutineContext

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            coroutineContext = mainThreadExecutor.asCoroutineDispatcher() + SupervisorJob()
        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
            cancel()
        }
    }

}