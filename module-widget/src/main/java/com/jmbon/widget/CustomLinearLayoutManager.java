package com.jmbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author : leimg
 * time   : 2021/7/12
 * desc   : java.lang.IndexOutOfBoundsException: Inconsistency detected.
 * 原因在于RecyclerView 下拉刷新时，若绑定的 List 对象在更新数据之前进行了 clear，而这时用户紧接着迅速上滑 RV，就会造成崩溃
 * version: 1.0
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 重写该方法，去捕捉该异常
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

}
