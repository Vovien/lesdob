package com.jmbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.widget.NestedScrollView;

public class CanScrollNestedScrollView  extends NestedScrollView {

    public static final int MAX_SCROLL_FACTOR = 1;
    boolean isAutoScrolling;

    public CanScrollNestedScrollView(Context context) {
        super(context);
    }

    public CanScrollNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanScrollNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollTo(int x, int y) {
        isAutoScrolling = true;
        super.scrollTo(x, y);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isAutoScrolling)
            return super.onInterceptTouchEvent(event);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAutoScrolling)
            return super.onTouchEvent(event);
        return false;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (isAutoScrolling) {
            if (Math.abs(y - oldY) < MAX_SCROLL_FACTOR  || y >= getMeasuredHeight() || y == 0
                    || Math.abs(x - oldX) < MAX_SCROLL_FACTOR || x >= getMeasuredWidth() || x == 0) {
                isAutoScrolling = false;
            }
        }
    }

    public void setAutoScrolling(boolean autoScrolling) {
        isAutoScrolling = autoScrolling;
    }
}