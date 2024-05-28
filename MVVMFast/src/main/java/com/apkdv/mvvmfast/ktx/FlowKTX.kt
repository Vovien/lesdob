package com.apkdv.mvvmfast.ktx

import com.apkdv.mvvmfast.network.exception.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


fun <T> Flow<T>.netCatch(block: ApiException.() -> Unit) = catch { cause ->
    block(
        if (cause is ApiException)
            cause
        else {
            ApiException.handleException(cause)
        }
    )
}

/**
 * next方法，只要调用了next方法就代表捕获异常和回调数据，
 * 这样有异常的那next方法中的catch方法就已经捕获了，
 * 其实就是还是用flow的catch捕获就行了，
 * 因为这时候catch是在next方法前面调用的会最先捕获到异常。
 * 调用的时候只是collect变成了next方法，变成如下这样了
 */
suspend fun <T> Flow<T>.next(block: suspend T.() -> Unit): Unit = netCatch { }.collect {
    block(it)
}

fun <T> Flow<T>.nextEach(block: suspend T.() -> Unit): Flow<T> = netCatch { }.onEach {
    block(it)
}