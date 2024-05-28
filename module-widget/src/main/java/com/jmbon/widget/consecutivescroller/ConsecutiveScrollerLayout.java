//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jmbon.widget.consecutivescroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.EdgeEffectCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jmbon.widget.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConsecutiveScrollerLayout extends ViewGroup implements ScrollingView, NestedScrollingParent2, NestedScrollingChild2 {
    private int mSecondScrollY;
    int mScrollRange;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private VelocityTracker mAdjustVelocityTracker;
    private int mAdjustYVelocity;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mTouchSlop;
    private int mTouchY;
    private int mEventX;
    private int mEventY;
    private float mFixedY;
    private final int[] mDownLocation;
    private boolean mTouching;
    private static final int SCROLL_NONE = 0;
    private static final int SCROLL_VERTICAL = 1;
    private static final int SCROLL_HORIZONTAL = 2;
    private int SCROLL_ORIENTATION;
    protected ConsecutiveScrollerLayout.OnScrollChangeListener mOnScrollChangeListener;
    private int mActivePointerId;
    private NestedScrollingParentHelper mParentHelper;
    private NestedScrollingChildHelper mChildHelper;
    private final int[] mScrollOffset;
    private final int[] mScrollConsumed;
    private View mScrollToTopView;
    private int mAdjust;
    private int mScrollToIndex;
    private int mSmoothScrollOffset;
    private int mScrollToIndexWithOffset;
    private EdgeEffect mEdgeGlowTop;
    private EdgeEffect mEdgeGlowBottom;
    private int mLastScrollerY;
    private boolean isPermanent;
    private boolean mAutoAdjustHeightAtBottomView;
    private int mAdjustHeightOffset;
    private int mStickyOffset;
    private View mCurrentStickyView;
    private final List<View> mCurrentStickyViews;
    private final List<View> mTempStickyViews;
    private int mOldScrollY;
    private final List<View> mViews;
    private int mNestedYOffset;
    private ConsecutiveScrollerLayout.OnStickyChangeListener mOnStickyChangeListener;
    private ConsecutiveScrollerLayout.OnPermanentStickyChangeListener mOnPermanentStickyChangeListener;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;
    private int mScrollState;
    static final Interpolator sQuinticInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            --t;
            return t * t * t * t * t + 1.0F;
        }
    };
    private boolean isTouchNotTriggerScrollStick;
    private boolean isBrake;

    public ConsecutiveScrollerLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ConsecutiveScrollerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConsecutiveScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDownLocation = new int[2];
        this.mTouching = false;
        this.SCROLL_ORIENTATION = 0;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mScrollToIndex = -1;
        this.mSmoothScrollOffset = 0;
        this.mScrollToIndexWithOffset = 0;
        this.mAdjustHeightOffset = 0;
        this.mStickyOffset = 0;
        this.mCurrentStickyViews = new ArrayList();
        this.mTempStickyViews = new ArrayList();
        this.mOldScrollY = 0;
        this.mViews = new ArrayList();
        this.mNestedYOffset = 0;
        this.mScrollState = 0;
        this.isTouchNotTriggerScrollStick = false;
        this.isBrake = false;
        TypedArray a = null;

        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.ConsecutiveScrollerLayout);
            this.isPermanent = a.getBoolean(R.styleable.ConsecutiveScrollerLayout_isPermanent, false);
            this.mStickyOffset = a.getDimensionPixelOffset(R.styleable.ConsecutiveScrollerLayout_stickyOffset, 0);
            this.mAutoAdjustHeightAtBottomView = a.getBoolean(R.styleable.ConsecutiveScrollerLayout_autoAdjustHeightAtBottomView, false);
            this.mAdjustHeightOffset = a.getDimensionPixelOffset(R.styleable.ConsecutiveScrollerLayout_adjustHeightOffset, 0);
        } finally {
            if (a != null) {
                a.recycle();
            }

        }

        this.mScroller = new OverScroller(this.getContext(), sQuinticInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mTouchSlop = ViewConfiguration.getTouchSlop();
        this.setWillNotDraw(false);
        this.setVerticalScrollBarEnabled(true);
        this.mParentHelper = new NestedScrollingParentHelper(this);
        this.mChildHelper = new NestedScrollingChildHelper(this);
        this.setNestedScrollingEnabled(true);
        this.setChildrenDrawingOrderEnabled(true);
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (params instanceof ConsecutiveScrollerLayout.LayoutParams) {
            LayoutParamsUtils.invalidTopAndBottomMargin((ConsecutiveScrollerLayout.LayoutParams) params);
        }

        super.addView(child, index, params);
        if (ScrollUtils.isConsecutiveScrollerChild(child)) {
            View scrollChild = ScrollUtils.getScrollChild(child);
            this.disableChildScroll(scrollChild);
            if (scrollChild instanceof IConsecutiveScroller) {
                List<View> views = ((IConsecutiveScroller) scrollChild).getScrolledViews();
                if (views != null && !views.isEmpty()) {
                    int size = views.size();

                    for (int i = 0; i < size; ++i) {
                        this.disableChildScroll((View) views.get(i));
                    }
                }
            }
        }

        if (child instanceof ViewGroup) {
            ((ViewGroup) child).setClipToPadding(false);
        }

    }

    private void disableChildScroll(View child) {
        child.setVerticalScrollBarEnabled(false);
        child.setHorizontalScrollBarEnabled(false);
        child.setOverScrollMode(2);
        ViewCompat.setNestedScrollingEnabled(child, false);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.resetScrollToTopView();
        int contentWidth = 0;
        int contentHeight = 0;
        List<View> children = this.getNonGoneChildren();
        int count = children.size();

        for (int i = 0; i < count; ++i) {
            View child = (View) children.get(i);
            int heightUsed = this.getAdjustHeightForChild(child);
            this.measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            contentWidth = Math.max(contentWidth, this.getContentWidth(child));
            contentHeight += child.getMeasuredHeight();
        }

        this.setMeasuredDimension(this.measureSize(widthMeasureSpec, contentWidth + this.getPaddingLeft() + this.getPaddingRight()), this.measureSize(heightMeasureSpec, contentHeight + this.getPaddingTop() + this.getPaddingBottom()));
    }

    private int getAdjustHeightForChild(View child) {
        return this.mAutoAdjustHeightAtBottomView && child == this.getChildAt(this.getChildCount() - 1) ? this.getAdjustHeight() : 0;
    }

    private int getAdjustHeight() {
        List<View> children = this.getStickyChildren();
        int adjustHeight = this.mAdjustHeightOffset;
        int count = children.size();
        int i;
        View child;
        if (this.isPermanent) {
            for (i = 0; i < count; ++i) {
                child = (View) children.get(i);
                if (!this.isSink(child)) {
                    adjustHeight += child.getMeasuredHeight();
                }
            }
        } else {
            for (i = count - 1; i >= 0; --i) {
                child = (View) children.get(i);
                if (!this.isSink(child)) {
                    adjustHeight += child.getMeasuredHeight();
                    break;
                }
            }
        }

        return adjustHeight;
    }

    private int getContentWidth(View child) {
        int contentWidth = child.getMeasuredWidth();
        MarginLayoutParams params = (ConsecutiveScrollerLayout.LayoutParams) child.getLayoutParams();
        contentWidth += params.leftMargin;
        contentWidth += params.rightMargin;
        return contentWidth;
    }

    private int measureSize(int measureSpec, int size) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == 1073741824) {
            result = specSize;
        } else {
            result = size;
            if (specMode == -2147483648) {
                result = Math.min(size, specSize);
            }
        }

        result = Math.max(result, this.getSuggestedMinimumWidth());
        result = resolveSizeAndState(result, measureSpec, 0);
        return result;
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        LayoutParamsUtils.invalidTopAndBottomMargin((ConsecutiveScrollerLayout.LayoutParams) child.getLayoutParams());
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mScrollRange = 0;
        int childTop = this.getPaddingTop();
        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();
        int parentWidth = this.getMeasuredWidth();
        List<View> children = this.getNonGoneChildren();
        int count = children.size();

        for (int i = 0; i < count; ++i) {
            View child = (View) children.get(i);
            int bottom = childTop + child.getMeasuredHeight();
            int left = this.getChildLeft(child, parentWidth, paddingLeft, paddingRight);
            child.layout(left, childTop, left + child.getMeasuredWidth(), bottom);
            childTop = bottom;
            this.mScrollRange += child.getHeight();
        }

        this.mScrollRange -= this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        if (this.mScrollRange < 0) {
            this.mScrollRange = 0;
        }

        this.checkLayoutChange(changed, false);
        this.sortViews();
    }

    public void requestLayout() {
        super.requestLayout();
    }

    private void sortViews() {
        List<View> list = new ArrayList();
        int count = this.getChildCount();

        int i;
        View child;
        for (i = 0; i < count; ++i) {
            child = this.getChildAt(i);
            if (!this.isStickyView(child) || this.isSink(child)) {
                list.add(child);
            }
        }

        for (i = 0; i < count; ++i) {
            child = this.getChildAt(i);
            if (this.isStickyView(child) && !this.isSink(child)) {
                list.add(child);
            }
        }

        this.mViews.clear();
        this.mViews.addAll(list);
    }

    private int getChildLeft(View child, int parentWidth, int paddingLeft, int paddingRight) {
        ConsecutiveScrollerLayout.LayoutParams lp = (ConsecutiveScrollerLayout.LayoutParams) child.getLayoutParams();
        switch (lp.align) {
            case RIGHT:
                return parentWidth - child.getMeasuredWidth() - paddingRight - lp.rightMargin;
            case CENTER:
                return paddingLeft + lp.leftMargin + (parentWidth - child.getMeasuredWidth() - paddingLeft - lp.leftMargin - paddingRight - lp.rightMargin) / 2;
            case LEFT:
            default:
                return paddingLeft + lp.leftMargin;
        }
    }

    private void resetScrollToTopView() {
        this.mScrollToTopView = this.findFirstVisibleView();
        if (this.mScrollToTopView != null) {
            this.mAdjust = this.getScrollY() - this.mScrollToTopView.getTop();
        }

    }

    protected ConsecutiveScrollerLayout.LayoutParams generateDefaultLayoutParams() {
        return new ConsecutiveScrollerLayout.LayoutParams(-2, -2);
    }

    protected ConsecutiveScrollerLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new ConsecutiveScrollerLayout.LayoutParams(p);
    }

    public ConsecutiveScrollerLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ConsecutiveScrollerLayout.LayoutParams(this.getContext(), attrs);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actionIndex = ev.getActionIndex();
        if (this.SCROLL_ORIENTATION == 2) {
            ev.setLocation(ev.getX(), this.mFixedY);
        }

        MotionEvent vtev = MotionEvent.obtain(ev);
        if (vtev.getActionMasked() == 0) {
            this.mNestedYOffset = 0;
        }

        vtev.offsetLocation(0.0F, (float) this.mNestedYOffset);
        int yVelocity;
        switch (ev.getActionMasked()) {
            case 0:
                this.isBrake = this.mScrollState == 2;
                this.stopScroll();
                this.checkTargetsScroll(false, false);
                this.mTouching = true;
                this.SCROLL_ORIENTATION = 0;
                this.mFixedY = ev.getY();
                this.mActivePointerId = ev.getPointerId(actionIndex);
                this.mEventY = (int) ev.getY(actionIndex);
                this.mEventX = (int) ev.getX(actionIndex);
                this.initOrResetAdjustVelocityTracker();
                this.mAdjustVelocityTracker.addMovement(vtev);
                this.startNestedScroll(2, 0);
                this.mDownLocation[0] = ScrollUtils.getRawX(this, ev, actionIndex);
                this.mDownLocation[1] = ScrollUtils.getRawY(this, ev, actionIndex);
                this.isTouchNotTriggerScrollStick = ScrollUtils.isTouchNotTriggerScrollStick(this, this.mDownLocation[0], this.mDownLocation[1]);
                break;
            case 1:
            case 3:
                if (this.mAdjustVelocityTracker != null) {
                    this.mAdjustVelocityTracker.addMovement(vtev);
                    this.mAdjustVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    yVelocity = (int) this.mAdjustVelocityTracker.getYVelocity();
                    this.mAdjustYVelocity = Math.max(-this.mMaximumVelocity, Math.min(yVelocity, this.mMaximumVelocity));
                    this.recycleAdjustVelocityTracker();
                    int touchX = ScrollUtils.getRawX(this, ev, actionIndex);
                    int touchY = ScrollUtils.getRawY(this, ev, actionIndex);
                    View targetView = this.getTouchTarget(touchX, touchY);
                    boolean canScrollVerticallyChild = ScrollUtils.canScrollVertically(targetView);
                    boolean canScrollHorizontallyChild = ScrollUtils.canScrollHorizontally(targetView);
                    if (this.SCROLL_ORIENTATION != 1 && canScrollVerticallyChild && Math.abs(yVelocity) >= this.mMinimumVelocity && !ScrollUtils.isHorizontalScroll(this, touchX, touchY)) {
                        ev.setAction(3);
                    }

                    if (this.SCROLL_ORIENTATION != 1 && !ScrollUtils.isConsecutiveScrollParent(this) && this.isIntercept(ev) && Math.abs(yVelocity) >= this.mMinimumVelocity && (this.SCROLL_ORIENTATION == 0 || !canScrollHorizontallyChild)) {
                        this.fling(-this.mAdjustYVelocity);
                    }
                }

                this.mEventY = 0;
                this.mEventX = 0;
                this.mTouching = false;
                this.mDownLocation[0] = 0;
                this.mDownLocation[1] = 0;
                this.isTouchNotTriggerScrollStick = false;
                break;
            case 2:
                int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                if (pointerIndex < 0 || pointerIndex >= ev.getPointerCount()) {
                    return false;
                }

                this.initAdjustVelocityTrackerIfNotExists();
                this.mAdjustVelocityTracker.addMovement(vtev);
                int offsetY = (int) ev.getY(pointerIndex) - this.mEventY;
                int offsetX = (int) ev.getX(pointerIndex) - this.mEventX;
                if (this.SCROLL_ORIENTATION == 0 && (this.isIntercept(ev) || this.isIntercept(this.mDownLocation[0], this.mDownLocation[1]))) {
                    if (Math.abs(offsetX) > Math.abs(offsetY)) {
                        if (Math.abs(offsetX) >= this.mTouchSlop) {
                            this.SCROLL_ORIENTATION = 2;
                            ev.setLocation(ev.getX(), this.mFixedY);
                        }
                    } else if (Math.abs(offsetY) >= this.mTouchSlop) {
                        this.SCROLL_ORIENTATION = 1;
                    }

                    if (this.SCROLL_ORIENTATION == 0) {
                        return true;
                    }
                }

                this.mEventY = (int) ev.getY(pointerIndex);
                this.mEventX = (int) ev.getX(pointerIndex);
            case 4:
            default:
                break;
            case 5:
                this.mActivePointerId = ev.getPointerId(actionIndex);
                this.mEventY = (int) ev.getY(actionIndex);
                this.mEventX = (int) ev.getX(actionIndex);
                this.requestDisallowInterceptTouchEvent(false);
                this.mDownLocation[0] = ScrollUtils.getRawX(this, ev, actionIndex);
                this.mDownLocation[1] = ScrollUtils.getRawY(this, ev, actionIndex);
                this.isTouchNotTriggerScrollStick = ScrollUtils.isTouchNotTriggerScrollStick(this, this.mDownLocation[0], this.mDownLocation[1]);
                this.initAdjustVelocityTrackerIfNotExists();
                this.mAdjustVelocityTracker.addMovement(vtev);
                break;
            case 6:
                if (this.mActivePointerId == ev.getPointerId(actionIndex)) {
                    yVelocity = actionIndex == 0 ? 1 : 0;
                    this.mActivePointerId = ev.getPointerId(yVelocity);
                    this.mEventY = (int) ev.getY(yVelocity);
                    this.mEventX = (int) ev.getX(yVelocity);
                    this.mDownLocation[0] = ScrollUtils.getRawX(this, ev, yVelocity);
                    this.mDownLocation[1] = ScrollUtils.getRawY(this, ev, yVelocity);
                    this.isTouchNotTriggerScrollStick = ScrollUtils.isTouchNotTriggerScrollStick(this, this.mDownLocation[0], this.mDownLocation[1]);
                }

                this.initAdjustVelocityTrackerIfNotExists();
                this.mAdjustVelocityTracker.addMovement(vtev);
        }

        vtev.recycle();
        boolean dispatch = super.dispatchTouchEvent(ev);
        switch (ev.getActionMasked()) {
            case 1:
            case 3:
                this.SCROLL_ORIENTATION = 0;
                this.mAdjustYVelocity = 0;
                if (this.mScroller.isFinished()) {
                    this.setScrollState(0);
                }
            default:
                return dispatch;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case 0:
                this.initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                break;
            case 1:
            case 3:
                this.stopNestedScroll(0);
                if (this.isBrake && this.SCROLL_ORIENTATION == 0) {
                    return true;
                }
                break;
            case 2:
                if (this.SCROLL_ORIENTATION != 2 && (this.isIntercept(ev) || this.isIntercept(this.mDownLocation[0], this.mDownLocation[1]))) {
                    return true;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!ScrollUtils.isConsecutiveScrollParent(this) && !this.isTouchNotTriggerScrollStick) {
            MotionEvent vtev = MotionEvent.obtain(ev);
            if (ev.getActionMasked() == 0) {
                this.mNestedYOffset = 0;
            }

            vtev.offsetLocation(0.0F, (float) this.mNestedYOffset);
            int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
            if (pointerIndex >= 0 && pointerIndex < ev.getPointerCount()) {
                int pulledToY;
                switch (ev.getActionMasked()) {
                    case 0:
                        this.startNestedScroll(2, 0);
                    case 5:
                    case 6:
                        this.mTouchY = (int) ev.getY(pointerIndex);
                        break;
                    case 1:
                        this.endDrag();
                        this.mTouchY = 0;
                        if (this.mVelocityTracker != null) {
                            this.mVelocityTracker.addMovement(vtev);
                            this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                            pulledToY = (int) this.mVelocityTracker.getYVelocity();
                            pulledToY = Math.max(-this.mMaximumVelocity, Math.min(pulledToY, this.mMaximumVelocity));
                            if (pulledToY == 0 && this.mAdjustYVelocity != 0) {
                                pulledToY = this.mAdjustYVelocity;
                            }

                            this.fling(-pulledToY);
                            this.recycleVelocityTracker();
                        }
                        break;
                    case 2:
                        if (this.mTouchY == 0) {
                            this.mTouchY = (int) ev.getY(pointerIndex);
                            return true;
                        }

                        this.mScrollConsumed[1] = 0;
                        int y = (int) ev.getY(pointerIndex);
                        int deltaY = this.mTouchY - y;
                        this.mTouchY = y;
                        if (this.dispatchNestedPreScroll(0, deltaY, this.mScrollConsumed, this.mScrollOffset, 0)) {
                            deltaY -= this.mScrollConsumed[1];
                            ev.offsetLocation(0.0F, (float) this.mScrollOffset[1]);
                            this.mNestedYOffset += this.mScrollOffset[1];
                            this.mTouchY -= this.mScrollOffset[1];
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                        }

                        int oldScrollY = this.mSecondScrollY;
                        if (this.mScrollState != 1) {
                            boolean startScroll = false;
                            if (this.canScrollVertically() && Math.abs(deltaY) > 0) {
                                startScroll = true;
                            }

                            if (startScroll) {
                                this.setScrollState(1);
                            }
                        }

                        if (this.mScrollState == 1) {
                            this.dispatchScroll(deltaY);
                        }

                        int scrolledDeltaY = this.mSecondScrollY - oldScrollY;
                        if (scrolledDeltaY != 0) {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                        }

                        deltaY -= scrolledDeltaY;
                        if (this.dispatchNestedScroll(0, scrolledDeltaY, 0, deltaY, this.mScrollOffset, 0)) {
                            deltaY += this.mScrollOffset[1];
                            this.mTouchY -= this.mScrollOffset[1];
                            this.mNestedYOffset += this.mScrollOffset[1];
                            ev.offsetLocation(0.0F, (float) this.mScrollOffset[1]);
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                        }

                        int range = this.getScrollRange();
                        int overscrollMode = this.getOverScrollMode();
                        boolean canOverscroll = overscrollMode == 0 || overscrollMode == 1 && range > 0;
                        if (canOverscroll) {
                            this.ensureGlows();
                            pulledToY = oldScrollY + deltaY;
                            if (pulledToY < 0) {
                                EdgeEffectCompat.onPull(this.mEdgeGlowTop, (float) deltaY / (float) this.getHeight(), ev.getX(pointerIndex) / (float) this.getWidth());
                                if (!this.mEdgeGlowBottom.isFinished()) {
                                    this.mEdgeGlowBottom.onRelease();
                                }
                            } else if (pulledToY > range) {
                                EdgeEffectCompat.onPull(this.mEdgeGlowBottom, (float) deltaY / (float) this.getHeight(), 1.0F - ev.getX(pointerIndex) / (float) this.getWidth());
                                if (!this.mEdgeGlowTop.isFinished()) {
                                    this.mEdgeGlowTop.onRelease();
                                }
                            }

                            if (this.mEdgeGlowTop != null && (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished())) {
                                ViewCompat.postInvalidateOnAnimation(this);
                            }
                        }
                        break;
                    case 3:
                        this.endDrag();
                        this.mTouchY = 0;
                        this.recycleVelocityTracker();
                        this.setScrollState(0);
                    case 4:
                }

                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.addMovement(vtev);
                }

                vtev.recycle();
                return true;
            } else {
                return false;
            }
        } else {
            return super.onTouchEvent(ev);
        }
    }

    private boolean canScrollVertically() {
        return !this.isScrollTop() || !this.isScrollBottom();
    }

    protected int getChildDrawingOrder(int childCount, int drawingPosition) {
        if (this.mViews.size() > drawingPosition) {
            int index = this.indexOfChild((View) this.mViews.get(drawingPosition));
            if (index != -1) {
                return index;
            }
        }

        return super.getChildDrawingOrder(childCount, drawingPosition);
    }

    int getDrawingPosition(View child) {
        return this.mViews.indexOf(child);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mOldScrollY != this.getScrollY()) {
            this.mOldScrollY = this.getScrollY();
            this.resetSticky();
        }

        if (this.mEdgeGlowTop != null) {
            int scrollY = this.getScrollY();
            int restoreCount;
            int width;
            int height;
            int xTranslation;
            int yTranslation;
            if (!this.mEdgeGlowTop.isFinished()) {
                restoreCount = canvas.save();
                width = this.getWidth();
                height = this.getHeight();
                xTranslation = 0;
                yTranslation = scrollY;
                if (VERSION.SDK_INT < 21 || this.getClipToPadding()) {
                    width -= this.getPaddingLeft() + this.getPaddingRight();
                    xTranslation += this.getPaddingLeft();
                }

                if (VERSION.SDK_INT >= 21 && this.getClipToPadding()) {
                    height -= this.getPaddingTop() + this.getPaddingBottom();
                    yTranslation = scrollY + this.getPaddingTop();
                }

                canvas.translate((float) xTranslation, (float) yTranslation);
                this.mEdgeGlowTop.setSize(width, height);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }

                canvas.restoreToCount(restoreCount);
            }

            if (!this.mEdgeGlowBottom.isFinished()) {
                restoreCount = canvas.save();
                width = this.getWidth();
                height = this.getHeight();
                xTranslation = 0;
                yTranslation = scrollY + height;
                if (VERSION.SDK_INT < 21 || this.getClipToPadding()) {
                    width -= this.getPaddingLeft() + this.getPaddingRight();
                    xTranslation += this.getPaddingLeft();
                }

                if (VERSION.SDK_INT >= 21 && this.getClipToPadding()) {
                    height -= this.getPaddingTop() + this.getPaddingBottom();
                    yTranslation -= this.getPaddingBottom();
                }

                canvas.translate((float) (xTranslation - width), (float) yTranslation);
                canvas.rotate(180.0F, (float) width, 0.0F);
                this.mEdgeGlowBottom.setSize(width, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }

                canvas.restoreToCount(restoreCount);
            }
        }

    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (this.getChildCount() > 0) {
            int childSize = this.computeVerticalScrollRange();
            int parentSpace = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            scrollRange = Math.max(0, childSize - parentSpace);
        }

        return scrollRange;
    }

    private void fling(int velocityY) {
        if (Math.abs(velocityY) > this.mMinimumVelocity && !this.dispatchNestedPreFling(0.0F, (float) velocityY)) {
            boolean canScroll = velocityY < 0 && !this.isScrollTop() || velocityY > 0 && !this.isScrollBottom();
            this.dispatchNestedFling(0.0F, (float) velocityY, canScroll);
            this.mScroller.fling(0, this.mSecondScrollY, 1, velocityY, -2147483648, -2147483648, -2147483648, 2147483647);
            this.startNestedScroll(2, 1);
            this.setScrollState(2);
            this.mLastScrollerY = this.mSecondScrollY;
            this.invalidate();
        }

    }

    public void computeScroll() {
        if (this.mScrollToIndex != -1 && this.mSmoothScrollOffset != 0) {
            if (this.mSmoothScrollOffset > 0 && this.mSmoothScrollOffset < 200) {
                this.mSmoothScrollOffset += 5;
            }

            if (this.mSmoothScrollOffset < 0 && this.mSmoothScrollOffset > -200) {
                this.mSmoothScrollOffset -= 5;
            }

            this.dispatchScroll(this.mSmoothScrollOffset);
            this.invalidate();
            Log.e("TAG", "computeScroll ==" + System.currentTimeMillis());
        } else {
            if (this.mScroller.computeScrollOffset()) {
                int y = this.mScroller.getCurrY();
                int unconsumed = y - this.mLastScrollerY;
                this.mLastScrollerY = y;
                this.mScrollConsumed[1] = 0;
                this.dispatchNestedPreScroll(0, unconsumed, this.mScrollConsumed, (int[]) null, 1);
                unconsumed -= this.mScrollConsumed[1];
                int oldScrollY = this.mSecondScrollY;
                this.dispatchScroll(unconsumed);
                int scrolledByMe = this.mSecondScrollY - oldScrollY;
                unconsumed -= scrolledByMe;
                if (unconsumed < 0 && this.isScrollTop() || unconsumed > 0 && this.isScrollBottom()) {
                    this.dispatchNestedScroll(0, scrolledByMe, 0, unconsumed, this.mScrollOffset, 1);
                    unconsumed += this.mScrollOffset[1];
                }

                if (unconsumed < 0 && this.isScrollTop() || unconsumed > 0 && this.isScrollBottom()) {
                    int mode = this.getOverScrollMode();
                    boolean canOverscroll = mode == 0 || mode == 1 && this.getScrollRange() > 0;
                    if (canOverscroll) {
                        this.ensureGlows();
                        if (unconsumed < 0) {
                            if (this.mEdgeGlowTop.isFinished()) {
                                this.mEdgeGlowTop.onAbsorb((int) this.mScroller.getCurrVelocity());
                            }
                        } else if (this.mEdgeGlowBottom.isFinished()) {
                            this.mEdgeGlowBottom.onAbsorb((int) this.mScroller.getCurrVelocity());
                        }
                    }

                    this.stopScroll();
                }

                this.invalidate();
            }

            if (this.mScrollState == 2 && this.mScroller.isFinished()) {
                this.stopNestedScroll(1);
                this.checkTargetsScroll(false, false);
                this.setScrollState(0);
            }
        }

    }

    private void endDrag() {
        if (this.mEdgeGlowTop != null) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }

    }

    private void ensureGlows() {
        if (this.getOverScrollMode() != 2) {
            if (this.mEdgeGlowTop == null) {
                Context context = this.getContext();
                this.mEdgeGlowTop = new EdgeEffect(context);
                this.mEdgeGlowBottom = new EdgeEffect(context);
            }
        } else {
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        }

    }

    private void dispatchScroll(int offset) {
        if (offset > 0) {
            this.scrollUp(offset);
        } else if (offset < 0) {
            this.scrollDown(offset);
        }

    }

    private void scrollUp(int offset) {
        int remainder = offset;
        int oldScrollY = this.computeVerticalScrollOffset();

        int scrollAnchor;
        int scrollOffset;
        do {
            scrollAnchor = 0;
            int viewScrollOffset = 0;
            View firstVisibleView;
            if (this.mScrollToIndex != -1) {
                firstVisibleView = this.getChildAt(this.mScrollToIndex);
                scrollAnchor = firstVisibleView.getTop() - this.mScrollToIndexWithOffset;
                scrollAnchor -= this.getAdjustHeightForChild(firstVisibleView);
                if (this.mScrollToIndexWithOffset < 0) {
                    viewScrollOffset = this.getViewsScrollOffset(this.mScrollToIndex);
                }

                if (this.getScrollY() + this.getPaddingTop() + viewScrollOffset >= scrollAnchor || this.isScrollBottom()) {
                    this.mScrollToIndex = -1;
                    this.mSmoothScrollOffset = 0;
                    this.mScrollToIndexWithOffset = 0;
                    this.setScrollState(0);
                    break;
                }
            }

            scrollOffset = 0;
            if (!this.isScrollBottom()) {
                firstVisibleView = null;
                if (this.getScrollY() < this.mScrollRange) {
                    firstVisibleView = this.findFirstVisibleView();
                } else {
                    firstVisibleView = this.getBottomView();
                }

                if (firstVisibleView != null) {
                    this.awakenScrollBars();
                    int bottomOffset = ScrollUtils.getScrollBottomOffset(firstVisibleView);
                    if (bottomOffset > 0) {
                        scrollOffset = Math.min(remainder, bottomOffset);
                        if (this.mScrollToIndex != -1) {
                            scrollOffset = Math.min(scrollOffset, scrollAnchor - (this.getScrollY() + this.getPaddingTop() + viewScrollOffset));
                        }

                        this.scrollChild(firstVisibleView, scrollOffset);
                    } else {
                        scrollOffset = Math.min(remainder, firstVisibleView.getBottom() - this.getPaddingTop() - this.getScrollY());
                        if (this.mScrollToIndex != -1) {
                            scrollOffset = Math.min(scrollOffset, scrollAnchor - (this.getScrollY() + this.getPaddingTop() + viewScrollOffset));
                        }

                        this.scrollSelf(this.getScrollY() + scrollOffset);
                    }

                    this.mSecondScrollY += scrollOffset;
                    remainder -= scrollOffset;
                }
            }
        } while (scrollOffset > 0 && remainder > 0);

        scrollAnchor = this.computeVerticalScrollOffset();
        if (oldScrollY != scrollAnchor) {
            this.scrollChange(scrollAnchor, oldScrollY);
        }

    }

    private void scrollDown(int offset) {
        int remainder = offset;
        int oldScrollY = this.computeVerticalScrollOffset();

        int scrollAnchor;
        int scrollOffset;
        do {
            scrollAnchor = 0;
            int viewScrollOffset = 0;
            View lastVisibleView;
            if (this.mScrollToIndex != -1) {
                lastVisibleView = this.getChildAt(this.mScrollToIndex);
                scrollAnchor = lastVisibleView.getTop() - this.mScrollToIndexWithOffset;
                scrollAnchor -= this.getAdjustHeightForChild(lastVisibleView);
                viewScrollOffset = this.getViewsScrollOffset(this.mScrollToIndex);
                if (this.getScrollY() + this.getPaddingTop() + viewScrollOffset <= scrollAnchor || this.isScrollTop()) {
                    this.mScrollToIndex = -1;
                    this.mSmoothScrollOffset = 0;
                    this.mScrollToIndexWithOffset = 0;
                    this.setScrollState(0);
                    break;
                }
            }

            scrollOffset = 0;
            if (!this.isScrollTop()) {
                lastVisibleView = null;
                if (this.getScrollY() < this.mScrollRange) {
                    lastVisibleView = this.findLastVisibleView();
                } else {
                    lastVisibleView = this.getBottomView();
                }

                if (lastVisibleView != null) {
                    this.awakenScrollBars();
                    int childScrollOffset = ScrollUtils.getScrollTopOffset(lastVisibleView);
                    if (childScrollOffset < 0) {
                        scrollOffset = Math.max(remainder, childScrollOffset);
                        if (this.mScrollToIndex != -1) {
                            scrollOffset = Math.max(scrollOffset, scrollAnchor - (this.getScrollY() + this.getPaddingTop() + viewScrollOffset));
                        }

                        this.scrollChild(lastVisibleView, scrollOffset);
                    } else {
                        int scrollY = this.getScrollY();
                        scrollOffset = Math.max(remainder, lastVisibleView.getTop() + this.getPaddingBottom() - scrollY - this.getHeight());
                        scrollOffset = Math.max(scrollOffset, -scrollY);
                        if (this.mScrollToIndex != -1) {
                            scrollOffset = Math.max(scrollOffset, scrollAnchor - (this.getScrollY() + this.getPaddingTop() + viewScrollOffset));
                        }

                        this.scrollSelf(scrollY + scrollOffset);
                    }

                    this.mSecondScrollY += scrollOffset;
                    remainder -= scrollOffset;
                }
            }
        } while (scrollOffset < 0 && remainder < 0);

        scrollAnchor = this.computeVerticalScrollOffset();
        //  if (oldScrollY != scrollAnchor) {
        this.scrollChange(scrollAnchor, oldScrollY);
        //  }

    }

    public void scrollBy(int x, int y) {
        this.scrollTo(0, this.mSecondScrollY + y);
    }

    public void scrollTo(int x, int y) {
        this.dispatchScroll(y - this.mSecondScrollY);
    }

    private void scrollChange(int scrollY, int oldScrollY) {
        if (this.mOnScrollChangeListener != null) {
            this.mOnScrollChangeListener.onScrollChange(this, scrollY, oldScrollY, this.mScrollState);
        }

    }

    private void stickyChange(View oldStickyView, View newStickyView) {
        if (this.mOnStickyChangeListener != null) {
            this.mOnStickyChangeListener.onStickyChange(oldStickyView, newStickyView);
        }

    }

    private void permanentStickyChange(List<View> mCurrentStickyViews) {
        if (this.mOnPermanentStickyChangeListener != null) {
            this.mOnPermanentStickyChangeListener.onStickyChange(mCurrentStickyViews);
        }

    }

    private void scrollSelf(int y) {
        int scrollY = y;
        if (y < 0) {
            scrollY = 0;
        } else if (y > this.mScrollRange) {
            scrollY = this.mScrollRange;
        }

        super.scrollTo(0, scrollY);
    }

    private void scrollChild(View child, int y) {
        View scrolledView = ScrollUtils.getScrolledView(child);
        if (scrolledView instanceof AbsListView) {
            AbsListView listView = (AbsListView) scrolledView;
            if (VERSION.SDK_INT >= 19) {
                listView.scrollListBy(y);
            }
        } else {
            boolean isInterceptRequestLayout = false;
            if (scrolledView instanceof RecyclerView) {
                isInterceptRequestLayout = ScrollUtils.startInterceptRequestLayout((RecyclerView) scrolledView);
            }

            scrolledView.scrollBy(0, y);
            if (isInterceptRequestLayout) {
                final RecyclerView view = (RecyclerView) scrolledView;
                view.postDelayed(new Runnable() {
                    public void run() {
                        ScrollUtils.stopInterceptRequestLayout(view);
                    }
                }, 0L);
            }
        }

    }

    public void checkLayoutChange() {
        this.checkLayoutChange(false, true);
    }

    private void checkLayoutChange(boolean changed, boolean isForce) {
        int y = this.mSecondScrollY;
        if (this.mScrollToTopView != null && changed) {
            if (this.indexOfChild(this.mScrollToTopView) != -1) {
                this.scrollSelf(this.mScrollToTopView.getTop() + this.mAdjust);
            }
        } else {
            this.scrollSelf(this.getScrollY());
        }

        this.checkTargetsScroll(true, isForce);
        if (y != this.mSecondScrollY && this.mScrollToTopView != this.findFirstVisibleView()) {
            this.scrollTo(0, y);
        }

        this.mScrollToTopView = null;
        this.mAdjust = 0;
        this.resetChildren();
        this.resetSticky();
    }

    private void checkTargetsScroll(boolean isLayoutChange, boolean isForce) {
        if (isForce || !this.mTouching && this.mScroller.isFinished() && this.mScrollToIndex == -1) {
            int oldScrollY = this.computeVerticalScrollOffset();
            View target = this.findFirstVisibleView();
            if (target != null) {
                int index = this.indexOfChild(target);
                int newScrollY;
                if (isLayoutChange) {
                    while (true) {
                        newScrollY = ScrollUtils.getScrollBottomOffset(target);
                        int scrollTopOffset = target.getTop() - this.getScrollY();
                        if (newScrollY <= 0 || scrollTopOffset >= 0) {
                            break;
                        }

                        int offset = Math.min(newScrollY, -scrollTopOffset);
                        this.scrollSelf(this.getScrollY() - offset);
                        this.scrollChild(target, offset);
                    }
                }

                List views;
                int size;
                int c;
                View child;
                View scrollChild;
                for (newScrollY = 0; newScrollY < index; ++newScrollY) {
                    child = this.getChildAt(newScrollY);
                    if (child.getVisibility() != 8 && ScrollUtils.isConsecutiveScrollerChild(child)) {
                        scrollChild = ScrollUtils.getScrollChild(child);
                        if (scrollChild instanceof IConsecutiveScroller) {
                            views = ((IConsecutiveScroller) scrollChild).getScrolledViews();
                            if (views != null && !views.isEmpty()) {
                                size = views.size();

                                for (c = 0; c < size; ++c) {
                                    this.scrollChildContentToBottom((View) views.get(c));
                                }
                            }
                        } else {
                            this.scrollChildContentToBottom(scrollChild);
                        }
                    }
                }

                for (newScrollY = index + 1; newScrollY < this.getChildCount(); ++newScrollY) {
                    child = this.getChildAt(newScrollY);
                    if (child.getVisibility() != 8 && ScrollUtils.isConsecutiveScrollerChild(child) && (newScrollY != this.getChildCount() - 1 || child.getHeight() >= this.getHeight() || this.getScrollY() < this.mScrollRange)) {
                        scrollChild = ScrollUtils.getScrollChild(child);
                        if (scrollChild instanceof IConsecutiveScroller) {
                            views = ((IConsecutiveScroller) scrollChild).getScrolledViews();
                            if (views != null && !views.isEmpty()) {
                                size = views.size();

                                for (c = 0; c < size; ++c) {
                                    this.scrollChildContentToTop((View) views.get(c));
                                }
                            }
                        } else {
                            this.scrollChildContentToTop(scrollChild);
                        }
                    }
                }

                this.computeOwnScrollOffset();
                if (isLayoutChange) {
                    newScrollY = this.computeVerticalScrollOffset();
                    if (oldScrollY != newScrollY) {
                        this.scrollChange(newScrollY, oldScrollY);
                    }
                }

                this.resetSticky();
            }
        }
    }

    void scrollChildContentToTop(View target) {
        boolean var2 = false;

        int scrollY;
        do {
            scrollY = 0;
            int offset = ScrollUtils.getScrollTopOffset(target);
            if (offset < 0) {
                int childOldScrollY = ScrollUtils.computeVerticalScrollOffset(target);
                this.scrollChild(target, offset);
                scrollY = childOldScrollY - ScrollUtils.computeVerticalScrollOffset(target);
            }
        } while (scrollY != 0);

    }

    void scrollChildContentToBottom(View target) {
        boolean var2 = false;

        int scrollY;
        do {
            scrollY = 0;
            int offset = ScrollUtils.getScrollBottomOffset(target);
            if (offset > 0) {
                int childOldScrollY = ScrollUtils.computeVerticalScrollOffset(target);
                this.scrollChild(target, offset);
                scrollY = childOldScrollY - ScrollUtils.computeVerticalScrollOffset(target);
            }
        } while (scrollY != 0);

    }

    private void computeOwnScrollOffset() {
        this.mSecondScrollY = this.computeVerticalScrollOffset();
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }

    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }

    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }

    }

    private void initOrResetAdjustVelocityTracker() {
        if (this.mAdjustVelocityTracker == null) {
            this.mAdjustVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mAdjustVelocityTracker.clear();
        }

    }

    private void initAdjustVelocityTrackerIfNotExists() {
        if (this.mAdjustVelocityTracker == null) {
            this.mAdjustVelocityTracker = VelocityTracker.obtain();
        }

    }

    private void recycleAdjustVelocityTracker() {
        if (this.mAdjustVelocityTracker != null) {
            this.mAdjustVelocityTracker.recycle();
            this.mAdjustVelocityTracker = null;
        }

    }

    public void stopScroll() {
        if (!this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
            this.stopNestedScroll(1);
            if (this.mScrollToIndex == -1) {
                this.setScrollState(0);
            }
        }

    }

    private View getBottomView() {
        List<View> views = this.getEffectiveChildren();
        return !views.isEmpty() ? (View) views.get(views.size() - 1) : null;
    }

    private List<View> getNonGoneChildren() {
        List<View> children = new ArrayList();
        int count = this.getChildCount();

        for (int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                children.add(child);
            }
        }

        return children;
    }

    private List<View> getEffectiveChildren() {
        List<View> children = new ArrayList();
        int count = this.getChildCount();

        for (int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8 && child.getHeight() > 0) {
                children.add(child);
            }
        }

        return children;
    }

    private List<View> getStickyChildren() {
        List<View> children = new ArrayList();
        int count = this.getChildCount();

        for (int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8 && this.isStickyView(child)) {
                children.add(child);
            }
        }

        return children;
    }

    public boolean isStickyView(View child) {
        android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
        return lp instanceof ConsecutiveScrollerLayout.LayoutParams ? ((ConsecutiveScrollerLayout.LayoutParams) lp).isSticky : false;
    }

    public boolean isSink(View stickyView) {
        android.view.ViewGroup.LayoutParams lp = stickyView.getLayoutParams();
        return lp instanceof ConsecutiveScrollerLayout.LayoutParams ? ((ConsecutiveScrollerLayout.LayoutParams) lp).isSink : false;
    }

    private void resetChildren() {
        List<View> children = this.getNonGoneChildren();
        Iterator var2 = children.iterator();

        while (var2.hasNext()) {
            View child = (View) var2.next();
            child.setTranslationY(0.0F);
        }

    }

    private void resetSticky() {
        List<View> children = this.getStickyChildren();
        if (!children.isEmpty()) {
            int count = children.size();

            View nextStickyView;
            for (int i = 0; i < count; ++i) {
                nextStickyView = (View) children.get(i);
                nextStickyView.setTranslationY(0.0F);
            }

            if (this.isPermanent) {
                this.clearCurrentStickyView();
                this.permanentStickyChild(children);
            } else {
                this.clearCurrentStickyViews();
                View stickyView = null;
                nextStickyView = null;

                for (int i = count - 1; i >= 0; --i) {
                    View child = (View) children.get(i);
                    if (child.getTop() <= this.getStickyY()) {
                        stickyView = child;
                        if (i != count - 1) {
                            nextStickyView = (View) children.get(i + 1);
                        }
                        break;
                    }
                }

                View oldStickyView = this.mCurrentStickyView;
                if (stickyView != null) {
                    int offset = 0;
                    if (nextStickyView != null && !this.isSink(stickyView)) {
                        offset = Math.max(0, stickyView.getHeight() - (nextStickyView.getTop() - this.getStickyY()));
                    }

                    this.stickyChild(stickyView, offset);
                }

                if (oldStickyView != stickyView) {
                    this.mCurrentStickyView = stickyView;
                    this.stickyChange(oldStickyView, stickyView);
                }
            }
        } else {
            this.clearCurrentStickyView();
            this.clearCurrentStickyViews();
        }

    }

    private void clearCurrentStickyView() {
        if (this.mCurrentStickyView != null) {
            View oldStickyView = this.mCurrentStickyView;
            this.mCurrentStickyView = null;
            this.stickyChange(oldStickyView, (View) null);
        }

    }

    private void clearCurrentStickyViews() {
        if (!this.mCurrentStickyViews.isEmpty()) {
            this.mCurrentStickyViews.clear();
            this.permanentStickyChange(this.mCurrentStickyViews);
        }

    }

    private void stickyChild(View child, int offset) {
        child.setY((float) (this.getStickyY() - offset));
        child.setClickable(true);
    }

    private int getStickyY() {
        return this.getScrollY() + this.getPaddingTop() + this.mStickyOffset;
    }

    private void permanentStickyChild(List<View> children) {
        this.mTempStickyViews.clear();

        for (int i = 0; i < children.size(); ++i) {
            View child = (View) children.get(i);
            int permanentHeight = this.getPermanentHeight(children, i);
            if (child.getTop() <= this.getStickyY() + permanentHeight) {
                child.setY((float) (this.getStickyY() + permanentHeight));
                child.setClickable(true);
                this.mTempStickyViews.add(child);
            }
        }

        if (!this.isListEqual()) {
            this.mCurrentStickyViews.clear();
            this.mCurrentStickyViews.addAll(this.mTempStickyViews);
            this.mTempStickyViews.clear();
            this.permanentStickyChange(this.mCurrentStickyViews);
        }

    }

    private int getPermanentHeight(List<View> children, int currentPosition) {
        int height = 0;

        for (int i = 0; i < currentPosition; ++i) {
            View child = (View) children.get(i);
            if (!this.isSink(child)) {
                height += child.getMeasuredHeight();
            }
        }

        return height;
    }

    private boolean isListEqual() {
        if (this.mTempStickyViews.size() == this.mCurrentStickyViews.size()) {
            int size = this.mTempStickyViews.size();

            for (int i = 0; i < size; ++i) {
                if (this.mTempStickyViews.get(i) != this.mCurrentStickyViews.get(i)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    void setScrollState(int state) {
        if (state != this.mScrollState) {
            this.mScrollState = state;
            int newScrollY = this.computeVerticalScrollOffset();
            this.scrollChange(newScrollY, newScrollY);
        }
    }

    public int getOwnScrollY() {
        return this.computeVerticalScrollOffset();
    }

    public View findFirstVisibleView() {
        int offset = this.getScrollY() + this.getPaddingTop();
        List<View> children = this.getEffectiveChildren();
        int count = children.size();

        for (int i = 0; i < count; ++i) {
            View child = (View) children.get(i);
            if (child.getTop() <= offset && child.getBottom() > offset) {
                return child;
            }
        }

        return null;
    }

    public View findLastVisibleView() {
        int offset = this.getHeight() - this.getPaddingBottom() + this.getScrollY();
        List<View> children = this.getEffectiveChildren();
        int count = children.size();

        for (int i = 0; i < count; ++i) {
            View child = (View) children.get(i);
            if (child.getTop() < offset && child.getBottom() >= offset) {
                return child;
            }
        }

        return null;
    }

    public boolean isScrollTop() {
        List<View> children = this.getEffectiveChildren();
        int size = children.size();
        if (size <= 0) {
            return true;
        } else {
            View child = (View) children.get(0);
            boolean isScrollTop = this.getScrollY() <= 0 && !ScrollUtils.canScrollVertically(child, -1);
            if (isScrollTop) {
                for (int i = size - 1; i >= 0; --i) {
                    View view = (View) children.get(i);
                    if (ScrollUtils.isConsecutiveScrollerChild(view) && ScrollUtils.canScrollVertically(view, -1)) {
                        return false;
                    }
                }
            }

            return isScrollTop;
        }
    }

    public boolean isScrollBottom() {
        List<View> children = this.getEffectiveChildren();
        if (children.size() <= 0) {
            return true;
        } else {
            View child = (View) children.get(children.size() - 1);
            return this.getScrollY() >= this.mScrollRange && !ScrollUtils.canScrollVertically(child, 1);
        }
    }

    public boolean canScrollVertically(int direction) {
        if (direction > 0) {
            return !this.isScrollBottom();
        } else {
            return !this.isScrollTop();
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setOnScrollChangeListener(android.view.View.OnScrollChangeListener l) {
    }

    public void setOnVerticalScrollChangeListener(ConsecutiveScrollerLayout.OnScrollChangeListener l) {
        this.mOnScrollChangeListener = l;
    }

    public ConsecutiveScrollerLayout.OnScrollChangeListener getOnVerticalScrollChangeListener() {
        return this.mOnScrollChangeListener;
    }

    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    public int computeVerticalScrollRange() {
        int range = 0;
        List<View> children = this.getNonGoneChildren();
        int count = children.size();

        for (int i = 0; i < count; ++i) {
            View child = (View) children.get(i);
            if (!ScrollUtils.isConsecutiveScrollerChild(child)) {
                range += child.getHeight();
            } else if (ScrollUtils.canScrollVertically(child)) {
                View view = ScrollUtils.getScrolledView(child);
                range += ScrollUtils.computeVerticalScrollRange(view) + view.getPaddingTop() + view.getPaddingBottom();
            } else {
                range += child.getHeight();
            }
        }

        return range;
    }

    public int computeVerticalScrollOffset() {
        int scrollOffset = this.getScrollY();
        List<View> children = this.getNonGoneChildren();
        int count = children.size();

        for (int i = 0; i < count; ++i) {
            View child = (View) children.get(i);
            if (ScrollUtils.isConsecutiveScrollerChild(child)) {
                scrollOffset += ScrollUtils.computeVerticalScrollOffset(child);
            }
        }

        return scrollOffset;
    }

    public int computeVerticalScrollExtent() {
        return this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
    }

    private View getTouchTarget(int touchX, int touchY) {
        View targetView = null;
        List<View> touchableViews = this.getNonGoneChildren();
        Iterator var5 = touchableViews.iterator();

        while (var5.hasNext()) {
            View touchableView = (View) var5.next();
            if (ScrollUtils.isTouchPointInView(touchableView, touchX, touchY)) {
                targetView = touchableView;
                break;
            }
        }

        return targetView;
    }

    private boolean isIntercept(MotionEvent ev) {
        int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
        return pointerIndex >= 0 && pointerIndex < ev.getPointerCount() ? this.isIntercept(ScrollUtils.getRawX(this, ev, pointerIndex), ScrollUtils.getRawY(this, ev, pointerIndex)) : true;
    }

    private boolean isIntercept(int touchX, int touchY) {
        View target = this.getTouchTarget(touchX, touchY);
        return target != null ? ScrollUtils.isConsecutiveScrollerChild(target) : false;
    }

    public void scrollToChild(View view) {
        this.scrollToChildWithOffset(view, 0);
    }

    public void scrollToChildWithOffset(View view, int offset) {
        int scrollToIndex = this.indexOfChild(view);
        if (scrollToIndex != -1) {
            int scrollAnchor = view.getTop() - offset;
            scrollAnchor -= this.getAdjustHeightForChild(view);
            int scrollOrientation = 0;
            if (offset >= 0) {
                if (this.getScrollY() + this.getPaddingTop() > scrollAnchor) {
                    scrollOrientation = -1;
                } else if (this.getScrollY() + this.getPaddingTop() < scrollAnchor) {
                    scrollOrientation = 1;
                } else if (ScrollUtils.canScrollVertically(view, -1)) {
                    scrollOrientation = -1;
                }
            } else {
                int viewScrollOffset = this.getViewsScrollOffset(scrollToIndex);
                if (this.getScrollY() + this.getPaddingTop() + viewScrollOffset > scrollAnchor) {
                    scrollOrientation = -1;
                } else if (this.getScrollY() + this.getPaddingTop() + viewScrollOffset < scrollAnchor) {
                    scrollOrientation = 1;
                }
            }

            if (scrollOrientation != 0) {
                this.mScrollToIndex = scrollToIndex;
                this.stopScroll();
                this.mScrollToIndexWithOffset = offset;
                this.setScrollState(2);

                do {
                    if (scrollOrientation < 0) {
                        this.dispatchScroll(-200);
                    } else {
                        this.dispatchScroll(200);
                    }
                } while (this.mScrollToIndex != -1);
            }
        }

    }

    public void smoothScrollToChild(View view) {
        this.smoothScrollToChildWithOffset(view, 0);
    }

    public void smoothScrollToChildWithOffset(View view, int offset) {
        int scrollToIndex = this.indexOfChild(view);
        if (scrollToIndex != -1) {
            int scrollAnchor = view.getTop() - offset;
            scrollAnchor -= this.getAdjustHeightForChild(view);
            int scrollOrientation = 0;
            if (offset >= 0) {
                if (this.getScrollY() + this.getPaddingTop() > scrollAnchor) {
                    scrollOrientation = -1;
                } else if (this.getScrollY() + this.getPaddingTop() < scrollAnchor) {
                    scrollOrientation = 1;
                } else if (ScrollUtils.canScrollVertically(view, -1)) {
                    scrollOrientation = -1;
                }
            } else {
                int viewScrollOffset = this.getViewsScrollOffset(scrollToIndex);
                if (this.getScrollY() + this.getPaddingTop() + viewScrollOffset > scrollAnchor) {
                    scrollOrientation = -1;
                } else if (this.getScrollY() + this.getPaddingTop() + viewScrollOffset < scrollAnchor) {
                    scrollOrientation = 1;
                }
            }

            if (scrollOrientation != 0) {
                this.mScrollToIndex = scrollToIndex;
                this.stopScroll();
                this.mScrollToIndexWithOffset = offset;
                this.setScrollState(2);
                if (scrollOrientation < 0) {
                    this.mSmoothScrollOffset = -50;
                } else {
                    this.mSmoothScrollOffset = 50;
                }

                this.invalidate();
            }
        }

    }

    private int getViewsScrollOffset(int index) {
        int offset = 0;
        int count = this.getChildCount();

        for (int i = index; i < count; ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() != 8 && ScrollUtils.isConsecutiveScrollerChild(child)) {
                offset += ScrollUtils.computeVerticalScrollOffset(child);
            }
        }

        return offset;
    }

    public boolean isAutoAdjustHeightAtBottomView() {
        return this.mAutoAdjustHeightAtBottomView;
    }

    public void setAutoAdjustHeightAtBottomView(boolean autoAdjustHeightAtBottomView) {
        if (this.mAutoAdjustHeightAtBottomView != autoAdjustHeightAtBottomView) {
            this.mAutoAdjustHeightAtBottomView = autoAdjustHeightAtBottomView;
            this.requestLayout();
        }

    }

    public int getAdjustHeightOffset() {
        return this.mAdjustHeightOffset;
    }

    public void setAdjustHeightOffset(int adjustHeightOffset) {
        if (this.mAdjustHeightOffset != adjustHeightOffset) {
            this.mAdjustHeightOffset = adjustHeightOffset;
            this.requestLayout();
        }

    }

    public void setPermanent(boolean isPermanent) {
        if (this.isPermanent != isPermanent) {
            this.isPermanent = isPermanent;
            if (this.mAutoAdjustHeightAtBottomView) {
                this.requestLayout();
            } else {
                this.resetSticky();
            }
        }

    }

    public boolean isPermanent() {
        return this.isPermanent;
    }

    public void setStickyOffset(int offset) {
        if (this.mStickyOffset != offset) {
            this.mStickyOffset = offset;
            this.resetSticky();
        }

    }

    public int getStickyOffset() {
        return this.mStickyOffset;
    }

    public View getCurrentStickyView() {
        return this.mCurrentStickyView;
    }

    public List<View> getCurrentStickyViews() {
        return this.mCurrentStickyViews;
    }

    public boolean theChildIsStick(View child) {
        return !this.isPermanent && this.mCurrentStickyView == child || this.isPermanent && this.mCurrentStickyViews.contains(child);
    }

    public ConsecutiveScrollerLayout.OnStickyChangeListener getOnStickyChangeListener() {
        return this.mOnStickyChangeListener;
    }

    public void setOnStickyChangeListener(ConsecutiveScrollerLayout.OnStickyChangeListener l) {
        this.mOnStickyChangeListener = l;
    }

    public ConsecutiveScrollerLayout.OnPermanentStickyChangeListener getOnPermanentStickyChangeListener() {
        return this.mOnPermanentStickyChangeListener;
    }

    public void setOnPermanentStickyChangeListener(ConsecutiveScrollerLayout.OnPermanentStickyChangeListener l) {
        this.mOnPermanentStickyChangeListener = l;
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        this.mChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return this.mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return this.mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public boolean startNestedScroll(int axes, int type) {
        return this.mChildHelper.startNestedScroll(axes, type);
    }

    public void stopNestedScroll(int type) {
        this.mChildHelper.stopNestedScroll(type);
    }

    public void stopNestedScroll() {
        this.stopNestedScroll(0);
    }

    public boolean hasNestedScrollingParent(int type) {
        return this.mChildHelper.hasNestedScrollingParent(type);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return this.mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return this.mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return this.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, 0);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return this.mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        boolean isNestedScroll = false;
        android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp instanceof ConsecutiveScrollerLayout.LayoutParams) {
            isNestedScroll = ((ConsecutiveScrollerLayout.LayoutParams) lp).isNestedScroll;
        }

        if (isNestedScroll) {
            return (axes & 2) != 0;
        } else {
            return false;
        }
    }

    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        this.mParentHelper.onNestedScrollAccepted(child, target, axes, type);
        this.checkTargetsScroll(false, false);
        this.startNestedScroll(2, type);
    }

    public void onStopNestedScroll(@NonNull View target, int type) {
        this.mParentHelper.onStopNestedScroll(target, type);
        this.stopNestedScroll(type);
    }

    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        this.onNestedScrollInternal(dyUnconsumed, type);
    }

    private void onNestedScrollInternal(int dyUnconsumed, int type) {
        int oldScrollY = this.mSecondScrollY;
        this.dispatchScroll(dyUnconsumed);
        int myConsumed = this.mSecondScrollY - oldScrollY;
        int myUnconsumed = dyUnconsumed - myConsumed;
        this.mChildHelper.dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, (int[]) null, type);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return this.onStartNestedScroll(child, target, nestedScrollAxes, 0);
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        this.onNestedScrollAccepted(child, target, nestedScrollAxes, 0);
    }

    public void onStopNestedScroll(View target) {
        this.onStopNestedScroll(target, 0);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        this.onNestedScrollInternal(dyUnconsumed, 0);
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        this.onNestedPreScroll(target, dx, dy, consumed, 0);
    }

    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        this.dispatchNestedPreScroll(dx, dy, consumed, (int[]) null, type);
    }

    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            this.dispatchNestedFling(0.0F, velocityY, true);
            this.fling((int) velocityY);
            return true;
        } else {
            return false;
        }
    }

    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return this.dispatchNestedPreFling(velocityX, velocityY);
    }

    public interface OnPermanentStickyChangeListener {
        void onStickyChange(@NonNull List<View> var1);
    }

    public interface OnStickyChangeListener {
        void onStickyChange(@Nullable View var1, @Nullable View var2);
    }

    public interface OnScrollChangeListener {
        void onScrollChange(View var1, int var2, int var3, int var4);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public boolean isConsecutive = true;
        public boolean isNestedScroll = true;
        public boolean isSticky = false;
        public boolean isTriggerScroll = false;
        public boolean isSink = false;
        public int scrollChild;
        public ConsecutiveScrollerLayout.LayoutParams.Align align;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.align = ConsecutiveScrollerLayout.LayoutParams.Align.LEFT;
            TypedArray a = null;

            try {
                a = c.obtainStyledAttributes(attrs, R.styleable.ConsecutiveScrollerLayout_Layout);
                this.isConsecutive = a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isConsecutive, true);
                this.isNestedScroll = a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isNestedScroll, true);
                this.isSticky = a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isSticky, false);
                this.isTriggerScroll = a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isTriggerScroll, false);
                this.isSink = a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isSink, false);
                int type = a.getInt(R.styleable.ConsecutiveScrollerLayout_Layout_layout_align, 1);
                this.align = ConsecutiveScrollerLayout.LayoutParams.Align.get(type);
                this.scrollChild = a.getResourceId(R.styleable.ConsecutiveScrollerLayout_Layout_layout_scrollChild, 0);
            } catch (Exception var8) {
                var8.printStackTrace();
            } finally {
                if (a != null) {
                    a.recycle();
                }

            }

        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.align = ConsecutiveScrollerLayout.LayoutParams.Align.LEFT;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.align = ConsecutiveScrollerLayout.LayoutParams.Align.LEFT;
        }

        public static enum Align {
            LEFT(1),
            RIGHT(2),
            CENTER(3);

            int value;

            private Align(int value) {
                this.value = value;
            }

            static ConsecutiveScrollerLayout.LayoutParams.Align get(int value) {
                switch (value) {
                    case 1:
                        return LEFT;
                    case 2:
                        return RIGHT;
                    case 3:
                        return CENTER;
                    default:
                        return LEFT;
                }
            }
        }
    }
}
