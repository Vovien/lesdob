package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

/**
 * 回答采纳完成EventBus事件
 */
@Keep
public class AnswerAcceptEvent {

    private int questionId = 0;
    private int answerId = 0;

    public AnswerAcceptEvent(int questionId, int answerId) {
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
