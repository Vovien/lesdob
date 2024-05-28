package com.jmbon.minitools.advisory.api

import com.jmbon.middleware.config.network.Http
import com.jmbon.minitools.advisory.bean.*
import rxhttp.wrapper.param.toResponse

/******************************************************************************
 * Description: 咨询求出相关的接口
 *
 * Author: jhg
 *
 * Date: 2023/5/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object AdvisoryApi {

    /**
     * 获取咨询表单列表
     */
    suspend fun getAdvisoryFormList(): AdvisoryFormBean {
        return Http.post("app/v12/consult/get_chat_form_data")
            .toResponse<AdvisoryFormBean>().await()
    }

    /**
     * 获取咨询放弃原因
     */
    suspend fun getAdvisoryAbandonReason(): AdvisoryAbandonReasonBean {
        return Http.post("app/v12/consult/get_reason_list")
            .toResponse<AdvisoryAbandonReasonBean>().await()
    }

    /**
     * 提交咨询放弃原因
     * @param reasonIds: 需要提交的咨询原因id
     */
    suspend fun submitAdvisoryAbandonReason(reasonIds: String): String {
        return Http.post("app/v12/consult/sumit_reason")
            .add("reason_ids", reasonIds)
            .toResponse<String>().await()
    }

    /**
     * 创建咨询
     * @param isOwner: 咨询是否为本人
     * @param relationShip: 和本人的关系
     * @param age: 咨询者的年龄
     * @param gender: 咨询是否为本人
     * @param hasFertilityHistory: 咨询是否为本人
     * @param reportUrl: 咨询是否为本人
     * @param question: 咨询是否为本人
     */
    suspend fun createAdvisory(
        isOwner: Int,
        relationShip: Int,
        age: Int,
        gender: Int,
        hasFertilityHistory: Int,
        reportUrl: String,
        question: String
    ): SubmitAdvisoryResultBean {
        return Http.post("app/v12/consult/create")
            .add("is_self", isOwner)
            .add("relationship_me", relationShip)
            .add("age", age)
            .add("gender", gender)
            .add("is_birth_history", hasFertilityHistory)
            .add("report_img", reportUrl)
            .add("title", question)
            .toResponse<SubmitAdvisoryResultBean>().await()
    }

    /**
     * 获取咨询回复
     * @param content: 咨询的内容
     * @param questionId: 问题id
     * @param dialogId: 对话id
     */
    suspend fun getAdvisoryReply(content: String, questionId: Int = 0, dialogId: Int = 0): AdvisoryReplyBean {
        return Http.post("ai_dialog_send")
            .readTimeout(5 * 60 * 1000)
            .add("content", content)
            .add("question_id", questionId)
            .add("dialog_id", dialogId)
            .toResponse<AdvisoryReplyBean>().await()
    }

    /**
     * 更新咨询状态
     * @param advisoryId: 咨询id
     * @param status: 咨询状态
     * @param byClick: 是否为主动点击未解决, 0=不是, 1=是
     * @param reasonType: 结束原因: 0=正常, 1=时间结束, 2=回复次数达上限, 3=AI异常结束
     */
    suspend fun updateAdvisoryStatus(
        advisoryId: Int,
        status: Int,
        byClick: Int = 0,
        reasonType: Int
    ) {
        return Http.post("app/v12/consult/set_dialogue_status")
            .add("consult_id", advisoryId)
            .add("status", status)
            .add("is_active", byClick)
            .add("end_type", reasonType)
            .toResponse<Unit>().await()
    }

    /**
     * 获取咨询会话历史记录
     * @param conversationId: 会话id
     */
    suspend fun getAdvisoryConversationHistory(conversationId: Int) =
        Http.post("ai_dialog_content")
            .add("dialog_id", conversationId)
            .toResponse<AdvisoryConversationHistoryBean>().await()

    /**
     * 获取默认会话记录
     * @param questionId: 会话id
     */
    suspend fun getDefaultConversationHistory(questionId: Int) =
        Http.post("ai_question_default_dialog")
            .add("question_id", questionId)
            .toResponse<AdvisoryConversationHistoryBean>().await()
}