package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.base.BaseResult
import com.apkdv.mvvmfast.network.config.HttpCode
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.apkdv.mvvmfast.network.exception.ApiException
import com.blankj.utilcode.util.GsonUtils
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.entity.ParameterizedTypeImpl
import rxhttp.wrapper.parse.TypeParser
import java.lang.reflect.Type


/**
 *
 * 如果使用协程发送请求，wrappers属性可不设置，设置了也无效
 */
@Parser(name = "Response")
open class ResponseParser<T> : TypeParser<T> {
    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象，如：List<Student>
     *
     * 用法:
     * Java: .asParser(new ResponseParser<List<Student>>(){})
     * Kotlin: .asParser(object : ResponseParser<List<Student>>() {})
     *
     * 注：此构造方法一定要用protected关键字修饰，否则调用此构造方法将拿不到泛型类型
     */
    protected constructor() : super()

    private val defaultMsg = "数据请求异常"

    /**
     * 此构造方法仅适用于不带泛型的Class对象，如: Student.class
     *
     * 用法
     * Java: .asParser(new ResponseParser<>(Student.class))   或者  .asResponse(Student.class)
     * Kotlin: .asParser(ResponseParser(Student::class.java)) 或者  .asResponse<Student>()
     */
    constructor(type: Type) : super(type)

    @Throws(ApiException::class)
    override fun onParse(response: okhttp3.Response): T {
        try {
            if (response.code == 200) {
                val result = response.body?.string()
                if (result?.isNotEmpty() == true) {
                    val ty: Type = ParameterizedTypeImpl(
                        BaseResult::class.java,
                        types[0]
                    ) // 传泛型的Type和我们想要的外层类的Type来组装我们想要的类型
                    val data: BaseResult<T> = GsonUtils.fromJson(result, ty)
                    var t = data.getData() //获取data字段
                    // 成功状态
                    if (data.isSuccess() && t == null) {
                        /*
                         * 考虑到有些时候服务端会返回：{"errorCode":0,"errorMsg":"关注成功"}  类似没有data的数据
                         * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
                         * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
                         */
                        when {
                            types[0] === String::class.java -> @Suppress("UNCHECKED_CAST")
                            t = "" as T
                            types[0] === EmptyData::class.java -> @Suppress("UNCHECKED_CAST")
                            t = EmptyData() as T
                            else -> {
                                // 如果以上两种都不是，尝试将 json 直接解析为 T
                                val newData: T = GsonUtils.fromJson(result, types[0])
                                t = newData
                            }
                        }
                    }
                    if (!data.isSuccess()) { //code不等于200，说明数据不正确，抛出异常
                        throw netThrow(
                            data.getCode(),
                            data.getMsg(),
                            result
                        )
                    }
                    return t
                } else {
                    throw netThrow(HttpCode.HTTP.NONE_CONTENT.toLong(), defaultMsg, result)
                }
            } else {
                throw netThrow(response.code.toLong(), defaultMsg, response.body?.string())
            }
        } catch (e: Exception) {
            if (e is ApiException) {
                throw e
            } else
                throw netThrow(
                    HttpCode.HTTP.INNER_EXCEPTION.toLong(),
                    defaultMsg,
                    e.message
                )
        }
    }

    private fun netThrow(code: Long, msg: String, data: String?): ApiException {
        return ApiException(code, msg, data)
    }
}