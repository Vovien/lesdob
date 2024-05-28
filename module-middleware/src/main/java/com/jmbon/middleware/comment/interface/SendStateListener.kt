package com.jmbon.middleware.comment.`interface`

import com.jmbon.middleware.comment.event.CommentEvent

interface SendStateListener {
    fun onStart(event: CommentEvent)
    fun onSuccess(event: CommentEvent)
    fun onFail(event: CommentEvent)
}