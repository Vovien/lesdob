package com.jmbon.middleware.config.network

import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.jmbon.middleware.config.Constant.APP_NAME
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

/******************************************************************************
 * Description: 请求签名
 *
 * Author: jhg
 *
 * Date: 2023/10/13
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class SignInterceptor: Interceptor {

    private val KEY = "910c20b24d6bce1807aae5cea503fc90"

    override fun intercept(chain: Interceptor.Chain): Response {
        val appKey = APP_NAME
        val timestamp = "${System.currentTimeMillis() / 1000}"
        val newUrl = chain.request().url.newBuilder()
            .addQueryParameter("app_key", "$appKey")
            .addQueryParameter("timestamp", "$timestamp")
            .addQueryParameter("sign", getSign(appKey, timestamp))
            .build()
        val newRequest = chain.request().newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

    private fun getSign(appKey: String, timestamp: String): String {
        val content = "app_key=${appKey}&timestamp=${timestamp}"
        val firstSign = EncryptUtils.encryptHmacSHA256ToString(content, KEY)
        return EncodeUtils.base64Encode(firstSign.lowercase()).toString(Charset.forName("UTF-8"))
    }
}