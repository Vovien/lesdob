package com.jmbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class SolveEditTextScroll extends AppCompatEditText {


    public SolveEditTextScroll(@NonNull Context context) {
        super(context);
    }

    public SolveEditTextScroll(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SolveEditTextScroll(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (action == MotionEvent.ACTION_MOVE) {
            getParent().requestDisallowInterceptTouchEvent(canVerticalScroll(this));
        } else if (action == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.onTouchEvent(event);
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}