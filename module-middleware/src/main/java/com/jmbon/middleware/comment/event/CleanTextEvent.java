package com.jmbon.middleware.comment.event;

public class CleanTextEvent {
    private boolean clean;
    private String content;
    private final boolean isInDialog;
    public int answerPositionInActivity;

    public CleanTextEvent(boolean clean, String content) {
        this.clean = clean;
        this.content = content;
        this.isInDialog = false;
    }

    public CleanTextEvent(boolean clean, String content, boolean isInDialog) {
        this.clean = clean;
        this.content = content;
        this.isInDialog = isInDialog;
    }


    public CleanTextEvent(boolean clean, boolean isInDialog) {
        this.clean = clean;
        this.isInDialog = isInDialog;
    }

    public CleanTextEvent(String content, boolean isInDialog) {
        this.content = content;
        this.isInDialog = isInDialog;
    }

    public boolean isClean() {
        return clean;
    }

    public String getContent() {
        return content;
    }

    public boolean isInDialog() {
        return isInDialog;
    }
}
