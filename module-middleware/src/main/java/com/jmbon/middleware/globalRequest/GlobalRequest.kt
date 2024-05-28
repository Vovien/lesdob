package com.jmbon.middleware.globalRequest

import com.jmbon.middleware.api.CommentApi
import com.jmbon.middleware.comment.bean.GivenCount
import kotlinx.coroutines.flow.flow

object GlobalRequest {

    val TYPE_LIKE = true
    val TYPE_COLLECT = false

    /**
     * status 操作类型,allow或ban，即开启或取消，只能传这两个参数
     * // 问题回答点赞或者收藏接口
     * type 点赞或者收藏，given或collect  type true give
     * answerId 对应问题ID
     */
    fun answerGivenCollect(answerId: Int, type: Boolean, status: Boolean) =
        flow<GivenCount> {
            CommentApi.answerGivenCollect(
                answerId,
                if (type) "given" else "collect",
                if (status) "allow" else "ban",
            )
        }

}