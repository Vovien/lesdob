package com.jmbon.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.donkingliang.consecutivescroller.ConsecutiveViewPager;

public class NoAnimationConsecutiveViewPager extends ConsecutiveViewPager {


    public NoAnimationConsecutiveViewPager(Context context) {
        super(context);

    }

    public NoAnimationConsecutiveViewPager(Context context, AttributeSet attrs) {
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
