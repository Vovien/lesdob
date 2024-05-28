package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.param.*

object Http {
    @DefaultDomain
    const val BASE_URL =  "${BuildConfig.BASE_URL}v1/"

    fun get(url: String): RxHttpNoBodyParam {
        return RxHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxHttp.postJsonArray(url)
    }
}