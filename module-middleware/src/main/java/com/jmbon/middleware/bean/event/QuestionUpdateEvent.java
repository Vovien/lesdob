package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

/**
 * 问题修改完成EventBus事件
 */
@Keep
public class QuestionUpdateEvent {

    private int questionId = 0;

    public QuestionUpdateEvent(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }
}
