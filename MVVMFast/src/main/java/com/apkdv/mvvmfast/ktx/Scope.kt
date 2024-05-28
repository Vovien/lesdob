package com.apkdv.mvvmfast.ktx

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


//<editor-fold desc="异步任务">
/**
 * 异步作用域
 *
 * 该作用域生命周期跟随整个应用, 注意内存泄漏
 */
fun scope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): AndroidScope {
    return AndroidScope(dispatcher = dispatcher).launch(block)
}

fun LifecycleOwner.scopeLife(
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
) = AndroidScope(this, lifeEvent, dispatcher).launch(block)

fun Fragment.scopeLife(
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
) = AndroidScope(this, lifeEvent, dispatcher).launch(block)
//</editor-fold>
