package com.jmbon.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 为了解决 Spannable 与 android:ellipsize="end" 冲突的问题
 * 当android:maxLines="n" n>=2 时，如果包含 Spannable，行尾没有 "..."
 * 使用 android:singleLine="true" 时，无此问题
 * <p>
 * 方案缺陷：包含中文时，无法精确一行能展示的字数，所以...未必是在行尾，会偏前一点
 */
public class EllipsizeTextView extends AppCompatTextView {

    private static final String YX_THREE_DOTS = "...";
    private static final int YX_THREE_DOTS_LENGTH = YX_THREE_DOTS.length();

    private SpannableStringBuilder mSpannableStringBuilder;

    public EllipsizeTextView(Context context) {
        super(context);
        //initAutoSplitTextView();
    }

    public EllipsizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //initAutoSplitTextView();
    }

    public EllipsizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initAutoSplitTextView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        StaticLayout layout = null;
        Field field = null;
        try {
            Field staticField = DynamicLayout.class.getDeclaredField("sStaticLayout");
            staticField.setAccessible(true);
            layout = (StaticLayout) staticField.get(DynamicLayout.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (layout != null) {
            try {
                field = StaticLayout.class.getDeclaredField("mMaximumVisibleLineCount");
                field.setAccessible(true);
                field.setInt(layout, getMaxLines());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (layout != null && field != null) {
            try {
                field.setInt(layout, Integer.MAX_VALUE);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    private void initAutoSplitTextView() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                String txt = getText().toString();
                CharSequence charSequence = getText();

                TextPaint mPaint = getPaint();
                //文本自动换行
                String[] texts = autoSplit(txt, mPaint, getWidth() - 5);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                for (String text : texts) {
                    if (TextUtils.isEmpty(text)) {
                        continue;
                    }
                    spannableStringBuilder.append(text);
                    spannableStringBuilder.append("\n");
                }
                setText(spannableStringBuilder);
            }
        });
    }

//    protected void onDraw(Canvas canvas) {
//        TextPaint mPaint = getPaint();
//        Paint.FontMetrics fm = mPaint.getFontMetrics();
//
//        float baseline = fm.descent - fm.ascent;
//        float x = 0;
//        float y = baseline;  //由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。
//
//        String txt = getText().toString();
//        CharSequence charSequence = getText();
//
//        //文本自动换行
//        String[] texts = autoSplit(txt, mPaint, getWidth() - 5);
//
//        System.out.printf("line indexs: %s\n", Arrays.toString(texts));
//
//
//        if (charSequence instanceof Spanned) {
//            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//
//            for (String text : texts) {
//                if (TextUtils.isEmpty(text)) {
//                    continue;
//                }
//                spannableStringBuilder.append(text);
//                spannableStringBuilder.append("\n");
//            }
//            setText(spannableStringBuilder);
//
//        } else {
//            for (String text : texts) {
//                if (TextUtils.isEmpty(text)) {
//                    continue;
//                }
//                canvas.drawText(text, x, y, mPaint);  //坐标以控件左上角为原点
//                y += baseline + fm.leading; //添加字体行间距
//            }
//        }
//    }

    /**
     * 自动分割文本
     *
     * @param content 需要分割的文本
     * @param p       画笔，用来根据字体测量文本的宽度
     * @param width   最大的可显示像素（一般为控件的宽度）
     * @return 一个字符串数组，保存每行的文本
     */
    private String[] autoSplit(String content, Paint p, float width) {
        int length = content.length();
        float textWidth = p.measureText(content);
        if (textWidth <= width) {
            return new String[]{content};
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            if (p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }


//    @Override
//    protected void onDraw(Canvas canvas) {
//
//
//        final Layout layout = getLayout();
//
//        if (layout.getLineCount() >= getMaxLines()) {
//            CharSequence charSequence = getText();
//            int lastCharDown = layout.getLineVisibleEnd(getMaxLines() - 1);
//
//            if (lastCharDown >= YX_THREE_DOTS_LENGTH && charSequence.length() > lastCharDown) {
//                if (mSpannableStringBuilder == null) {
//                    mSpannableStringBuilder = new SpannableStringBuilder();
//                } else {
//                    mSpannableStringBuilder.clear();
//                }
//
//                // mSpannableStringBuilder.append(charSequence.subSequence(0,
//                //        lastCharDown - YX_THREE_DOTS_LENGTH)).append(YX_THREE_DOTS);
//                mSpannableStringBuilder.append(charSequence.subSequence(0, lastCharDown))
//                        .append(YX_THREE_DOTS);
//                setText(mSpannableStringBuilder);
//            }
//        }
//
//        super.onDraw(canvas);
//
//    }
}
