package com.jmbon.middleware.bury.api

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

/******************************************************************************
 * Description: 埋点Http客户端
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
internal object BuryHttp {

    @Domain(name = "BURY", className = "Bury")
    const val BASE_URL_BURY = BuildConfig.BURY_HOST

    fun get(url: String): RxHttpNoBodyParam {
        return RxBuryHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxBuryHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxBuryHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxBuryHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxBuryHttp.postJsonArray(url)
    }
}