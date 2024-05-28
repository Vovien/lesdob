package com.apkdv.mvvmfast.utils.divider;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private final int spacing;
    private int topspacing;
    private int bottomspacing;
    private int headspacing;
    private final boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    public GridSpacingItemDecoration(int spanCount, int spacing, int topspacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.topspacing = topspacing;
        this.includeEdge = includeEdge;
    }


    public GridSpacingItemDecoration(int spanCount, int spacing, int topspacing, int bottomspacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.topspacing = topspacing;
        this.bottomspacing = bottomspacing;
        this.includeEdge = includeEdge;
    }

    public GridSpacingItemDecoration(int spanCount, int spacing, int topspacing, int headspacing, int bottomspacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.topspacing = topspacing;
        this.headspacing = headspacing;
        this.bottomspacing = bottomspacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
        } else {
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
        }

        if (topspacing > 0) {
            if (position < spanCount) {
                if (headspacing > 0) {
                    outRect.top = headspacing;
                } else {
                    outRect.top = topspacing;
                }
            }

            if (bottomspacing > 0) {
                outRect.bottom = bottomspacing;
            } else {
                outRect.bottom = spacing;
            }
        } else {
            if (position < spanCount) {
                outRect.top = spacing;
            }
            if (bottomspacing > 0) {
                outRect.bottom = bottomspacing;
            } else {
                outRect.bottom = spacing;
            }
        }
    }
}