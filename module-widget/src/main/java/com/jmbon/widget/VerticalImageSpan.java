package com.jmbon.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   :
 * version: 1.0
 */
public class VerticalImageSpan extends ImageSpan {

    private boolean limitImageSize = false;

    public VerticalImageSpan(Drawable drawable) {
        super(drawable);
    }

    public VerticalImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Context context, Bitmap b) {
        super(context, b);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Context context, int resourceId, int verticalAlignment, boolean limitImageSize) {
        super(context, resourceId, verticalAlignment);
        this.limitImageSize = limitImageSize;
    }

    public VerticalImageSpan(Context context, int resourceId) {
        super(context, resourceId);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Context context, Uri uri) {
        super(context, uri);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
        // TODO Auto-generated constructor stub
    }

    public VerticalImageSpan(Drawable d, String source) {
        super(d, source);
        // TODO Auto-generated constructor stub
    }

    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (limitImageSize && paint != null) {
                drawable.setBounds(0, 0, (int) paint.getTextSize(), (int) paint.getTextSize());
            }
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fontMetricsInt.ascent = -bottom;
                fontMetricsInt.top = -bottom;
                fontMetricsInt.bottom = top;
                fontMetricsInt.descent = top;
            }
            return rect.right;
        }
        return 0;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        if (drawable != null) {// drawable==null?
            if (limitImageSize && paint != null) {
                drawable.setBounds(0, 0, (int) paint.getTextSize(), (int) paint.getTextSize());
            }
            canvas.save();
            int transY = 0;
            transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public Drawable getDrawable() {
        return super.getDrawable();
    }
}
