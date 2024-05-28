package com.jmbon.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author : leimg
 * time   : 2021/10/28
 * desc   :
 * version: 1.0
 */
public class AdapterImageView extends AppCompatImageView {
    public AdapterImageView(Context context) {
        super(context);
    }

    public AdapterImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        int maxWidth = getMaxWidth();
        int minWidth = getMinimumWidth();
        int maxHeight = getMaxHeight();
        int minHeight = getMinimumHeight();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);


        float drawHeight = drawable.getIntrinsicHeight();
        float drawWidth = drawable.getIntrinsicWidth();

        if (height > width) {
            //以高为准
            if (height > maxHeight) {
                height = maxHeight;
            }
            if (height < minHeight) {
                height = minHeight;
            }

            // 控件的宽度width = 屏幕的宽度 MeasureSpec.getSize(widthMeasureSpec);
            // 控件的高度 = 控件的宽度width*图片的宽高比 drawHeight / drawWidth；
            //以宽度为标准
            int imageWidth = (int) Math.ceil(height * (drawWidth / drawHeight));
            setMeasuredDimension(imageWidth, height);

        } else {
            if (width > maxWidth) {
                width = maxWidth;
            }
            if (width < minWidth) {
                width = minWidth;
            }

            // 控件的宽度width = 屏幕的宽度 MeasureSpec.getSize(widthMeasureSpec);
            // 控件的高度 = 控件的宽度width*图片的宽高比 drawHeight / drawWidth；
            //以宽度为标准
            int imageHeight = (int) Math.ceil(width * (drawHeight / drawWidth));
            setMeasuredDimension(width, imageHeight);
        }
    }

}
