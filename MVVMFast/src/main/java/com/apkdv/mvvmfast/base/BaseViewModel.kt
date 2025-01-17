package com.apkdv.mvvmfast.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.event.Message
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.handleError
import com.apkdv.mvvmfast.network.config.HttpCode
import com.apkdv.mvvmfast.network.exception.ApiException
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
 *   @auther : lengyue
 *   time   : 2019/11/01
 */
open class BaseViewModel : AndroidViewModel(Utils.getApp()), LifecycleObserver {

    val defUI: UIChange by lazy { UIChange() }
    val defLayout: LayoutChange by lazy { LayoutChange() }

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    /**
     * 直接获取当前协程的 viewModelScope 域
     */
    fun viewScope() = viewModelScope

    /**
     * 用流的方式进行网络请求
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }


    /**
     * 用流的方式进行网络请求
     */
    fun <T> launchWithFlow(
        block: suspend () -> T,
        error: (ApiException) -> Unit = {},
        complete: (Boolean) -> Unit = {},
        isShowDialog: Boolean = false,
        handleError: Boolean = true
    ): Flow<T> {
        return flow {
            emit(block())
        }.onStart {
            if (isShowDialog)
                defUI.showDialog.call()
        }
            .onCompletion { cause ->
                cause?.let { e ->
                    val case = if (e is ApiException)
                        e
                    else {
                        ApiException.handleException(e)
                    }
                    if (HttpCode.FRAME_WORK.REQUEST_CANCEL.toLong() != case.code)
                        error(case)
                    if (handleError)
                        case.handleError(defLayout)
                }
                complete(cause == null)
                if (isShowDialog)
                    defUI.dismissDialog.call()
            }
    }

    /**
     *  不过滤请求结果
     * @param block 请求体
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun launchGo(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ApiException) -> Unit = {},
        complete: suspend CoroutineScope.() -> Unit = {},
        isShowDialog: Boolean = false,
        handleError: Boolean = true,
    ) {
        if (isShowDialog) defUI.showDialog.call()
        launchUI {
            handleException(
                withContext(Dispatchers.IO) { block },
                { error(it) },
                {
                    if (isShowDialog)
                        defUI.dismissDialog.call()
                    complete()
                }, handleError
            )
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launchOnlyResult(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        error: (ApiException) -> Unit = {},
        complete: () -> Unit = {},
        isShowDialog: Boolean = false,
        handleError: Boolean = true,
    ): Job {
        if (isShowDialog) defUI.showDialog.call()
        return launchUI {
            handleException(
                { withContext(Dispatchers.IO) { block() } },
                { res ->
                    success(res)
                },
                {
                    error(it)
                    LogUtils.e(it.toString())
                },
                {
                    if (isShowDialog)
                        defUI.dismissDialog.call()
                    complete()
                }, handleError
            )
        }
    }

    /**
     * 请求结果过滤
     */
    private suspend fun <T> executeResponse(
        response: T,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (response != null) success(response)
        }
    }

    /**
     * 异常统一处理
     */
    private suspend fun <T> handleException(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(ApiException) -> Unit,
        complete: suspend CoroutineScope.() -> Unit,
        handleError: Boolean = false
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: Throwable) {
                topHandleError(e, error, handleError)
            } finally {
                complete()
            }
        }
    }


    private suspend fun topHandleError(
        e: Throwable, error: suspend CoroutineScope.(ApiException) -> Unit,
        handleError: Boolean
    ) {
        coroutineScope {
            val exp = if (e is ApiException) {
                e
            } else {
                ApiException.handleException(e)
            }
            if (HttpCode.FRAME_WORK.REQUEST_CANCEL.toLong() != exp.code)
                error(exp)
            if (handleError)
                exp.handleError(defLayout)
        }
    }


    /**
     * 异常统一处理
     */
    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ApiException) -> Unit,
        complete: suspend CoroutineScope.() -> Unit,
        handleError: Boolean
    ) {
        coroutineScope {
            try {
                block()
            } catch (e: Throwable) {
                topHandleError(e, error, handleError)
            } finally {
                complete()
            }
        }
    }


    /**
     * UI事件
     */
    inner class UIChange {
        val showDialog by lazy { SingleLiveEvent<String>() }
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
        val toastEvent by lazy { SingleLiveEvent<String>() }
        val msgEvent by lazy { SingleLiveEvent<Message>() }
    }

    /**
     * UI事件
     */
    inner class LayoutChange {
        // 显示数据
        val showEmpty by lazy { SingleLiveEvent<Void>() }
        val showError by lazy { SingleLiveEvent<Void>() }
        val showErrorMsg by lazy { SingleLiveEvent<String>() }
        val showLoading by lazy { SingleLiveEvent<Void>() }
        val showNoNet by lazy { SingleLiveEvent<Void>() }
        val showContent by lazy { SingleLiveEvent<Void>() }
    }
}