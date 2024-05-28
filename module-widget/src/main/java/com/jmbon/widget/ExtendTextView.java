package com.jmbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;


/**
 * 折叠TextView
 * 当TextView行数大于{@link ExtendTextView#maxLine} 折叠情况只显示 maxLine行
 * 当行数小于{@link ExtendTextView#maxLine}则显示实际行数，并隐藏折叠指示器
 */
public class ExtendTextView extends RelativeLayout implements ExtendView {

    private static final String TAG = "ExpandTextView";

    private final static int STATUS_EXPEND = 1;

    private final static int STATUS_FOLD = 2;

    private int status = STATUS_FOLD;

    private TextView mTextView, indicatorText;

    private ImageView ivIndicator;
    private LinearLayout indicatorLayout;

    //折叠时 显示的函数
    private int maxLine = 3;

    private final int lineSpace = SizeUtils.dp2px(3f);

    private long mAnimationDuration = 1000;

    private String text, openStr = "收起简介", closeStr = "全部简介";

    private final int textSize = 15;
    private Drawable mIndicatorSrc;

    private int lineCount = 0;

    private boolean clickable = false;

    private int mTextColor;
    private float mTextSize;
    private float mTextLineSpace;
    private String mText;

    private float mLeftMargin = 0;
    private float mRightMargin = mLeftMargin;
    private int mIndicatorPosition;

    private float mTextPadding = 0;
    private float mTextPaddingLeft = mTextPadding;
    private float mTextPaddingRight = mTextPadding;
    private float mTextPaddingTop = mTextPadding;
    private float mTextPaddingBottom = mTextPadding;


    private OnExtendListener mListener;

    private final float mSpacingMult = 1;


    public ExtendTextView(Context context) {
        this(context, null);
    }

