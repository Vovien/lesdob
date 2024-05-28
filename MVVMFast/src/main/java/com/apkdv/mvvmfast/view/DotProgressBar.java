package com.apkdv.mvvmfast.view;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 布局加载状态
 */
public class DotProgressBar extends View {

    public static final int RIGHT_DIRECTION = 1;
    public static final int LEFT_DIRECTION = -1;

    /**
     * Dots amount
     */
    private int dotAmount;

    /**
     * Drawing tools
     */
    private Paint primaryPaint;
    private Paint startPaint;
    private Paint endPaint;

    /**
     * Animation tools
     */
    private long animationTime;
    private boolean isFirstLaunch = true;
    private ValueAnimator startValueAnimator;
    private ValueAnimator endValueAnimator;

    /**
     * Circle size
     */
    private float dotRadius;
    private float bounceDotRadius;
    /**
     * Circle coordinates
     */
    private float xCoordinate;
    private int dotPosition;

    /**
     * Colors
     */
    private int startColor;
    private int endColor;

    /**
     * This value detect direction of circle animation direction
     * {@link DotProgressBar#RIGHT_DIRECTION} and {@link DotProgressBar#LEFT_DIRECTION}
     */
    private int animationDirection;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeAttributes(attrs);
        init();
    }

    public DotProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(attrs);
        init();
    }

    public DotProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttributes(attrs);
        init();
    }

    public DotProgressBar(Context context) {
        super(context);
        initializeAttributes(null);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

        if (getMeasuredHeight() > getMeasuredWidth()) {
            dotRadius = getMeasuredWidth() / dotAmount / 4;
        } else {
            dotRadius = getMeasuredHeight() >> 2;
        }

        bounceDotRadius = dotRadius + (dotRadius / 3);
        float circlesWidth = (dotAmount * (dotRadius * 2)) + dotRadius * (dotAmount - 1);
        xCoordinate = (getMeasuredWidth() - circlesWidth) / 2 + dotRadius;
    }

    private void initializeAttributes(AttributeSet attrs) {
        setDotAmount(3);
        setAnimationTime(getResources().getInteger(android.R.integer.config_shortAnimTime));
        setStartColor(Color.parseColor("#325384FF"));
        setEndColor(Color.parseColor("#5384FF"));
        setAnimationDirection(RIGHT_DIRECTION);
    }

    private void init() {
        primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        primaryPaint.setColor(startColor);
        primaryPaint.setStrokeJoin(Paint.Join.ROUND);
        primaryPaint.setStrokeCap(Paint.Cap.ROUND);
        primaryPaint.setStrokeWidth(20);

        startPaint = new Paint(primaryPaint);
        endPaint = new Paint(primaryPaint);

        startValueAnimator = ValueAnimator.ofInt(startColor, endColor);
        startValueAnimator.setDuration(animationTime);
        startValueAnimator.setEvaluator(new ArgbEvaluator());
        startValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startPaint.setColor(((Integer) animation.getAnimatedValue()));
            }
        });

        endValueAnimator = ValueAnimator.ofInt(endColor, startColor);
        endValueAnimator.setDuration(animationTime);
        endValueAnimator.setEvaluator(new ArgbEvaluator());
        endValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                endPaint.setColor(((Integer) animation.getAnimatedValue()));
            }
        });
    }

    /**
     * setters
     */
    void setDotAmount(int amount) {
        this.dotAmount = amount;
    }

    void setStartColor(@ColorInt int color) {
        this.startColor = color;
    }

    void setEndColor(@ColorInt int color) {
        this.endColor = color;
    }

    void setAnimationTime(long animationTime) {
        this.animationTime = animationTime;
    }

    private void setDotPosition(int dotPosition) {
        this.dotPosition = dotPosition;
    }

    /**
     * Set amount of dots, it will be restarted your view
     *
     * @param amount number of dots, dot size automatically adjust
     */
    public void changeDotAmount(int amount) {
        stopAnimation();
        setDotAmount(amount);
        setDotPosition(0);
        reinitialize();
    }

    /**
     * It will be restarted your view
     */
    public void changeStartColor(@ColorInt int color) {
        stopAnimation();
        setStartColor(color);
        reinitialize();
    }

    /**
     * It will be restarted your view
     */
    public void changeEndColor(@ColorInt int color) {
        stopAnimation();
        setEndColor(color);
        reinitialize();
    }

    /**
     * It will be restarted your view
     */
    public void changeAnimationTime(long animationTime) {
        stopAnimation();
        setAnimationTime(animationTime);
        reinitialize();
    }

    /**
     * Change animation direction, doesn't restart view.
     *
     * @param animationDirection left or right animation direction
     */
    public void changeAnimationDirection(@AnimationDirection int animationDirection) {
        setAnimationDirection(animationDirection);
    }

    public int getAnimationDirection() {
        return animationDirection;
    }

    void setAnimationDirection(int direction) {
        this.animationDirection = direction;
    }

    /**
     * Reinitialize animators instances
     */
    void reinitialize() {
        init();
        requestLayout();
        startAnimation();
    }

    private void drawCirclesLeftToRight(Canvas canvas) {
        float step = 0;
        for (int i = 0; i < dotAmount; i++) {
            drawCircles(canvas, i, step);
            step += dotRadius * 3;
        }
    }

    private void drawCirclesRightToLeft(Canvas canvas) {
        float step = 0;
        for (int i = dotAmount - 1; i >= 0; i--) {
            drawCircles(canvas, i, step);
            step += dotRadius * 3;
        }
    }

    private void drawCircles(@NonNull Canvas canvas, int i, float step) {
        if (dotPosition == i) {
            drawCircleUp(canvas, step);
        } else {
            if ((i == (dotAmount - 1) && dotPosition == 0 && !isFirstLaunch) || ((dotPosition - 1) == i)) {
                drawCircleDown(canvas, step);
            } else {
                drawCircle(canvas, step);
            }
        }
    }

    /**
     * Circle radius is grow
     */
    private void drawCircleUp(@NonNull Canvas canvas, float step) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() >> 1,
                dotRadius,
                startPaint
        );
    }

    private void drawCircle(@NonNull Canvas canvas, float step) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() >> 1,
                dotRadius,
                primaryPaint
        );
    }

    /**
     * Circle radius is decrease
     */
    private void drawCircleDown(@NonNull Canvas canvas, float step) {
        canvas.drawCircle(
                xCoordinate + step,
                getMeasuredHeight() >> 1,
                dotRadius,
                endPaint
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animationDirection < 0) {
            drawCirclesRightToLeft(canvas);
        } else {
            drawCirclesLeftToRight(canvas);
        }
    }

    public void stopAnimation() {
        int endColor = Color.parseColor("#320EA9B0");
        setEndColor(endColor);
        startValueAnimator.cancel();
        endValueAnimator.cancel();
        startPaint.setColor(endColor);
        endPaint.setColor(endColor);
        this.clearAnimation();
        postInvalidate();
    }

    public void startAnimation() {
        setEndColor(Color.parseColor("#0EA9B0"));
        final BounceAnimation bounceAnimation = new BounceAnimation();
        bounceAnimation.setDuration(animationTime);
        bounceAnimation.setRepeatCount(Animation.INFINITE);
        bounceAnimation.setInterpolator(new LinearInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                dotPosition++;
                if (dotPosition == dotAmount) {
                    dotPosition = 0;
                }

                startValueAnimator.start();

                if (!isFirstLaunch) {
                    endValueAnimator.start();
                }

                isFirstLaunch = false;
            }
        });
        startAnimation(bounceAnimation);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RIGHT_DIRECTION, LEFT_DIRECTION})
    public @interface AnimationDirection {
    }

    private class BounceAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            invalidate();
        }
    }
}
