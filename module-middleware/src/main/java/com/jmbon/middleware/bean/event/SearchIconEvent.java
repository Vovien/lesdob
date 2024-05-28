package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

@Keep
public class SearchIconEvent {
    private final boolean showIcon;
    private final boolean pageType;

    public SearchIconEvent(boolean showIcon, boolean isCircle) {
        this.showIcon = showIcon;
        this.pageType = isCircle;
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public boolean isCircle() {
        return pageType;
    }
}
