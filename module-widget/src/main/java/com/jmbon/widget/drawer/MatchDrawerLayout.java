package com.jmbon.widget.drawer;

import android.content.Context;
import android.util.AttributeSet;

import androidx.drawerlayout.widget.DrawerLayout;

public class MatchDrawerLayout extends DrawerLayout {
    public MatchDrawerLayout(Context context) {
        super(context);
    }

    public MatchDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
 