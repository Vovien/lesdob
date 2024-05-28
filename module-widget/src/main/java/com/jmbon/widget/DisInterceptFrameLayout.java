package com.jmbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.apkdv.mvvmfast.utils.TouchEventUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Func:用于子类防止父类拦截子类的事件
 */
public class DisInterceptFrameLayout extends FrameLayout {
    public DisInterceptFrameLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        views = new ArrayList<>();
    }

    public DisInterceptFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DisInterceptFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ArrayList<View> views;
    public View baseView;
    public boolean isCheckBaseView;

    public void setCheckBaseView(boolean checkBaseView) {
        isCheckBaseView = checkBaseView;
    }

    public void setViews(View... views) {
        this.views.addAll(Arrays.asList(views));
    }

    public void setBaseView(View baseView) {
        this.baseView = baseView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (baseView == null || views.isEmpty())
            return super.dispatchTouchEvent(ev);
        else {
            View clickView = null;
            for (View view : views) {
                if (TouchEventUtil.isOnView(ev, view)) {
                    clickView = view;
                }
            }
            if (clickView != null) {
                TouchEventUtil.reviseToView(clickView,ev);
                return clickView.onTouchEvent(ev);
            } else {
                if (TouchEventUtil.isOnView(ev, baseView) && isCheckBaseView) {
                    TouchEventUtil.reviseToView(baseView,ev);
                    return baseView.onTouchEvent(ev);
                } else {
                    return super.dispatchTouchEvent(ev);
                }
            }
        }
    }

}