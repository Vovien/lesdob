package com.apkdv.mvvmfast.ktx

import com.apkdv.mvvmfast.network.config.HttpCode
import com.apkdv.mvvmfast.network.exception.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

/**
 * 用流的方式进行网络请求
 */
fun <T> launchWithFlow(
    block: suspend () -> T,
    error: suspend (ApiException) -> Unit = {},
    complete: (Boolean) -> Unit = {}
): Flow<T> {
    return flow {
        emit(block())
    }.onCompletion { cause ->
        cause?.let { e ->
            val case = if (e is ApiException)
                e
            else {
                ApiException.handleException(e)
            }
            if (HttpCode.FRAME_WORK.REQUEST_CANCEL.toLong() != case.code)
                error(case)
        }
        complete(cause == null)
    }
}