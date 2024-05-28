package com.jmbon.middleware.extend

import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.startCoroutine

private class MainThreadExecutor : Executor {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }

}

val ioThreadExecutor: Executor by lazy(LazyThreadSafetyMode.SYNCHRONIZED, Dispatchers.IO::asExecutor)

val mainThreadExecutor: Executor by lazy(LazyThreadSafetyMode.SYNCHRONIZED, ::MainThreadExecutor)

/** 尝试从[Context]中获取[LifecycleOwner]，如果不行，则返回null */
fun Context.getLifecycleOwner(): LifecycleOwner? {
    return when (this) {
        is LifecycleOwner -> this
        !is ContextWrapper -> null
        else -> baseContext.getLifecycleOwner()
    }
}
