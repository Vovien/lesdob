package com.jmbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


/**
 * Created by reiserx on 2017/6/13.
 * desc : 可以清除的TextInputEditText
 */

public class ClearableEditText extends TextInputEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;
    private OnClickListener mIconClickListener;
    private Context mContext;
    private float mPaddingLeft;

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public ClearableEditText(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 默认值
     */
    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray at = context.getResources().obtainAttributes(attrs, R.styleable.ClearableEditText);
        int resId;
        try {
            resId = at.getResourceId(R.styleable.ClearableEditText_clearIcon, R.drawable.icon_edit_clean);
            mPaddingLeft = at.getDimension(R.styleable.ClearableEditText_clearIconPaddingLeft, 0);
        } finally {
            at.recycle();
        }
        mClearTextIcon = getDrawable(resId);
        //设置宽高
        mClearTextIcon.setBounds(0, 0, SizeUtils.dp2px(24), SizeUtils.dp2px(24));

        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /**
     * 设施清除按钮可见
     *
     * @param visible 可见
     */
    private void setClearIconVisible(final boolean visible) {
        if (mClearTextIcon != null) {
            mClearTextIcon.setVisible(visible, false);
            final Drawable[] compoundDrawables = getCompoundDrawables();
            final Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            if (mPaddingLeft > 0) {

            } else {
                setCompoundDrawables(compoundDrawables[0] == null ?
                                compoundDrawablesRelative[0] : compoundDrawables[0],
                        compoundDrawables[1] == null ? compoundDrawablesRelative[1] : compoundDrawables[1],
                        visible ? mClearTextIcon : null,compoundDrawables[3] == null ?
                                compoundDrawablesRelative[3] : compoundDrawables[3]);
            }
        }
    }


    /**
     * 设置清除图标
     *
     * @param resId 清除样式图标
     */
    public void setClearIcon(int resId) {
        mClearTextIcon = getDrawable(resId);
        setClearIconVisible(true);
    }

    /**
     * 获取图标Drawable
     *
     * @param resId 资源id
     * @return 图标Drawable
     */
    private Drawable getDrawable(int resId) {
        //封装drawable对象
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
//        //简单着色
//        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        return wrappedDrawable;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setClearIconVisible(Objects.requireNonNull(getText()).length() > 0);
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
                if (mIconClickListener != null)
                    mIconClickListener.onClick(v);
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        setClearIconVisible(s.length() > 0);
    }

    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public void setIconClickListener(final OnClickListener clickListener) {
        mIconClickListener = clickListener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        setClearIconVisible(enabled);
        super.setEnabled(enabled);
    }
}

