package com.jmbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * ChenboCui 竖向的Viewpager
 */
public class VerticalViewPager extends ViewPager {
    private boolean isPagingEnabled = true;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, new DefaultTransformer());
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        event.setLocation(swappedX, swappedY);

        return event;
    }


    private float mX;
    private float mY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(!this.isPagingEnabled){
            return false;
        }
        //If not intercept, touch event should not be swapped.
        swapTouchEvent(event);
        return  super.onInterceptTouchEvent(swapTouchEvent(event));
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!this.isPagingEnabled){
            return false;
        }
        return super.onTouchEvent(swapTouchEvent(ev));
    }

    public class DefaultTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            float alpha = 0;
            if (0 <= position && position <= 1) {
                alpha = 1 - position;
            } else if (-1 < position && position < 0) {
                alpha = position + 1;
            }
            view.setAlpha(alpha); //切换渐变效果
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }
}