    public ExtendTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        initView(context);
        initListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtendTextView);

        mTextColor = typedArray.getColor(R.styleable.ExtendTextView_text_color, Color.BLACK);
        mTextSize = typedArray.getDimension(R.styleable.ExtendTextView_text_size, mLeftMargin);
        mTextLineSpace = typedArray.getDimension(R.styleable.ExtendTextView_text_line_space, SizeUtils.dp2px(3f));
        CharSequence text = typedArray.getText(R.styleable.ExtendTextView_text);
        if (text != null) {
            mText = text.toString();
        }

        mTextPadding = typedArray.getDimension(R.styleable.ExtendTextView_text_padding, 0);
        mIndicatorSrc = typedArray.getDrawable(R.styleable.ExtendTextView_indicator);
        mAnimationDuration = typedArray.getInteger(R.styleable.ExtendTextView_animation_duration, 1000);

        mTextPaddingLeft = typedArray.getDimension(R.styleable.ExtendTextView_text_padding_left, mLeftMargin);
        mTextPaddingRight = typedArray.getDimension(R.styleable.ExtendTextView_text_padding_right, mLeftMargin);
        mTextPaddingTop = typedArray.getDimension(R.styleable.ExtendTextView_text_padding_top, mLeftMargin);
        mTextPaddingBottom = typedArray.getDimension(R.styleable.ExtendTextView_text_padding_bottom, mLeftMargin);
        mLeftMargin = typedArray.getDimension(R.styleable.ExtendTextView_indicator_margin_left, mLeftMargin);
        mRightMargin = typedArray.getDimension(R.styleable.ExtendTextView_indicator_margin_right, mLeftMargin);

        mIndicatorPosition = typedArray.getInt(R.styleable.ExtendTextView_indicator_position, CENTER_HORIZONTAL);
        typedArray.recycle();
    }

    private void initListener() {

        setOnClickListener(v -> {
            if (!clickable) {
                return;
            }
            mTextView.clearAnimation();
            if (status == STATUS_FOLD) {
                startExtend();
            } else {
                startFold();
            }

        });
    }

    private void startFold() {
        status = STATUS_FOLD;
        if (mListener != null) {
            mListener.fold();
        }
        foldIndicatorAnimator();
        changeParams(3);
        textAnimation(mTextView.getHeight(), calculateHeight(maxLine) - mTextView.getHeight(), false);

        if (mListener != null) {
            mListener.changeValue(-calculateHeight(mTextView.getLineCount()));
        }
    }

    private void startExtend() {
        status = STATUS_EXPEND;
        if (mListener != null) {
            mListener.expand();
        }
        extendIndicatorAnimation();
        changeParams(8);
        textAnimation(mTextView.getHeight(), calculateHeight(mTextView.getLineCount()) - mTextView.getHeight(), true);
      //
        //  textAnimation(mTextView.getHeight(),  - mTextView.getHeight(), true);

        if (mListener != null) {
            mListener.changeValue(calculateHeight(mTextView.getLineCount()));
        }
    }

    private void changeParams(int size) {
        LayoutParams params = (LayoutParams) indicatorLayout.getLayoutParams();
        params.topMargin = SizeUtils.dp2px(size);
        indicatorLayout.setLayoutParams(params);
    }

    private int calculateHeight(int lineCount) {
        if (mTextView.getLayout() != null)
            return mTextView.getLayout().getLineTop(lineCount) + mTextView.getCompoundPaddingTop() + mTextView.getCompoundPaddingBottom();
        else return 0;
        //return (int) (lineCount * mTextView.getLineHeight() + lineCount * mLineSpaceHeight + mTextView.getPaddingTop() + mTextView.getPaddingBottom());
    }


    /**
     * 每次调用{@link ExtendTextView#setText(CharSequence)}函数，需要重新获取行数，进行TextView高度设置
     */
    public void registerGlobalLayoutListener() {
        mTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lineCount = mTextView.getLineCount();

                if (lineCount < maxLine || lineCount == maxLine) {
                    clickable = false;
                    mTextView.setHeight(calculateHeight(mTextView.getLineCount()));
                    indicatorLayout.setVisibility(View.GONE);
                } else {
                    clickable = true;
                    indicatorLayout.setVisibility(View.VISIBLE);
                    mTextView.setHeight(calculateHeight(maxLine));
                    mTextView.setEllipsize(TextUtils.TruncateAt.END);
                    setFoldEllipsize();

                }
                mTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    public String getText() {
        return mTextView.getText().toString();
    }

    public void setText(CharSequence sequence) {
        mText = sequence.toString();
        mTextView.setText(sequence);
        registerGlobalLayoutListener();
    }

    private void initView(Context mContext) {
        createTextView(mContext);
        createIndicatorView();
    }

    private void createTextView(Context mContext) {
        mTextView = new TextView(mContext);
        mTextView.setId(View.generateViewId());
        addView(mTextView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        mTextView.setMaxLines(maxLine);
        mTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mTextView.setText(mText);
        mTextView.setLineSpacing(mTextLineSpace, mSpacingMult);
        mTextView.setTextColor(mTextColor);
        if (mTextPadding == 0) {
            mTextView.setPadding((int) mTextPadding, (int) mTextPadding, (int) mTextPadding, (int) mTextPadding);
        } else {
            mTextView.setPadding((int) mTextPaddingLeft, (int) mTextPaddingTop, (int) mTextPaddingRight, (int) mTextPaddingBottom);
        }

        registerGlobalLayoutListener();
    }

    private void createIndicatorView() {
        indicatorLayout = new LinearLayout(getContext());
        indicatorLayout.setOrientation(LinearLayout.HORIZONTAL);
        ivIndicator = new ImageView(getContext());

        if (mIndicatorSrc != null) {
            ivIndicator.setImageDrawable(mIndicatorSrc);
        } else {
            ivIndicator.setImageResource(R.drawable.article_detail_down_icon);
        }
        indicatorText = new TextView(getContext());
        indicatorText.setTextColor(ColorUtils.getColor(R.color.color_262626));
        indicatorText.setTextSize(14);
        indicatorText.setText(closeStr);
        indicatorLayout.addView(ivIndicator);
        indicatorLayout.addView(indicatorText);
        addView(indicatorLayout);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.addRule(BELOW, mTextView.getId());
        layoutParams.topMargin = SizeUtils.dp2px(4f);
        indicatorLayout.setLayoutParams(layoutParams);
        setIndicatorPosition(mIndicatorPosition);
    }

    @Override
    public void extend() {
        if (lineCount > maxLine) {
            startExtend();
        }
    }


    /**
     * 设置指示器的位置，左，中，下
     *
     * @param position
     * @see android.widget.RelativeLayout#CENTER_HORIZONTAL
     * @see android.widget.RelativeLayout#ALIGN_PARENT_LEFT
     * @see android.widget.RelativeLayout#ALIGN_PARENT_RIGHT
     */
    public void setIndicatorPosition(int position) {
        if (position == ALIGN_PARENT_LEFT) {
            LayoutParams layoutParams = (LayoutParams) indicatorLayout.getLayoutParams();
            layoutParams.removeRule(CENTER_HORIZONTAL);
            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
            layoutParams.addRule(position);
            layoutParams.leftMargin = (int) mLeftMargin;
            indicatorLayout.setLayoutParams(layoutParams);
        } else if (position == ALIGN_PARENT_RIGHT) {
            LayoutParams layoutParams = (LayoutParams) indicatorLayout.getLayoutParams();
            layoutParams.removeRule(CENTER_HORIZONTAL);
            layoutParams.removeRule(ALIGN_PARENT_LEFT);
            layoutParams.addRule(position);
            layoutParams.rightMargin = (int) mRightMargin;
            indicatorLayout.setLayoutParams(layoutParams);
        } else {
            LayoutParams layoutParams = (LayoutParams) indicatorLayout.getLayoutParams();
            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
            layoutParams.removeRule(ALIGN_PARENT_LEFT);
            layoutParams.addRule(CENTER_HORIZONTAL);
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
            indicatorLayout.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void fold() {
        if (lineCount > maxLine) {
            startFold();
        }
    }

    public void setListener(OnExtendListener listener) {
        mListener = listener;
    }

    public void setTextSize(int textSize) {
        mTextView.setTextSize(textSize);
    }

    public void setOpenStr(String openStr) {
        this.openStr = openStr;
    }

    public void setCloseStr(String closeStr) {
        this.closeStr = closeStr;
    }

    /**
     * 折叠情况下，最大行数
     *
     * @return
     */
    public int getMaxLine() {
        return maxLine;
    }

    /**
     * 设置最大行数
     *
     * @param maxLine
     */
    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }

    /**
     * TextView的边距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setTextPadding(int left, int top, int right, int bottom) {
        mTextView.setPadding(left, top, right, bottom);
    }

    /**
     * 通过该行数设置自定义的参数，例如TextSize,TextColor等,涉及到布局大小改变的，设置之后
     * 需要再调用{@link ExtendTextView#registerGlobalLayoutListener()}修正大小
     *
     * @return
     * @see ExtendTextView#setTextPadding(int, int, int, int)
     */
    public TextView getTextView() {
        return mTextView;
    }

    /**
     * 指示器开始伸展动画
     */
    private void extendIndicatorAnimation() {
        Animation animation = new RotateAnimation(0f, 180f, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(mAnimationDuration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                indicatorText.setText(openStr);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivIndicator.startAnimation(animation);
    }

    /**
     * 指示器开始折叠动画
     */
    private void foldIndicatorAnimator() {
        Animation animation = new RotateAnimation(180f, 0f, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(mAnimationDuration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                indicatorText.setText(closeStr);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivIndicator.startAnimation(animation);
    }

    /**
     * TextView折叠和伸展动画
     *
     * @param endValue
     * @param startValue
     */
    private void textAnimation(int startValue, int endValue, boolean isExtend) {
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mTextView.setHeight((int) (startValue + endValue * interpolatedTime));
            }

        };

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTextView.setText(mText);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isExtend) {
                    setFoldEllipsize();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation.setDuration(mAnimationDuration);
        mTextView.startAnimation(animation);
    }

    /**
     * 手动替换最后字符串
     */
    private void setFoldEllipsize() {
        if (maxLine < 3 || !isOpenEllipsize) {
            return;
        }
        int end = mTextView.getLayout().getLineEnd(maxLine - 1);
        StringBuilder sb = new StringBuilder(mText);
        if (mText.length()>end){
            sb.replace(end - 1, end, "...   ");
        }
        mTextView.setText(sb.toString());
    }

    /**
     * 是否收缩显示省略
     */
    private boolean isOpenEllipsize = true;

    public void setOpenEllipsize(boolean openEllipsize) {
        isOpenEllipsize = openEllipsize;
    }

    public long getAnimationDuration() {
        return mAnimationDuration;
    }

    public void setAnimationDuration(long mAnimationDuration) {
        this.mAnimationDuration = mAnimationDuration;
    }

    public interface OnExtendListener {
        void expand();

        void fold();
        void changeValue(int value);
    }


}
