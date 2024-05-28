package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

@Keep
public class FocusChangedEvent {
    public static final String USER = "user";
    public static final String QUESTION = "question";
    public static final String VIDEO = "video";
    public static final String COLUMN = "column";
    public static final String TOPIC = "topic";
    public static final String EMPTY = "empty";
    public static final String GIVE_VIDEO = "give_video";

    public String type;
    public boolean isFocus;
    public int id;

    public FocusChangedEvent(String type, boolean isFocus, int id) {
        this.type = type;
        this.isFocus = isFocus;
        this.id = id;
    }
    public static FocusChangedEvent emptyEvent() {
        return new FocusChangedEvent(EMPTY, false, 0);
    }


    public static FocusChangedEvent focusUser(int id, boolean isFocus) {
        return new FocusChangedEvent(USER, isFocus, id);
    }

    public static FocusChangedEvent focusQuestion(int id, boolean isFocus) {
        return new FocusChangedEvent(QUESTION, isFocus, id);
    }

    public static FocusChangedEvent focusColumn(int id, boolean isFocus) {
        return new FocusChangedEvent(COLUMN, isFocus, id);
    }

    public static FocusChangedEvent focusTopic(int id, boolean isFocus) {
        return new FocusChangedEvent(TOPIC, isFocus, id);
    }
    public static FocusChangedEvent collectionVideo(int id, boolean isCollection) {
        return new FocusChangedEvent(VIDEO, isCollection, id);
    }

    public static FocusChangedEvent giveVideo(int id, boolean isGive) {
        return new FocusChangedEvent(GIVE_VIDEO, isGive, id);
    }

    public  boolean isTopic(){
        return TOPIC.equals(type);
    }
    public  boolean isQuestion(){
        return QUESTION.equals(type);
    }
    public  boolean isColumn(){
        return COLUMN.equals(type);
    }
    public  boolean isUser(){
        return USER.equals(type);
    }
    public  boolean isVideo(){
        return VIDEO.equals(type);
    }
    @Override
    public String toString() {
        return "FocusChangedEvent{" +
                "type='" + type + '\'' +
                ", isFocus=" + isFocus +
                ", id=" + id +
                '}';
    }
}
