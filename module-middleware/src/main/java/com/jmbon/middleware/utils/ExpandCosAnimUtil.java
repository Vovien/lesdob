package com.jmbon.middleware.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.jmbon.widget.interpolator.EaseCubicInterpolator;

/**
 * view展开收起动画
 */
public class ExpandCosAnimUtil {

    public static void expand(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int viewHeight = view.getMeasuredHeight();
        //Log.e("viewHeight", SizeUtils.px2dp(viewHeight) + "");

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                view.setAlpha(interpolatedTime);
                if (interpolatedTime == 1) {
                    view.getLayoutParams().height = viewHeight;
                } else {
                    view.getLayoutParams().height = (int) (viewHeight * interpolatedTime);
                }
                view.requestLayout();
            }
        };
        animation.setDuration(250);
        animation.setInterpolator(new EaseCubicInterpolator());
        view.startAnimation(animation);
    }

    public static void expand(final View view, int viewHeight) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.getLayoutParams().height = viewHeight;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                view.setAlpha(interpolatedTime);
                if (interpolatedTime == 1) {
                    // view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    view.getLayoutParams().height = viewHeight;
                } else {
                    view.getLayoutParams().height = (int) (viewHeight * interpolatedTime);
                }
                view.requestLayout();
            }
        };
        animation.setDuration(250);
        animation.setInterpolator(new EaseCubicInterpolator());
        view.startAnimation(animation);
    }

    public static void collapse(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int viewHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.setAlpha(interpolatedTime);
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = viewHeight - (int) (viewHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };
        animation.setDuration(250);
        animation.setInterpolator(new EaseCubicInterpolator());
        view.startAnimation(animation);
    }

    public static void collapse(final View view, int viewHeight) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.setAlpha(1 - interpolatedTime);
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = viewHeight - (int) (viewHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };
        animation.setDuration(250);
        animation.setInterpolator(new EaseCubicInterpolator());
        view.startAnimation(animation);
    }
}