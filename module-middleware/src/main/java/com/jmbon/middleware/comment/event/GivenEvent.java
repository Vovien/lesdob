package com.jmbon.middleware.comment.event;


import com.jmbon.middleware.comment.bean.CommentList;

import java.util.List;

public class GivenEvent {
    public int itemId;
    public int answerId;
    public List<CommentList.Comment> comment;
    public int mainComments;
    public int answerPositionInActivity;

    public GivenEvent(int itemId, int answerId, List<CommentList.Comment> comment, int mainComments) {
        this.comment = comment;
        this.itemId = itemId;
        this.answerId = answerId;
        this.mainComments = mainComments;

    }

    public GivenEvent(int itemId, int answerId, List<CommentList.Comment> comment, int mainComments, int answerPositionInActivity) {
        this.itemId = itemId;
        this.answerId = answerId;
        this.comment = comment;
        this.mainComments = mainComments;
        this.answerPositionInActivity = answerPositionInActivity;
    }

}
