package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpV4 {
    @Domain(name = "V4", className = "V4")
    const val BASE_URL_V4 = "${BuildConfig.BASE_URL}app/v4/"
    fun get(url: String): RxHttpNoBodyParam {
        return RxV4Http.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxV4Http.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxV4Http.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxV4Http.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxV4Http.postJsonArray(url)
    }
}