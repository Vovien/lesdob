package com.jmbon.middleware.bean.event;

public class SearchTotalsEvent {
    public String pageType;
    public int total;

    public SearchTotalsEvent(String pageType, int total) {
        this.pageType = pageType;
        this.total = total;
    }
}
