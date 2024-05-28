package com.apkdv.mvvmfast.base

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BaseResult<T>(
    @SerializedName("code") private val code: Long,
    @SerializedName("msg", alternate = ["message"]) private val msg: String,
    @SerializedName("data", alternate = ["datas"]) private val data: T
) {
    fun getCode(): Long {
        return code
    }

    fun getMsg(): String {
        return msg
    }

    fun getData(): T {
        return data
    }

    fun isSuccess(): Boolean {
        return code.toInt() == 0 || code.toInt() == 200
    }

}