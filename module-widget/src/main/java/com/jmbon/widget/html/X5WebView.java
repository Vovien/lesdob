package com.jmbon.widget.html;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class X5WebView extends Html5WebView {
    public X5WebView(Context context) {
        super(context);
        View view = getView();
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);
        view.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public X5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollBy(int x, int y) {
        getView().scrollBy(x, y);
    }

    public void scroll(int y) {
        getView().scrollBy(0, y);
    }

}
