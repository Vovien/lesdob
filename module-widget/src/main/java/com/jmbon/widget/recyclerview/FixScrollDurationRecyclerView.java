package com.jmbon.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;


import java.lang.reflect.Field;

/**
 * 修改滚动速度的recyclerview
 */
public class FixScrollDurationRecyclerView extends RecyclerView {
    private TopPositionLayoutManager.TopSmoothScroller mSmoothScroller;

    private int duration = 300;

    public FixScrollDurationRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public FixScrollDurationRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FixScrollDurationRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mSmoothScroller = new TopPositionLayoutManager.TopSmoothScroller(context);
    }

    public void setScrollSpeed(int duration, Interpolator interpolator) {
        try {
            Field mViewFlinger = RecyclerView.class.getDeclaredField("mViewFlinger");
            mViewFlinger.setAccessible(true);
            Class viewFlingerClass = Class.forName("androidx.recyclerview.widget" +
                    ".RecyclerView$ViewFlinger");
            //不过如果是AndroidX的recyclerview就需要把mScroller改成mOverScroller才可以正常用
            // Field mScroller = viewFlingerClass.getDeclaredField("mScroller");
            Field mScroller = viewFlingerClass.getDeclaredField("mOverScroller");
            mScroller.setAccessible(true);
            Field mInterpolator = viewFlingerClass.getDeclaredField("mInterpolator");
            mInterpolator.setAccessible(true);
            Field mDecelerateInterpolator = LinearSmoothScroller.class.getDeclaredField(
                    "mDecelerateInterpolator");
            mDecelerateInterpolator.setAccessible(true);
            mInterpolator.set(mViewFlinger.get(this),
                    mDecelerateInterpolator.get(mSmoothScroller));
            if (duration >= 0) {
                FixScrollDurationOverScroller overScroller = new FixScrollDurationOverScroller(getContext(), interpolator);
                overScroller.setScrollDuration(duration);
                mScroller.set(mViewFlinger.get(this), overScroller);
            } else {
                OverScroller overScroller = new OverScroller(getContext(), interpolator);
                mScroller.set(mViewFlinger.get(this), overScroller);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
 static final Interpolator sQuinticInterpolator2 = new LinearInterpolator();

    public void scrollToPositionWithDuration(int position, int duration) {
        mSmoothScroller = new TopPositionLayoutManager.TopSmoothScroller(getContext());
        mSmoothScroller.setTargetPosition(position);
        setScrollSpeed(duration, sQuinticInterpolator);
        getLayoutManager().startSmoothScroll(mSmoothScroller);
    }

    public void scrollToPositionWithDuration(int position) {
        mSmoothScroller = new TopPositionLayoutManager.TopSmoothScroller(getContext());
        mSmoothScroller.setTargetPosition(position);
        setScrollSpeed(duration, sQuinticInterpolator);
        getLayoutManager().startSmoothScroll(mSmoothScroller);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
