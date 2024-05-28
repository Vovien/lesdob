package com.jmbon.widget;

import static java.lang.Math.max;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.ColorUtils;

import kotlin.jvm.functions.Function0;

public class BaseTextSwitcher extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private String[] mData;
    private final long DEFAULT_TIME_SWITCH_INTERVAL = 1000;//1s
    private long mTimeInterval = DEFAULT_TIME_SWITCH_INTERVAL;
    private int mCurrentIndex = 0;
    private Lifecycle lifecycle = null;
    private int textColor = 0;
    private float textSize = 16f;

    private final Function0<View> makeView = null;

    public BaseTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttr(context, attrs);

        setFactory(this);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseTextSwitcher);
        textColor = typedArray.getColor(R.styleable.BaseTextSwitcher_switch_text_color, context.getResources().getColor(R.color.color_7F7F7F));
        textSize = typedArray.getFloat(R.styleable.BaseTextSwitcher_switch_text_size, 14f);
        typedArray.recycle();
    }

    public BaseTextSwitcher setInAnimation(int animationResId) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), animationResId);
        setInAnimation(animation);
        return this;
    }

    public BaseTextSwitcher setOutAnimation(int animationResId) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), animationResId);
        setOutAnimation(animation);
        return this;
    }

    /**
     * 通知/公告数据绑定
     *
     * @param data
     * @return
     */
    public BaseTextSwitcher bindData(String[] data) {
        this.mData = data;
        return this;
    }

    public String[] getmData() {
        return mData;
    }

    public void startSwitch(long timeInterval) {
        this.mTimeInterval = timeInterval;
        if (mData != null && mData.length != 0) {
            mSwitchHandler.sendEmptyMessage(0);
        } else {
            throw new RuntimeException("data is empty");
        }
    }

    public BaseTextSwitcher setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        return this;
    }

    /**
     * 工厂类中创建TextView供给TextSwitcher使用
     *
     * @return
     */
    @Override
    public View makeView() {
        return makeView == null ? createDefaultView() : makeView.invoke();
    }


    public String getCurrentData() {
        try {
            if (mData != null && mData.length > 0) {
                int index = max((mCurrentIndex - 1), 0) % mData.length;
                return createText(index);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    @NonNull
    private TextView createDefaultView() {
        TextView view = new TextView(getContext());
        view.setSingleLine();
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setTextSize(textSize);
        view.setEllipsize(TextUtils.TruncateAt.START);
        view.setTextColor(textColor);
        return view;
    }

    private final Handler mSwitchHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (lifecycle != null) {
                if (lifecycle.getCurrentState() == Lifecycle.State.RESUMED)
                    extracted();
            } else
                extracted();
            sendEmptyMessageDelayed(0, mTimeInterval);
        }
    };

    private void extracted() {
        int index = mCurrentIndex % mData.length;
        mCurrentIndex++;
        setText(createText(index));
    }

    public String createText(int index) {
        return mData[index];
    }


    public void cancel() {
        if (mSwitchHandler != null) {
            mSwitchHandler.removeMessages(0);
        }
    }


}
