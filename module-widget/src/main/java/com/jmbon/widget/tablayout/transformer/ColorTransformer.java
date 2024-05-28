package com.jmbon.widget.tablayout.transformer;

import android.util.Log;
import android.widget.TextView;

import com.jmbon.widget.tablayout.SlidingScaleTabLayout;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by li.zhipeng on 2019/1/3.
 * <p>
 * tab切换的
 */
public class ColorTransformer implements ITabScaleTransformer {
    private static final String TAG = "ColorTransformer";
    ArrayList<Integer> color;
    private final SlidingScaleTabLayout slidingScaleTabLayout;
    private boolean openDmg;

    public ColorTransformer(SlidingScaleTabLayout slidingScaleTabLayout, ArrayList<Integer> color) {
        this.slidingScaleTabLayout = slidingScaleTabLayout;
        this.color = color;
    }

    @Override
    public void setNormalWidth(int position, int width, boolean isSelect) {
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "position:" + position);
        changeTextColor(position, positionOffset);
        if (position + 1 < slidingScaleTabLayout.getTabCount()) {
            changeTextColor(position + 1, 1 - positionOffset);
        }
    }

    private void changeTextColor(final int position, final float positionOffset) {
        final TextView currentTab = slidingScaleTabLayout.getTitle(position);/**/
        currentTab.post(new Runnable() {
            @Override
            public void run() {
                BigDecimal bigDecimal = new BigDecimal(positionOffset * 10);
                bigDecimal.setScale(1, BigDecimal.ROUND_DOWN);
                int index = bigDecimal.intValue();
                if (index == 10) {
                    index = 9;
                }
                currentTab.setTextColor(color.get(index));
                currentTab.requestLayout();
            }
        });
    }

}