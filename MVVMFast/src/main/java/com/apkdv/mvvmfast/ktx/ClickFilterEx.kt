package com.apkdv.mvvmfast.ktx

import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
fun EditText.textChangeFlow(): Flow<String> = callbackFlow {

    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { trySend(it.toString()) }
        }
    }
    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) }
}


val FAST_CLICK_THRSHOLD = 300

/**
 * 将点击事件封装成 flow
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
fun View.flowClick(): Flow<View> = callbackFlow {
    setOnClickListener {
        trySend(it)
    }
    awaitClose { setOnClickListener(null) }
}

/**
 * 仿制 View点击事件产生的快速调用
 * 不用于 View 的防止双击，在延迟指定时间后没有继续收到事件，才会处理事件
 *
 */
@ExperimentalCoroutinesApi
@FlowPreview
fun <T> View.filterQuickCall(call: () -> Unit, flowCall: () -> Flow<T>, action: (T) -> Unit = {}) {
    flowClick()
        .map { withContext(Dispatchers.Main) { call.invoke() } }
        .debounce(500)
        .flowOn(Dispatchers.IO)
        .flatMapLatest { flowCall.invoke() }
        .nextEach { action.invoke(this) }
        .launchIn(MainScope())
}

fun View.isFastClick(): Boolean {
    val currentTime = SystemClock.elapsedRealtime()
    return if (currentTime >= FAST_CLICK_THRSHOLD) {
        this.triggerTime = currentTime
        false
    } else {
        true
    }
}


private var View.triggerTime: Long
    get() = getTag(12) as? Long ?: 0L
    set(value) = setTag(12, value)
