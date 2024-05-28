package com.jmbon.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;

/**
 * @author : leimg
 * time   : 2022/1/5
 * desc   :
 * version: 1.0
 */
public class ProgressIndicatorView extends View {

    private int noteNum = 2;//总计节点数
    private int finishNoteNum = 1;//完成的节点数

    private Paint finishNotePaint;
    private Paint finishInnerNotePaint;
    private Paint doingNotePaint;
    private Paint doingInnerNotePaint;
    private Paint finishLinePaint;
    private Paint doingLinePaint;
    private final float radius = SizeUtils.dp2px(7);
    private final float innerRadius = SizeUtils.dp2px(4);
    private final float lineHeight = SizeUtils.dp2px(2);
    int sizeWidth = 0;
    int sizeHeight = 0;

    public ProgressIndicatorView(Context context) {
        this(context, null);
    }

    public ProgressIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
    }

    private void initPaint() {
        finishNotePaint = new Paint();
        finishLinePaint = new Paint();
        doingNotePaint = new Paint();
        doingLinePaint = new Paint();
        finishInnerNotePaint = new Paint();
        doingInnerNotePaint = new Paint();
        finishNotePaint.setColor(getResources().getColor(R.color.color_cfeeef));
        finishLinePaint.setColor(getResources().getColor(R.color.color_currency));
        finishInnerNotePaint.setColor(getResources().getColor(R.color.color_currency));
        doingInnerNotePaint.setColor(getResources().getColor(R.color.picture_color_e5));


        doingNotePaint.setColor(getResources().getColor(R.color.colorF5F5F5));
        doingLinePaint.setColor(getResources().getColor(R.color.colorF5F5F5));
        finishNotePaint.setStyle(Paint.Style.FILL);
        finishLinePaint.setStyle(Paint.Style.FILL);
        doingNotePaint.setStyle(Paint.Style.FILL);
        doingLinePaint.setStyle(Paint.Style.FILL);
        finishInnerNotePaint.setStyle(Paint.Style.FILL);
        doingInnerNotePaint.setStyle(Paint.Style.FILL);

        doingLinePaint.setStrokeWidth(lineHeight);
        finishLinePaint.setStrokeWidth(lineHeight);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (sizeHeight > 0 && sizeWidth > 0) {
            float lineWidth = (sizeWidth - noteNum * radius * 2) / noteNum;
            for (int i = 0; i < noteNum; i++) {

                if (i < finishNoteNum) {
                    canvas.drawCircle(radius + i * (radius * 2 + lineWidth), sizeHeight / 2, radius, finishNotePaint);
                    canvas.drawCircle(radius + i * (radius * 2 + lineWidth), sizeHeight / 2, innerRadius, finishInnerNotePaint);
                } else {
                    canvas.drawCircle(radius + i * (radius * 2 + lineWidth), sizeHeight / 2, radius, doingNotePaint);
                    canvas.drawCircle(radius + i * (radius * 2 + lineWidth), sizeHeight / 2, innerRadius, doingInnerNotePaint);
                }


                //划线数量比note少1
                if (i < noteNum - 1) {
                    if (i < finishNoteNum - 1) {
                        canvas.drawLine(lineWidth * i + (i + 1) * (radius * 2), sizeHeight / 2,
                                lineWidth * i + (i + 1) * (radius * 2) + lineWidth, sizeHeight / 2, finishLinePaint);
                    } else {
                        canvas.drawLine(lineWidth * i + (i + 1) * (radius * 2), sizeHeight / 2,
                                lineWidth * i + (i + 1) * (radius * 2) + lineWidth, sizeHeight / 2, doingLinePaint);
                    }
                }

            }
        }
    }

    public void updateProgress(int finishNoteNum) {
        this.finishNoteNum = finishNoteNum;
        invalidate();
    }

    public void setTotalNoteNum(int noteNum) {
        this.noteNum = noteNum;
        invalidate();
    }

}
