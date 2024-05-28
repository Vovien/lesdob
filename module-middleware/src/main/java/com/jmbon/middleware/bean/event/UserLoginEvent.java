package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

/**
 * 用户登录事件
 * login true 登录
 * false 退出登录
 */
@Keep
public class UserLoginEvent {
    public boolean login;

    public UserLoginEvent(boolean login) {
        this.login = login;
    }

}
