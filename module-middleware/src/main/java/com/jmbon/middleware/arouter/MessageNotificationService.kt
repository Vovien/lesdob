package com.jmbon.middleware.arouter

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.template.IProvider
import com.jmbon.middleware.bean.User

interface MessageNotificationService : IProvider {
    fun startCreateGroupNotification(
        context: Context, seq: Int, circleId: Int, groupId: Int, title: String, message: String
    )
    fun startCreateGroupSystemNotification(
        context: Context, seq: Int,  groupNumber: String, title: String, message: String
    )

    fun startCreateChatNotification(
        context: Context, user: User, message: String
    )
}