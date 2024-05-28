package com.jmbon.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;

public class ChatGroupTextSwitcher extends BaseTextSwitcher {


    public ChatGroupTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 工厂类中创建TextView供给TextSwitcher使用
     *
     * @return
     */
    @Override
    public View makeView() {
        return createDefaultView();
    }


    @NonNull
    private TextView createDefaultView() {
        TextDrawable more = new TextDrawable(
                "医生刚来过",
                SizeUtils.sp2px(14f),
                ColorUtils.getColor(R.color.white),
                Color.TRANSPARENT,
                0,
                0
        );
        TextView view = new TextView(getContext());
        view.setSingleLine();
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setTextSize(14f);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setTextColor(ColorUtils.getColor(R.color.white));
        view.setCompoundDrawablesRelative (null,null,more,null);
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return view;
    }

}
