package com.jmbon.widget.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.lang.reflect.Field;

/**
 * 当调用RecyclerView的smoothScrollToPosition()方法时，
 * 默认会滑动到该position的底部。当该position的高度超出一屏时，
 * 就会让该position的底部与RecyclerView的底部对齐，本类实现了
 * 让该position的顶部与RecyclerView的顶部对齐。
 */
public class TopPositionLayoutManager extends LinearLayoutManager {
    private int duration = 300;
    //预加载下一个item露出部分
    private int extraLayoutSpace = SizeUtils.dp2px(10000);
    private  static int screenHeight = ScreenUtils.getScreenHeight();
    private Context mContext;
    private TopSmoothScroller smoothScroller;

    public TopPositionLayoutManager(Context context) {
        super(context);
        this.mContext = context;
    }

    public TopPositionLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.mContext = context;
    }

    public TopPositionLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
    }

//    @Override
//    protected int getExtraLayoutSpace(RecyclerView.State state) {
//         return super.getExtraLayoutSpace(state);
//      // return extraLayoutSpace;
//
//
//        //预览一个屏幕的空间提前加载下一个item
//        // return ScreenUtils.getScreenHeight();
//        //return 2000;
//    }


    @Override
    protected void calculateExtraLayoutSpace(@NonNull RecyclerView.State state,
                                             @NonNull int[] extraLayoutSpace) {
        final int offscreenSpace = 5000;
        extraLayoutSpace[0] = offscreenSpace;
        extraLayoutSpace[1] = offscreenSpace;
    }


//
//    int getPageSize() {
//        // final RecyclerView rv = ;
//        // return rv.getHeight() - rv.getPaddingTop() - rv.getPaddingBottom();
//        return extraLayoutSpace*100;
//    }

//    @Override
//    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
//                                                 @NonNull View child, @NonNull Rect rect, boolean immediate,
//                                                 boolean focusedChildVisible) {
//        return false; // users should use setCurrentItem instead
//    }


    /**
     * 重写本方法， RecyclerView#smoothScrollToPosition(int) 也是调用的本方法
     * 默认是使用了LinearSmoothScroller进行滑动
     *
     * @param recyclerView
     * @param state
     * @param position
     * @see RecyclerView#smoothScrollToPosition(int)
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        smoothScroller = new TopSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        setScrollSpeed(duration, sQuinticInterpolator2, recyclerView);
        startSmoothScroll(smoothScroller);
    }

    /**
     * 实现RecyclerView顶部与item的顶部对齐
     *
     * @see RecyclerView#smoothScrollToPosition(int)
     */
    static class TopSmoothScroller extends LinearSmoothScroller {



        TopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            int top = boxStart - viewStart;
            return top;
        }


        //        @Override
//        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//            // float newDuration = duration / (Math.abs(targetPosion - lastPositon));//重新计算相近两个位置的滚动间隔
//            float newDuration = scale * duration/ height * duration;//重新计算相近两个位置的滚动间隔
//            if (height == 0) {
//                return duration / displayMetrics.densityDpi;
//            } else {
//                return newDuration / displayMetrics.densityDpi;
//            }
//        }
//
        @Override
        protected int calculateTimeForScrolling(int dx) {
            if (dx > screenHeight) {
                dx = screenHeight;
            }
            return super.calculateTimeForScrolling(dx);
        }
    }

    /**
     * 实现RecyclerView中部与item的中部对齐
     *
     * @see RecyclerView#smoothScrollToPosition(int)
     */
    private static class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            int center = (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
            return center;
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

    public void setScrollSpeed(int duration, Interpolator interpolator, RecyclerView recyclerView) {
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
            mInterpolator.set(mViewFlinger.get(recyclerView),
                    mDecelerateInterpolator.get(smoothScroller));
            if (duration >= 0) {
                FixScrollDurationOverScroller overScroller = new FixScrollDurationOverScroller(mContext, interpolator);
                overScroller.setScrollDuration(duration);
                mScroller.set(mViewFlinger.get(recyclerView), overScroller);
            } else {
                OverScroller overScroller = new OverScroller(mContext, interpolator);
                mScroller.set(mViewFlinger.get(recyclerView), overScroller);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}