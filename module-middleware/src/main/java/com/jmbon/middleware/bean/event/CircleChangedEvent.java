package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

@Keep
public class CircleChangedEvent {
    public static final String JOIN = "Join"; // 加入退出圈子
    public static final String Dissolve = "dissolve"; // 解散圈子
    public static final String CREATE = "Create"; //
    public static final String ADDNEW = "add_new";
    public static final String DELETE = "delete";
    public static final String TOP_CHANGE = "top_change";//置顶状态修改
    public static final String TOP_MULTI_CHANGE = "top_multi_change";//置顶状态修改
    public static final String ADD_TAG = "add_tag";//加精标签修改
    public static final String ADD_MULTI_TAG = "add_multi_tag";//多个加精标签修改
    public static final String EMPTY = "empty";
    public static final String APPEAL = "appeal";
    public static final String COLLECT = "collect"; //收藏
    public static final String LOCATION = "location"; //发送当前的位置

    public String type;
    public boolean isJoin;
    public boolean appeal;
    public String id;

    public CircleChangedEvent(String type, boolean isJoin, String id) {
        this.type = type;
        this.isJoin = isJoin;
        this.id = id;
    }

    public CircleChangedEvent(String type, boolean isJoin, boolean appeal, String id) {
        this.type = type;
        this.isJoin = isJoin;
        this.appeal = appeal;
        this.id = id;
    }

    public static CircleChangedEvent emptyEvent() {
        return new CircleChangedEvent(EMPTY, false, "");
    }

    public static CircleChangedEvent createCircle() {
        return new CircleChangedEvent(CREATE, false, "");
    }

    public static CircleChangedEvent joinCircle(String id) {
        return new CircleChangedEvent(JOIN, true, id);
    }

    public static CircleChangedEvent exitCircle(String id) {
        return new CircleChangedEvent(JOIN, false, id);
    }
    public static CircleChangedEvent dissolveCircle(String id) {
        return new CircleChangedEvent(Dissolve, false, id);
    }

    public static CircleChangedEvent appealSuccess(String id) {
        return new CircleChangedEvent(APPEAL, false, true, id);
    }

    public static CircleChangedEvent topChange(boolean isTop, String id) {
        return new CircleChangedEvent(TOP_CHANGE, false, isTop, id);
    }

    public static CircleChangedEvent topMultiChange(boolean isTop, String ids) {
        return new CircleChangedEvent(TOP_MULTI_CHANGE, false, isTop, ids);
    }

    public static CircleChangedEvent addTag(boolean isAdd, String id) {
        return new CircleChangedEvent(ADD_TAG, false, isAdd, id);
    }


    public static CircleChangedEvent addMultiTag(boolean isAdd, String ids) {
        return new CircleChangedEvent(ADD_MULTI_TAG, false, isAdd, ids);
    }

    public CircleChangedEvent(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public static CircleChangedEvent deleteItem(String id) {
        return new CircleChangedEvent(DELETE, id);
    }

    public CircleChangedEvent(String type) {
        this.type = type;
    }

    public static CircleChangedEvent addItem() {
        return new CircleChangedEvent(ADDNEW);
    }

    public static CircleChangedEvent collect(String id, boolean isCollect) {
        return new CircleChangedEvent(COLLECT, isCollect, id);
    }


    public boolean isJoinAction() {
        return JOIN.equals(type);
    }
    public boolean isDissolveAction() {
        return Dissolve.equals(type);
    }

    public boolean isDelete() {
        return DELETE.equals(type);
    }

    public boolean isAdd() {
        return ADDNEW.equals(type);
    }

    public boolean isAppeal() {
        return APPEAL.equals(type);
    }
}
