package com.jmbon.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Drawable实现TextView末尾自定义样式文本，支持文末省略号
 */
public class TextDrawable extends Drawable {

    private final Paint paint;
    private final int baseline;

    private final String text;
    private final int textSize;
    private final int textColor;
    private final int backgroundColor;
    private final int padding;
    private final int radius;

    /**
     * @param text            文本
     * @param textSize        字体大小
     * @param textColor       字体颜色
     * @param backgroundColor 背景颜色
     * @param padding         左右padding
     * @param radius          背景圆角半径
     */
    public TextDrawable(String text, int textSize, int textColor, int backgroundColor, int padding, int radius) {
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.padding = padding;
        this.radius = radius;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float textWidth = paint.measureText(text);
        float textHeight = metrics.bottom - metrics.top;
        baseline = (int) (textHeight - metrics.descent);
        setBounds(new Rect(0, 0, (int) (textWidth + 2 * padding), (int) (textHeight)));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(new RectF(getBounds()), radius, radius, paint);
        paint.setColor(textColor);
        canvas.drawText(text, padding, baseline, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
