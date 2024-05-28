package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

/**
 * 问题发布完成EventBus事件
 */
@Keep
public class QuestionAddEvent {

    private int questionId = 0;
    private int checkLogId = 0;

    public QuestionAddEvent(int questionId) {
        this.questionId = questionId;
    }

    public QuestionAddEvent(int questionId, int checkLogId) {
        this.questionId = questionId;
        this.checkLogId = checkLogId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getCheckLogId() {
        return checkLogId;
    }
}
