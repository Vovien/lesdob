package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

import com.jmbon.middleware.bean.User;

/**
 * 打赏新增
 */
@Keep
public class RewardAddEvent {

    private User user = null;
    private int itemId = 0;

    public RewardAddEvent(int itemId, User user) {
        this.user = user;
        this.itemId = itemId;
    }

    public User getUser() {
        return user;
    }

    public int getItemId() {
        return itemId;
    }
}
