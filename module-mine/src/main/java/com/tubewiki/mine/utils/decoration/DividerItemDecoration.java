package com.tubewiki.mine.utils.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private float dividerHeight;
    private Paint mPaint;

    public DividerItemDecoration() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#08000000"));
        dividerHeight = SizeUtils.dp2px(1f);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = (int) dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft() + SizeUtils.dp2px(20);
        int right = parent.getWidth() - parent.getPaddingRight() - SizeUtils.dp2px(20);

        for (int i = 1; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
