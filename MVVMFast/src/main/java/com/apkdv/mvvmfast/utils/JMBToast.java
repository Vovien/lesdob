package com.apkdv.mvvmfast.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.apkdv.mvvmfast.R;
import com.blankj.utilcode.util.SizeUtils;
import com.hjq.toast.style.BlackToastStyle;

public class JMBToast extends BlackToastStyle {
    @Override
    protected int getTextColor(Context context) {
        return 0xD9FFFFFF;
    }

    @Override
    protected float getTextSize(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 17, context.getResources().getDisplayMetrics());
    }

    @Override
    protected int getHorizontalPadding(Context context) {
        return SizeUtils.dp2px(20f);
    }

    @Override
    protected int getVerticalPadding(Context context) {
        return SizeUtils.dp2px(16f);
    }

    @Override
    protected Drawable getBackgroundDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.toast_background);
    }
}
