package com.jmbon.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;

public class TagSpanV2 extends ReplacementSpan {
    // span width
    private int mSize;
    // text and background wireframe color
    private final int mColor;
    // text color
    private final int mTextColor;
    // tag text size
    private final int mTextSizePx;
    // background radius
    private final int mRadiusPx;
    // background wireframe right margin
    private final int mRightMarginPx;
    // 绘制 方式
    private final Paint.Style drawableStyle;

    //padding
    private int topBottomPadding = 0;
    private int leftRightPadding = 0;

    private Context context;
    private int bitmapId = 0;
    private int bitmapHeightWidth = 0;
    private boolean isBold = false;

    private String startStr = "";
    private int startSize;

    public TagSpanV2(int color, int textColor, int textSizePx, int radiusPx, int rightMarginPx, Paint.Style drawableStyle) {
        mColor = color;
        mTextColor = textColor;
        mTextSizePx = textSizePx;
        mRadiusPx = radiusPx;
        mRightMarginPx = rightMarginPx;
        this.drawableStyle = drawableStyle;
    }

    public TagSpanV2(int color, int textColor, int textSizePx, int radiusPx, int rightMarginPx, Paint.Style drawableStyle, boolean isBold) {
        mColor = color;
        mTextColor = textColor;
        mTextSizePx = textSizePx;
        mRadiusPx = radiusPx;
        mRightMarginPx = rightMarginPx;
        this.drawableStyle = drawableStyle;
        this.isBold = isBold;
    }

    public TagSpanV2(int color, int textColor, int textSizePx, int radiusPx, int rightMarginPx, int topBottomPadding, int leftRightPadding, Paint.Style drawableStyle, boolean isBold) {
        mColor = color;
        mTextColor = textColor;
        mTextSizePx = textSizePx;
        mRadiusPx = radiusPx;
        mRightMarginPx = rightMarginPx;
        this.drawableStyle = drawableStyle;
        this.topBottomPadding = topBottomPadding;
        this.leftRightPadding = leftRightPadding;
        this.isBold = isBold;
    }

    public TagSpanV2(Context context, int color, int textColor, int textSizePx, int radiusPx, int rightMarginPx, int topBottomPadding, int leftRightPadding, int bitmapId, int bitmapHeightWidth, Paint.Style drawableStyle, boolean isBold) {
        this.context = context;
        mColor = color;
        mTextColor = textColor;
        mTextSizePx = textSizePx;
        mRadiusPx = radiusPx;
        mRightMarginPx = rightMarginPx;
        this.drawableStyle = drawableStyle;
        this.topBottomPadding = topBottomPadding;
        this.leftRightPadding = leftRightPadding;
        this.bitmapId = bitmapId;
        this.bitmapHeightWidth = bitmapHeightWidth;
        this.isBold = isBold;
    }

    public TagSpanV2(Context context, String startStr, int color, int textColor, int textSizePx, int radiusPx, int rightMarginPx, int topBottomPadding, int leftRightPadding, int bitmapId, int bitmapHeightWidth, Paint.Style drawableStyle, boolean isBold) {
        this.context = context;
        mColor = color;
        mTextColor = textColor;
        mTextSizePx = textSizePx;
        mRadiusPx = radiusPx;
        mRightMarginPx = rightMarginPx;
        this.drawableStyle = drawableStyle;
        this.topBottomPadding = topBottomPadding;
        this.leftRightPadding = leftRightPadding;
        this.bitmapId = bitmapId;
        this.bitmapHeightWidth = bitmapHeightWidth;
        this.startStr = startStr;
        this.isBold = isBold;

    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) paint.measureText(text, start, end) + mRightMarginPx;
        return mSize + bitmapHeightWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        drawTagRect(canvas, x, y, paint);
        drawTagText(canvas, text, start, end, x, y, paint, bottom+ top);

    }

    private void drawTagRect(Canvas canvas, float x, int y, Paint paint) {
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        final float strokeWidth = SizeUtils.dp2px(1f);
        paint.setStrokeWidth(strokeWidth);
//        final float strokeWidth = paint.getStrokeWidth();
        RectF oval;
        if (bitmapId == 0) {
            //无图tag
            oval = new RectF(x + strokeWidth, y + fontMetrics.ascent + 1f - topBottomPadding,
                    x + mSize + strokeWidth - mRightMarginPx + leftRightPadding * 2, y + fontMetrics.descent - 1.5f + topBottomPadding);
        } else {
            //有图TAG
            oval = new RectF(x + strokeWidth, y + fontMetrics.ascent + 1f - topBottomPadding,
                    x + mSize + strokeWidth - mRightMarginPx + leftRightPadding * 2 + bitmapHeightWidth, y + fontMetrics.descent - 1.5f + topBottomPadding);
        }
        paint.setStyle(drawableStyle);

        canvas.drawRoundRect(oval, mRadiusPx, mRadiusPx, paint);
    }

    private void drawTagText(Canvas canvas, CharSequence text, int start, int end, float x, int y, Paint paint, int height) {
       // Paint paint = new Paint();
        paint.setTextSize(mTextSizePx);
        paint.setColor(mTextColor);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        if (isBold) {
            //加粗
            paint.setFakeBoldText(true);
        }

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseY = (height >> 1) + (Math.abs(fontMetrics.top) + Math.abs(fontMetrics.bottom)) / 2 - fontMetrics.bottom;
        if (bitmapId != 0) {
            //边框1dp
            if (TextUtils.isEmpty(startStr)) {
                startSize = -SizeUtils.dp2px(1);
            } else {
                startSize = (int) x - SizeUtils.dp2px(1);
            }

            //有图绘制
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
            canvas.drawBitmap(bitmap, (bitmapHeightWidth >> 1) + startSize, ((height - bitmapHeightWidth) >> 1) + SizeUtils.dp2px(1f), new Paint());
            final int textCenterX = (mSize - mRightMarginPx) / 2 + leftRightPadding + bitmapHeightWidth + startSize;
//            (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom
            float textBaselineY = (y - fontMetrics.descent - fontMetrics.ascent) / 2 + fontMetrics.descent + topBottomPadding;

            final String tag = text.toString().substring(start, end);

            textBaselineY = (textBaselineY - (y - textBaselineY) / 2) + (SizeUtils.dp2px(1f) >> 1);

            canvas.drawText(tag, textCenterX + SizeUtils.dp2px(1f), baseY, paint);
        } else {
            //无图绘制
            final int textCenterX = (mSize - mRightMarginPx) / 2 + leftRightPadding;
            float textBaselineY = (y - fontMetrics.descent - fontMetrics.ascent) / 2 + fontMetrics.descent + topBottomPadding;

            final String tag = text.toString().substring(start, end);

            textBaselineY = (textBaselineY - (y - textBaselineY) / 2) + (SizeUtils.dp2px(1f) >> 1);

            canvas.drawText(tag, textCenterX + SizeUtils.dp2px(1f), baseY, paint);
        }
    }
}