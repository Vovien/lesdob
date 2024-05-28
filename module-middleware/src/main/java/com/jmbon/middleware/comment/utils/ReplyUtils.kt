package com.jmbon.middleware.comment.utils

import com.jmbon.middleware.comment.event.SubCommentEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import org.greenrobot.eventbus.EventBus

object ReplyUtils {
    fun showReplyInput(event: SubCommentEvent) {
        if (Constant.isLogin)
            EventBus.getDefault()
                .postSticky(event)
        else {
            Action {
                EventBus.getDefault()
                    .postSticky(event)
            }.logInToIntercept()
        }
    }
}