package com.jmbon.middleware.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.jmbon.middleware.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/8 10:56
 */
public class SideIndexBar extends View {
    private static final String[] DEFAULT_INDEX_ITEMS = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private List<String> mIndexItems;
    private float mItemHeight; //每个index的高度
    private int mTextSize;      //sp
    private int mTextColor;
    private int mTextTouchedColor;
    private int mCurrentIndex = -1;

    private Paint mPaint;
    private Paint mTouchedPaint;

    private int mWidth;
    private int mHeight;
    private float mTopMargin;   //居中绘制，文字绘制起点和控件顶部的间隔

    private TextView mOverlayTextView;
    private OnIndexTouchedChangedListener mOnIndexChangedListener;

    private int navigationBarHeight;

    public void setNavigationBarHeight(int height) {
        this.navigationBarHeight = height;
    }

    public SideIndexBar(Context context) {
        this(context, null);
    }

    public SideIndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mIndexItems = new ArrayList<>();
        mIndexItems.addAll(Arrays.asList(DEFAULT_INDEX_ITEMS));

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SideIndexBar);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.SideIndexBar_cpIndexBarTextSize, SizeUtils.sp2px(12));
        mTextColor = typedArray.getColor(R.styleable.SideIndexBar_cpIndexBarNormalTextColor, Color.parseColor("#262626"));
        mTextTouchedColor = typedArray.getColor(R.styleable.SideIndexBar_cpIndexBarSelectedTextColor, Color.parseColor("#262626"));
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTouchedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTouchedPaint.setTextSize(mTextSize);
        mTouchedPaint.setColor(mTextTouchedColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String index;
        for (int i = 0; i < mIndexItems.size(); i++) {
            index = mIndexItems.get(i);
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            canvas.drawText(index,
                    (mWidth - mPaint.measureText(index)) / 2,
                    mItemHeight / 2 + (fm.bottom - fm.top) / 2 - fm.bottom + mItemHeight * i + mTopMargin,
                    i == mCurrentIndex ? mTouchedPaint : mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        if (Math.abs(h - oldh) == navigationBarHeight) {
            //底部导航栏隐藏或显示
            mHeight = h;
        } else {
            //避免软键盘弹出时挤压
            mHeight = Math.max(getHeight(), oldh);
        }
        mItemHeight = mHeight / mIndexItems.size();
        mTopMargin = (mHeight - mItemHeight * mIndexItems.size()) / 2;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int indexSize = mIndexItems.size();
                int touchedIndex = (int) (y / mItemHeight);
                if (touchedIndex < 0) {
                    touchedIndex = 0;
                } else if (touchedIndex >= indexSize) {
                    touchedIndex = indexSize - 1;
                }
                if (mOnIndexChangedListener != null && touchedIndex >= 0 && touchedIndex < indexSize) {
                    if (touchedIndex != mCurrentIndex) {
                        mCurrentIndex = touchedIndex;
                        if (mOverlayTextView != null) {
                            mOverlayTextView.setVisibility(VISIBLE);
                            mOverlayTextView.setText(mIndexItems.get(touchedIndex));
                        }
                        mOnIndexChangedListener.onIndexChanged(mIndexItems.get(touchedIndex), touchedIndex);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentIndex = -1;
                if (mOverlayTextView != null) {
                    mOverlayTextView.setVisibility(GONE);
                }
                invalidate();
                break;
        }
        return true;
    }

    public SideIndexBar setOverlayTextView(TextView overlay) {
        this.mOverlayTextView = overlay;
        return this;
    }

    public SideIndexBar setOnIndexChangedListener(OnIndexTouchedChangedListener listener) {
        this.mOnIndexChangedListener = listener;
        return this;
    }

    public interface OnIndexTouchedChangedListener {
        void onIndexChanged(String index, int position);
    }
}
