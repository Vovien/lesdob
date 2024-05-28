package com.jmbon.middleware.bean

import androidx.annotation.Keep

/**
 * @author : leimg
 * time   : 2021/4/19
 * desc   :话题beans
 * version: 1.0
 *
 */
@Keep
data class TopicTypeData(
    var categorys: MutableList<TopicType>
)