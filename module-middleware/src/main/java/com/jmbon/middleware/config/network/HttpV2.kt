package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.RxHttpBodyParam
import rxhttp.wrapper.param.RxHttpFormParam
import rxhttp.wrapper.param.RxHttpJsonArrayParam
import rxhttp.wrapper.param.RxHttpJsonParam
import rxhttp.wrapper.param.RxHttpNoBodyParam
import rxhttp.wrapper.param.RxV2Http

object HttpV2 {
    @Domain(name = "V2", className = "V2")
    const val BASE_URL_V2 = "${BuildConfig.BASE_URL}app/v2/"
    fun get(url: String): RxHttpNoBodyParam {
        return RxV2Http.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxV2Http.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxV2Http.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxV2Http.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxV2Http.postJsonArray(url)
    }
}