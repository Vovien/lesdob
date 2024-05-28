package com.jmbon.middleware.bean.event

import androidx.annotation.Keep


/**
 * @author : leimg
 * time   : 2021/9/7
 * desc   :群解散
 * version: 1.0
 */
@Keep
data class GroupDeleteEvent(var groupId: String, var number: String)
