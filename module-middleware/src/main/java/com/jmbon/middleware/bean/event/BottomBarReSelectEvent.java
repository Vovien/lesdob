package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

@Keep
public class BottomBarReSelectEvent {
    private final int type;

    public BottomBarReSelectEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
