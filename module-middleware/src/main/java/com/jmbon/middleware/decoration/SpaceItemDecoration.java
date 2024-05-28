package com.jmbon.middleware.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;
    private final int firstMargin;

    public SpaceItemDecoration(int firstMargin, int space) {
        this.space = space;
        this.firstMargin = firstMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        int itemOffset2 = dp2px(parent.getContext(), space >> 1);
        int itemOffset = dp2px(parent.getContext(), firstMargin);
        outRect.right = itemOffset2;
        outRect.left = itemOffset2;
        outRect.top = itemOffset2;
        outRect.bottom = itemOffset2;
        if (parent.getChildLayoutPosition(view) == 0) {
            //头部header
            outRect.bottom = itemOffset;
        }
//        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
//        if (parent.getChildLayoutPosition(view) % 3 == 0) {
//        }
//       else if (parent.getChildLayoutPosition(view) % 3 == 1) {
//        } else if (parent.getChildLayoutPosition(view) % 3 == 2) {
//        }
    }

    protected int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}