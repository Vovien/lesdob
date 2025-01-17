package com.jmbon.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

public class NoAnimationViewPager extends ViewPager {


    public NoAnimationViewPager(Context context) {
        super(context);

    }

    public NoAnimationViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);

    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        try {
            //去除页面切换时的滑动翻页效果
            super.setCurrentItem(item, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
