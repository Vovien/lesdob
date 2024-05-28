package com.jmbon.middleware.comment.event;


import androidx.annotation.Nullable;

import com.jmbon.middleware.comment.bean.CommentList;
import com.jmbon.middleware.config.Constant;

public class SubCommentEvent {
    public int answerId;
    public int topPosition;
    public int topAnswerId;
    public String replyName;
    public String type;
    public boolean isSubComment;
    public int hashCode;
    public int secondAnswerId;
    public int replyId;
    public CommentList.Comment.UserReceiver receiver;
    public int articlePublishedUid;
    public int questionPublishedUid;
    public int answerPublishedUid;
    public int videoPublishedUid;

    public SubCommentEvent() {
    }

    /**
     * 回复二级评论
     *
     * @param answerId
     * @param topPosition
     * @param replyName
     */
    public SubCommentEvent(int answerId, int topPosition, int topAnswerId,
                           int secondAnswerId, CommentList.Comment.UserReceiver receiver,
                           String replyName, String type, @Nullable CommentList.Comment lastComment) {
        this.answerId = answerId;
        this.topPosition = topPosition;
        this.replyName = replyName;
        this.type = type;
        this.topAnswerId = topAnswerId;
        this.replyId = answerId;
        this.isSubComment = true;
        this.secondAnswerId = secondAnswerId;
        this.receiver = receiver;
        setUid(lastComment);
    }

    private void setUid(@Nullable CommentList.Comment lastComment) {
        if (lastComment != null) {
            this.articlePublishedUid = lastComment.getArticlePublishedUid();
            this.questionPublishedUid = lastComment.getQuestionPublishedUid();
            this.answerPublishedUid = lastComment.getAnswerPublishedUid();
        }
    }

    /**
     * 回复二级评论
     *
     * @param answerId
     * @param topPosition
     * @param replyName
     */
    public SubCommentEvent(int answerId, int topPosition, int topAnswerId,
                           CommentList.Comment.UserReceiver receiver,
                           String replyName, String type, int hashCode,@Nullable CommentList.Comment lastComment) {
        this.answerId = answerId;
        this.topPosition = topPosition;
        this.replyName = replyName;
        this.type = type;
        this.topAnswerId = topAnswerId;
        this.isSubComment = true;
        this.hashCode = hashCode;
        this.replyId = answerId;
        this.secondAnswerId = answerId;
        this.receiver = receiver;
        setUid(lastComment);
    }


    /**
     * 直接回复回答
     */
    public SubCommentEvent(int answerId, String type, int hashCode,@Nullable CommentList.Comment lastComment) {
        if (type.equals(Constant.INSTANCE.getTYPE_QUESTION()))
            this.answerId = answerId;
        else
            this.answerId = 0;
        this.replyName = "";
        this.topAnswerId = answerId;
        this.secondAnswerId = 0;
        this.topPosition = 0;
        this.type = type;
        this.replyId = 0;
        this.isSubComment = false;
        this.hashCode = hashCode;
        setUid(lastComment);
    }


}
