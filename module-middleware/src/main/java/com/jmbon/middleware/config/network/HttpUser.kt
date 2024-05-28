package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpUser {
    @Domain(name = "user", className = "user")
    const val BASE_USER_URL =  "${BuildConfig.BASE_USER_URL}app/"

    fun get(url: String): RxHttpNoBodyParam {
        return RxuserHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxuserHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxuserHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxuserHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxuserHttp.postJsonArray(url)
    }
}