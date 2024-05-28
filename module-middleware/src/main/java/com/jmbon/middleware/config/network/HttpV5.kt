package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpV5 {
    @Domain(name = "V5", className = "V5")
    const val BASE_URL_V5 = "${BuildConfig.BASE_URL}app/v5/"
    fun get(url: String): RxHttpNoBodyParam {
        return RxV5Http.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxV5Http.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxV5Http.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxV5Http.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxV5Http.postJsonArray(url)
    }
}