package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpV3 {
    @Domain(name = "V3", className = "V3")
    const val BASE_URL_V2 = "${BuildConfig.BASE_URL}app/v3/"
    fun get(url: String): RxHttpNoBodyParam {
        return RxV3Http.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxV3Http.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxV3Http.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxV3Http.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxV3Http.postJsonArray(url)
    }
}