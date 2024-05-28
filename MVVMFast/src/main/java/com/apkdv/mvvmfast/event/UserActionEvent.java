package com.apkdv.mvvmfast.event;

public class UserActionEvent {
    public static String CLEAN_LOGIN_USER = "clean_login_user";

    public String action;

    public UserActionEvent(String action) {
        this.action = action;
    }
}
