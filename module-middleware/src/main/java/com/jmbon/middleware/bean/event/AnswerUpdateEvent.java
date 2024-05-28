package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

/**
 * 问题修改完成EventBus事件
 */
@Keep
public class AnswerUpdateEvent {

    public int answerId = 0;
    public int checkId = 0;

    public AnswerUpdateEvent() {
    }

    public AnswerUpdateEvent(int answerId) {
        this.answerId = answerId;
    }

    public AnswerUpdateEvent(int answerId, int checkId) {
        this.answerId = answerId;
        this.checkId = checkId;
    }

}
