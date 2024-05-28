package com.jmbon.middleware.comment.event;


import com.jmbon.middleware.comment.bean.CommentList;

public class CommentEvent {
    public CommentList commentList;
    public int topPosition;
    public boolean isSubComment;
    public int originId;
    public int topAnswerId;
    public String clientId;

    public CommentEvent(CommentList commentList, int originId, int topAnswerId, int topPosition, boolean isSubComment,String clientId) {
        this.commentList = commentList;
        this.topPosition = topPosition;
        this.isSubComment = isSubComment;
        this.originId = originId;
        this.topAnswerId = topAnswerId;
        this.clientId = clientId;
    }
}
