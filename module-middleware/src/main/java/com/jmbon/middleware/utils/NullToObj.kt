package com.jmbon.middleware.utils

import com.apkdv.mvvmfast.utils.GsonUtil
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.jmbon.middleware.bean.Question
import java.lang.reflect.Type

class NullToQuestion : JsonDeserializer<Question> {
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?,
    ): Question {
        return if (json == null || json.toString()
                .isNotEmpty() || json.toString() == "null" || json.toString() == "\"\""
        ) {
            Question()
        } else
            GsonUtil.gson().fromJson(json.toString(), Question::class.java)

    }
}