package com.jmbon.widget.html;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author : leimg
 * time   : 2021/4/29
 * desc   :
 * version: 1.0
 */
public class NoScrollWebview extends Html5WebView {
    public NoScrollWebview(Context context) {
        super(context);
    }

    public NoScrollWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
