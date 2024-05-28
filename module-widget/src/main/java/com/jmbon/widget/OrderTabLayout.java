package com.jmbon.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.List;

/**
 * @author : leimg
 * time   : 2021/4/25
 * desc   :
 * version: 1.0
 */
public class OrderTabLayout extends RelativeLayout {
    private final int padding2 = SizeUtils.dp2px(2);
    private final int padding5 = SizeUtils.dp2px(5);
    private final int padding4 = SizeUtils.dp2px(4);
    private final int padding6 = SizeUtils.dp2px(6);
    private final int padding7 = SizeUtils.dp2px(7);

    private @DrawableRes
    int ivIndicatorBg = R.drawable.answer_order_choice_bg;

    private @DrawableRes
    final
    int backgroudDrawable = R.drawable.answer_order_bg;

    private int selectedTextColor = R.color.color_262626;
    private int normalTextColor = R.color.color_BFBFBF;

    private ImageView ivIndicator;
    private LinearLayout linearLayout;
    private boolean isBold = false;

    //默认排序方式
    private int defaultOrderType = 0;
    private SelectedClickListener selectedClickListener;
    OnClickListener onClickListener = this::updateTab;

    private void updateTab(View v) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            if (linearLayout.getChildAt(i).getId() == v.getId()) {
                textView.setTextColor(getResources().getColor(selectedTextColor));

                int margin = 0;
                if (v.getLayoutParams() != null) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) v.getLayoutParams();
                    margin = layoutParams.leftMargin + layoutParams.rightMargin;
                }

                int finalMargin = margin;
                int finalI = i;
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        setAnimate((v.getWidth() + finalMargin) * finalI);
                    }
                });

                if (selectedClickListener != null) {
                    //俩次点击不同才回调
                    if (defaultOrderType != i) {
                        defaultOrderType = i;
                        selectedClickListener.onSelected(i);
                    }
                }
            } else {
                textView.setTextColor(getResources().getColor(normalTextColor));
            }
        }
    }

    public OrderTabLayout(Context context) {
        this(context, null);
    }

    public OrderTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0, 0);
    }

    public OrderTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView();
    }

    private void initView() {
        setPadding(padding5, padding4, padding5, padding4);
        setBackgroundResource(backgroudDrawable);
    }


    /**
     * 是否自动刷新当前状态
     * 如果当前已有默认排序且非第一个，就保持当前选中状态
     *
     * @param title
     * @param isAutoRefresh
     */
    public void initTabView(List<String> title, boolean isAutoRefresh) {
        if (isAutoRefresh && defaultOrderType != 0) {
            setSelected(defaultOrderType);
        } else {
            initTabView(title);
        }
    }

    /**
     * 均分布局
     */
    public void splitView() {
        if (linearLayout == null || ivIndicator == null || linearLayout.getChildCount() <= 0) {
            throw new RuntimeException("必须先初始化 Tab ");
        }
        // 指示器
        ivIndicator.post(() -> {
            LayoutParams tabParams = (LayoutParams) ivIndicator.getLayoutParams();
            tabParams.width = getWidth() / 2 - SizeUtils.dp2px(5); // 5 padding
            ivIndicator.setLayoutParams(tabParams);
        });
        // tab
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = -1;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getChildAt(i).getLayoutParams();
            params.width = 0;
            params.weight = 1;
            ((TextView) linearLayout.getChildAt(i)).setGravity(Gravity.CENTER);
        }
    }

    public void initTabView(List<String> title) {

        if (title == null || title.size() == 0) {
            return;
        }
        removeAllViews();

        //先添加默认指示器，层级在下面
        ivIndicator = new ImageView(getContext());
        ivIndicator.setBackgroundResource(ivIndicatorBg);
        RelativeLayout.LayoutParams ivLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivLayoutParams.width = SizeUtils.dp2px(title.get(0).length() * 12 + 7 * 2);
        ivLayoutParams.height = SizeUtils.dp2px(16 + 6 * 2);
        ivIndicator.setLayoutParams(ivLayoutParams);
        addView(ivIndicator);

        ivIndicator.setOnClickListener(v -> LogUtils.e("OrderTabLayout 点击了ivIndicator"));

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);


        for (int i = 0; i < title.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(title.get(i));
            if (i == 0) {
//                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setTextColor(getResources().getColor(selectedTextColor));
            } else {
//                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setTextColor(getResources().getColor(normalTextColor));
            }
            textView.setText(title.get(i));
            textView.setTextSize(12);

            //设置id
            textView.setId(i + 100);

            textView.setPadding(padding7, padding6, padding7, padding6);

            ClickUtils.applyGlobalDebouncing(textView, 400, onClickListener);

            linearLayout.addView(textView);
        }

        addView(linearLayout);
    }

    public void initTabView2(List<String> title) {

        if (title == null || title.size() == 0) {
            return;
        }
        removeAllViews();

        //先添加默认指示器，层级在下面
        ivIndicator = new ImageView(getContext());
        ivIndicator.setBackgroundResource(ivIndicatorBg);
        RelativeLayout.LayoutParams ivLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivLayoutParams.width = SizeUtils.dp2px(title.get(0).length() * 12 + 4 * 2);
        ivLayoutParams.height = SizeUtils.dp2px(12 + 4 * 2);
        ivIndicator.setLayoutParams(ivLayoutParams);
        addView(ivIndicator);

        ivIndicator.setOnClickListener(v -> LogUtils.e("OrderTabLayout 点击了ivIndicator"));

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < title.size(); i++) {
            TextView textView = new TextView(getContext());
            if (i == 0) {
                textView.setTextColor(getResources().getColor(selectedTextColor));
            } else {
                textView.setTextColor(getResources().getColor(normalTextColor));
            }
            textView.setText(title.get(i));
            textView.setTextSize(12);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            //设置id
            textView.setId(i + 100);
            textView.setPadding(padding4, padding2, padding4, padding2);
            if (i != 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = SizeUtils.dp2px(12);
                textView.setLayoutParams(layoutParams);
            }
            ClickUtils.applyGlobalDebouncing(textView, 400, onClickListener);

            linearLayout.addView(textView);
        }

        addView(linearLayout);
    }


    //设置指示器背景 initTabView前调用
    public void setIvIndicatorBg(int ivIndicatorBg) {
        this.ivIndicatorBg = ivIndicatorBg;
    }

    //设置默认文本颜色  initTabView前调用
    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    //设置选中文本颜色  initTabView前调用
    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public void setSelected(int position) {
        if (linearLayout != null && position < linearLayout.getChildCount()) {
            //设置选择项
            updateTab(linearLayout.getChildAt(position));
        }
    }

    /**
     * 属性动画
     * 平移
     */
    private void setAnimate(float transOffset) {
        //      创建属性动画对象，并设置移动的方向和偏移量
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivIndicator, "translationX", transOffset);
        //      设置移动时间
        objectAnimator.setDuration(300);
        //      开始动画
        objectAnimator.start();
    }

    public void setSelectedClickListener(SelectedClickListener selectedClickListener) {
        this.selectedClickListener = selectedClickListener;
    }

    public int getCurrentTab() {
        return defaultOrderType;
    }

    public interface SelectedClickListener {
        void onSelected(int type);
    }


    public void setBold(boolean bold) {
        isBold = bold;
    }
}
