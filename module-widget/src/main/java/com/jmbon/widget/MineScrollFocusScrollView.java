package com.jmbon.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Keep;
import androidx.core.widget.NestedScrollView;

import com.luck.picture.lib.tools.DoubleUtils;
import com.qmuiteam.qmui.widget.QMUIObservableScrollView;

/**
 * 获取焦点不滚动的scrollview
 */
@Keep
public class MineScrollFocusScrollView extends NestedScrollView {

    public boolean isAutoScroll = true;
    public int offsetY = 0;

    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    private ISmartScrollChangedListener mSmartScrollChangedListener;

    /**
     * 定义监听接口
     */
    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();

        void onScrolledToTop();
    }

    public void setScanScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener;
    }

    public MineScrollFocusScrollView(Context context) {
        super(context);
    }

    public MineScrollFocusScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineScrollFocusScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    int lastY = -1;

    @Override
    public void scrollTo(int x, int y) {
        if (!isAutoScroll || lastY == y) {
            lastY = y;
            isAutoScroll = true;
            return;
        }

        super.scrollTo(x, y);
    }

    public void setAutoScroll(boolean autoScroll) {
        isAutoScroll = autoScroll;
    }


    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
// 这个log可以研究ScrollView的上下padding对结果的影响
        //System.out.println("onScrollChanged getScrollY():" + getScrollY() + " t: " + t + " paddingTop: " + getPaddingTop());
        if (getScrollY() == 0) {
            isScrolledToTop = true;
            isScrolledToBottom = false;
            // System.out.println("onScrollChanged isScrolledToTop:" + isScrolledToTop);
        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() + offsetY >= getChildAt(0).getHeight()) {
            isScrolledToBottom = true;
            // System.out.println("onScrollChanged isScrolledToBottom:" + isScrolledToBottom);
            isScrolledToTop = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = false;
        }
        notifyScrollChangedListeners();
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }
}
