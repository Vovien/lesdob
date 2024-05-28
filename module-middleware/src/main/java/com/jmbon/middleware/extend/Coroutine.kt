package com.jmbon.middleware.extend

import android.view.View
import com.jmbon.middleware.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * 获取这个View中缓存的协程范围
 * 如果没有，则创建一个并保存到tag中
 * 这个协程范围中的任务默认运行在主线程中，并且在View从ViewGroup中分离时停止所有任务
 */
val View.attachedScope: CoroutineScope
    get() {
        val tag = getTag(R.id.view_coroutine)
        if (tag is ViewCoroutineScope) {
            return tag
        }
        val viewCoroutineScope = ViewCoroutineScope(this)
        setTag(R.id.view_coroutine, viewCoroutineScope)
        return viewCoroutineScope
    }

/**
 * 获取这个View中缓存的协程调度器
 * 如果没有，则创建一个并保存到tag中
 * 这个协程调度器会将任务通过Handle提交到主线程中运行
 */
val View.attachedDispatcher: CoroutineDispatcher
    get() {
        var dispatcher = getTag(R.id.view_dispatcher) as? CoroutineDispatcher
        if (dispatcher != null) {
            return dispatcher
        }
        dispatcher = ViewDispatcher(this)
        setTag(R.id.view_dispatcher, dispatcher)
        return dispatcher
    }

internal class ViewCoroutineScope(view: View) : CoroutineScope, View.OnAttachStateChangeListener {

    init {
        view.addOnAttachStateChangeListener(this)
    }

    override val coroutineContext = view.attachedDispatcher + SupervisorJob()

    override fun onViewAttachedToWindow(v: View) = Unit

    override fun onViewDetachedFromWindow(v: View) {
        // 在View被分离时，应该清理掉View中记录的协程范围
        v.setTag(R.id.view_coroutine, null)
        cancel()
    }

}

internal class ViewDispatcher(private val view: View) : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        view.post(block)
    }

}