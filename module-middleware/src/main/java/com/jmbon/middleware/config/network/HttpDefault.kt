package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.RxDefaultHttp
import rxhttp.wrapper.param.RxHttpBodyParam
import rxhttp.wrapper.param.RxHttpFormParam
import rxhttp.wrapper.param.RxHttpJsonArrayParam
import rxhttp.wrapper.param.RxHttpJsonParam
import rxhttp.wrapper.param.RxHttpNoBodyParam

object HttpDefault {

    @Domain(name = "Default", className = "Default")
    const val BASE_URL =  "${BuildConfig.BASE_URL}"

    fun get(url: String): RxHttpNoBodyParam {
        return RxDefaultHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxDefaultHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxDefaultHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxDefaultHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxDefaultHttp.postJsonArray(url)
    }
}