package com.jmbon.video.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by Mr_Zeng
 *
 * @date 2018/12/5
 */
public class VideoStaggeredSpaceDecoration extends RecyclerView.ItemDecoration {

    private int interval;

    public VideoStaggeredSpaceDecoration(int interval) {
        this.interval = interval;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int position = parent.getChildAdapterPosition(view);
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        // 获取item在span中的下标
        int spanIndex = params.getSpanIndex();
        // 中间间隔
        if (spanIndex % 2 == 0) {
            outRect.right = interval;
        } else {
            // item为奇数位，设置其左间隔为5dp
            outRect.left = interval;
        }
        // 下方间隔
        outRect.bottom = interval * 2;
    }
}