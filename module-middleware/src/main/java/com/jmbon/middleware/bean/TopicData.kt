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
data class TopicData(
    var topics: MutableList<TopicBean>, var total: Int, var page_count: Int
)