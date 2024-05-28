package com.jmbon.middleware.arouter

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import java.util.*

object LoginCallBack {
    /**
     * 压栈：push()；
     * 弹出：pop()；
     * 取栈peek()。
     */
    val queue by lazy { Stack<Runnable>() }

    var mPostcard: Postcard? = null
    var interceptorCallback: InterceptorCallback? = null
}