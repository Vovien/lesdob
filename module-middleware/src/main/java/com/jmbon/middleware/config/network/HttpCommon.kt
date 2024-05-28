package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.RxCommonHttp
import rxhttp.wrapper.param.RxHttpBodyParam
import rxhttp.wrapper.param.RxHttpFormParam
import rxhttp.wrapper.param.RxHttpJsonArrayParam
import rxhttp.wrapper.param.RxHttpJsonParam
import rxhttp.wrapper.param.RxHttpNoBodyParam

object HttpCommon {

    @Domain(name = "Common", className = "Common")
    const val BASE_URL =  "${BuildConfig.BASE_COMMON_URL}"

    fun get(url: String): RxHttpNoBodyParam {
        return RxCommonHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxCommonHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxCommonHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxCommonHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxCommonHttp.postJsonArray(url)
    }
}