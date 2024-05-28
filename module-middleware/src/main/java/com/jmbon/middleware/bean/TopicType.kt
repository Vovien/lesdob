package com.jmbon.middleware.bean

import androidx.annotation.Keep
import java.io.Serializable

/**
 * @author : leimg
 * time   : 2021/4/21
 * desc   : 话题分类
 * version: 1.0
 */
@Keep
data class TopicType(var id: Int, var title: String) : Serializable