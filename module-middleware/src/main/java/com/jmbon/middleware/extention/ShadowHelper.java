package com.jmbon.middleware.extention;

import static android.graphics.drawable.GradientDrawable.RECTANGLE;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/******************************************************************************
 * Description: 阴影实现类
 *
 * Author: jhg
 *
 * Date: 2023/6/29
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
public class ShadowHelper extends Drawable {

    /**
     * 背景的形状, 支持圆角矩形和圆形
     */
    private int mShape;
    /**
     * 背景圆角
     */
    private int mShapeRadius;
    /**
     * 背景圆角, 可单独设置部分圆角
     */
    private float[] mShapeRadii;
    /**
     * 背景颜色, 可设置纯色, 也可设置渐变色
     */
    private int mBgColor[];
    /**
     * 渐变方向, 默认从左到右
     */
    private GradientDrawable.Orientation mGradientAngle;
    /**
     * 边框颜色
     */
    private int mBorderColor;
    /**
     * 边框宽度
     */
    private int mBorderWidth;
    /**
     * View的Padding, 控制阴影的显示范围
     */
    private int[] mShadowSpacing;

    /**
     * 背景&阴影区域
     */
    private RectF  mRect;
    /**
     * 阴影画笔
     */
    private Paint mShadowPaint;
    /**
     * 背景画笔
     */
    private Paint mBgPaint;
    /**
     * 边框画笔
     */
    private Paint mBorderPaint;

    private ShadowHelper(int shape, int[] bgColor, GradientDrawable.Orientation gradientAngle,
                         int borderColor, int borderWidth, int shapeRadius, float[] shapeRadii,
                         int shadowColor, int shadowRadius, int[] viewPadding) {
        this.mShape = shape;
        this.mBgColor = bgColor;
        this.mGradientAngle = gradientAngle;
        this.mBorderColor = borderColor;
        this.mBorderWidth = borderWidth;
        this.mShapeRadius = shapeRadius;
        this.mShapeRadii = shapeRadii;
        this.mShadowSpacing = viewPadding;

        int offsetX = 0;
        if (viewPadding[0] != viewPadding[2]) {
            offsetX = viewPadding[2] - (viewPadding[0] + viewPadding[2]) / 2;
        }
        int offsetY = 0;
        if (viewPadding[1] != viewPadding[3]) {
            offsetY = viewPadding[3] - (viewPadding[1] + viewPadding[3]) / 2;
        }
        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.TRANSPARENT);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
        mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        if (mBgColor != null) {
            if (mBgColor.length == 1) {
                mBgPaint.setColor(mBgColor[0]);
            } else {
                if (mGradientAngle == GradientDrawable.Orientation.LEFT_RIGHT) {
                    mBgPaint.setShader(new LinearGradient(mRect.left, mRect.height() / 2, mRect.right,
                            mRect.height() / 2, mBgColor, null, Shader.TileMode.CLAMP));
                } else if (mGradientAngle == GradientDrawable.Orientation.TOP_BOTTOM) {
                    mBgPaint.setShader(new LinearGradient(mRect.width() / 2, mRect.top, mRect.width() / 2,
                            mRect.bottom, mBgColor, null, Shader.TileMode.CLAMP));
                } else if (mGradientAngle == GradientDrawable.Orientation.RIGHT_LEFT) {
                    mBgPaint.setShader(new LinearGradient(mRect.right, mRect.height() / 2, mRect.left,
                            mRect.height() / 2, mBgColor, null, Shader.TileMode.CLAMP));
                } else if (mGradientAngle == GradientDrawable.Orientation.BOTTOM_TOP) {
                    mBgPaint.setShader(new LinearGradient(mRect.width() / 2, mRect.bottom, mRect.width() / 2,
                            mRect.top, mBgColor, null, Shader.TileMode.CLAMP));
                } else if (mGradientAngle == GradientDrawable.Orientation.TL_BR) {
                    mBgPaint.setShader(new LinearGradient(mRect.left, mRect.top, mRect.right,
                            mRect.bottom, mBgColor, null, Shader.TileMode.CLAMP));
                } else if (mGradientAngle == GradientDrawable.Orientation.TR_BL) {
                    mBgPaint.setShader(new LinearGradient(mRect.right, mRect.top, mRect.left,
                            mRect.bottom, mBgColor, null, Shader.TileMode.CLAMP));
                } else if (mGradientAngle == GradientDrawable.Orientation.BL_TR) {
                    mBgPaint.setShader(new LinearGradient(mRect.left, mRect.bottom, mRect.right,
                            mRect.top, mBgColor, null, Shader.TileMode.CLAMP));
                } else if (mGradientAngle == GradientDrawable.Orientation.BR_TL) {
                    mBgPaint.setShader(new LinearGradient(mRect.right, mRect.bottom, mRect.left,
                            mRect.top, mBgColor, null, Shader.TileMode.CLAMP));
                }
            }
        }
        if (mBorderColor != Color.TRANSPARENT && mBorderWidth > 0) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRect = new RectF(left + mShadowSpacing[0], top + mShadowSpacing[1], right - mShadowSpacing[2],
                bottom - mShadowSpacing[3]);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mShape == RECTANGLE) {
            if (mShapeRadii != null && mShapeRadii.length == 4) {
                float[] realShapeRadii = new float[] {mShapeRadii[0], mShapeRadii[0], mShapeRadii[1], mShapeRadii[1], mShapeRadii[2], mShapeRadii[2], mShapeRadii[3], mShapeRadii[3]};
                Path path = new Path();
                path.addRoundRect(mRect, realShapeRadii, Path.Direction.CW);
                canvas.drawPath(path, mShadowPaint);
                canvas.drawPath(path, mBgPaint);
                if (mBorderPaint != null) {
                    canvas.drawPath(path, mBorderPaint);
                }
            } else {
                canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mShadowPaint);
                canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mBgPaint);
                if (mBorderPaint != null) {
                    canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mBorderPaint);
                }
            }
        } else {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mShadowPaint);
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mBgPaint);
            if (mBorderPaint != null) {
                canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mBorderPaint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static class Builder {
        private int mShape;
        private int mShapeRadius;
        private float[] mShapeRadii;
        private int mShadowColor;
        private int mShadowRadius;
        private int[] mBgColor;
        private GradientDrawable.Orientation mGradientAngle;
        private int mBorderColor;
        private int mBorderWidth;
        private int[] mShadowSpacing = null;

        public Builder() {
            mShape = RECTANGLE;
            mShapeRadius = 12;
            mShadowColor = Color.parseColor("#4d000000");
            mShadowRadius = 18;
            mBgColor = new int[1];
            mBgColor[0] = Color.TRANSPARENT;
            mGradientAngle = GradientDrawable.Orientation.LEFT_RIGHT;
            mBorderColor = Color.TRANSPARENT;
            mBorderWidth = 0;
        }

        public Builder setShape(int shape) {
            this.mShape = shape;
            return this;
        }

        public Builder setShapeRadius(int shapeRadius) {
            this.mShapeRadius = shapeRadius;
            return this;
        }

        public Builder setShapeRadii(float[] shapeRadii) {
            this.mShapeRadii = shapeRadii;
            return this;
        }

        public Builder setShadowColor(int shadowColor) {
            this.mShadowColor = shadowColor;
            return this;
        }

        public Builder setShadowRadius(int shadowRadius) {
            this.mShadowRadius = shadowRadius;
            return this;
        }

        public Builder setBgColor(int bgColor) {
            this.mBgColor[0] = bgColor;
            return this;
        }

        public Builder setBgColor(int[] bgColor) {
            this.mBgColor = bgColor;
            return this;
        }

        public Builder setGradientAngle(GradientDrawable.Orientation gradientAngle) {
            this.mGradientAngle = gradientAngle;
            return this;
        }

        public Builder setBorderColor(int borderColor) {
            this.mBorderColor = borderColor;
            return this;
        }

        public Builder setBorderWidth(int borderWidth) {
            this.mBorderWidth = borderWidth;
            return this;
        }

        public Builder setShadowSpacing(int leftSpacing, int topSpacing, int rightSpacing, int bottomSpacing) {
            this.mShadowSpacing = new int[] {leftSpacing, topSpacing, rightSpacing, bottomSpacing};
            return this;
        }

        public ShadowHelper builder() {
            if (mShadowSpacing == null) {
                mShadowSpacing = new int[] {mShadowRadius, mShadowRadius, mShadowRadius, mShadowRadius};
            }
            return new ShadowHelper(mShape, mBgColor, mGradientAngle, mBorderColor, mBorderWidth, mShapeRadius, mShapeRadii, mShadowColor, mShadowRadius, mShadowSpacing);
        }

        public void applyTo(View targetView) {
            targetView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            if (mShadowSpacing == null) {
                mShadowSpacing = new int[] {mShadowRadius, mShadowRadius, mShadowRadius, mShadowRadius};
            }
            targetView.setBackground(new ShadowHelper(mShape, mBgColor, mGradientAngle, mBorderColor, mBorderWidth, mShapeRadius, mShapeRadii, mShadowColor, mShadowRadius, mShadowSpacing));
        }
    }
}