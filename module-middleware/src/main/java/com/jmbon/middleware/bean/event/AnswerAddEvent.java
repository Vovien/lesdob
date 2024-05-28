package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

/**
 * 新增问题回答完成EventBus事件
 */
@Keep
public class AnswerAddEvent {

    private int questionId = 0;
    private int answerId = 0;

    public AnswerAddEvent(int questionId,int answerId) {
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getAnswerId() {
        return answerId;
    }
}
