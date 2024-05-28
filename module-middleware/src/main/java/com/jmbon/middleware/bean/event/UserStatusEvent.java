package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

/**
 * 用户怀孕状态修改事件
 */
@Keep
public class UserStatusEvent {

    int type = 0;//1 表示来自mainfragment

    public UserStatusEvent() {
    }

    public UserStatusEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
