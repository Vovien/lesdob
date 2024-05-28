package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpDefaultUser {

    @Domain(name = "DefaultUser", className = "DefaultUser")
    const val BASE_USER_URL =  "${BuildConfig.BASE_USER_URL}"

    fun get(url: String): RxHttpNoBodyParam {
        return RxDefaultUserHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxDefaultUserHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxDefaultUserHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxDefaultUserHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxDefaultUserHttp.postJsonArray(url)
    }
}