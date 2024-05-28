package com.jmbon.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 自定义数字键盘
 */
public class CustomNumKeyView extends View {
    /**
     * 列
     */
    private static final int TOTAL_COL = 3;
    /**
     * 行
     */
    private static final int TOTAL_ROW = 4;
    private static final String TAG = "CustomNumKeyView";
    ObjectAnimator objectAnimatorHide, objectAnimatorShow;
    private Paint linePaint, itemBgPaint, itemClickBgPaint;
    private Paint mTextPaint;
    private int mViewWidth; // 键盘宽度
    private int mViewHight; // 键盘高度
    private int mViewHight2; // 键盘高度
    private float mCellWidth, mCellHight; // 单元格宽度、高度
    private final Row[] rows = new Row[TOTAL_ROW];
    private Bitmap bitmap; // 删除按钮图片
    private Paint mCutTextPaint;
    private final int raduisPx = SizeUtils.dp2px(5);
    private final int space3 = SizeUtils.dp2px(4);
    private final int space6 = SizeUtils.dp2px(8);
    private CallBack mCallBack;// 回调
    private Cell mClickCell = null;
    private float mDownX;
    private float mDownY;
    private final List<String> numKeys = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    private int backColor = 0xEBCDD0D4;
    private int textColor = 0xFF262626;
    private int textSize = SizeUtils.dp2px(22);

    public CustomNumKeyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);

        init(context);

    }


    public CustomNumKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);

    }

    public CustomNumKeyView(Context context) {
        super(context);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomNumKeyView);
        textColor = typedArray.getColor(R.styleable.CustomNumKeyView_numKeyViewTextColor, 0xFF000000);
        backColor = typedArray.getColor(R.styleable.CustomNumKeyView_numKeyViewBackground, 0xEBCDD0D4);
        textSize = (int) typedArray.getDimension(R.styleable.CustomNumKeyView_numKeyViewTextSize, SizeUtils.dp2px(22));
        typedArray.recycle();
    }


    public void setOnCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public void hideAnimal() {

        if (objectAnimatorHide == null) {
            //      创建属性动画对象，并设置移动的方向和偏移量
            objectAnimatorHide = ObjectAnimator.ofFloat(this, "translationY", mViewHight2);
            //      设置移动时间
            objectAnimatorHide.setDuration(300);
        }
        //      开始动画
        objectAnimatorHide.start();
    }

    public void showAnimal() {

        if (objectAnimatorShow == null) {
            //      创建属性动画对象，并设置移动的方向和偏移量
            objectAnimatorShow = ObjectAnimator.ofFloat(this, "translationY", mViewHight2, 0);
            //      设置移动时间
            objectAnimatorShow.setDuration(300);
        }
        //      开始动画
        objectAnimatorShow.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先绘制一个白色，否则透传后面的颜色
        canvas.drawColor(0xffffffff);
        //绘制背景色
        canvas.drawColor(backColor);
        //drawLine(canvas);
        for (int i = 0; i < TOTAL_ROW; i++) {
            if (rows[i] != null)
                rows[i].drawCells(canvas);
        }
    }

    /**
     * 画6条直线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        canvas.drawLine(0, 0, mViewWidth, 0, linePaint);
        canvas.drawLine(0, mCellHight, mViewWidth, mCellHight, linePaint);
        canvas.drawLine(0, mCellHight * 2, mViewWidth, mCellHight * 2, linePaint);
        canvas.drawLine(0, mCellHight * 3, mViewWidth, mCellHight * 3, linePaint);
        canvas.drawLine(mCellWidth, 0, mCellWidth, mViewHight, linePaint);
        canvas.drawLine(mCellWidth * 2, 0, mCellWidth * 2, mViewHight, linePaint);
    }

    /**
     * 初始化画笔
     *
     * @param
     */
    private void init(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        //mTextPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/din_alternate_bold.ttf"));


        //绘制数字矩形
        itemBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemBgPaint.setColor(0xffffffff);
        //绘制背景阴影
        itemBgPaint.setShadowLayer(5, 0, 2, 0x05000000);

        //绘制点击交互
        itemClickBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemClickBgPaint.setColor(0xfffcfcfc);
        itemClickBgPaint.setShadowLayer(5, 0, 5, 0x05000000);

        mCutTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setTextSize(1.0f);
        linePaint.setColor(0x90000000);


        initDate();
    }

    private void initDate() {
        fillDate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHight = h - SizeUtils.dp2px(34);//减去底部padding
        mViewHight2 = h;
        mCellWidth = mViewWidth / TOTAL_COL;
        mCellHight = mViewHight / TOTAL_ROW;
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextSize(SizeUtils.dp2px(22));

    }

    /*
     *
     * 触摸事件为了确定点击位置的数字
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                int col = (int) (mDownX / mCellWidth);
                int row = (int) (mDownY / mCellHight);
                measureClickCell(col, row);
                break;
            case MotionEvent.ACTION_UP:
                if (mClickCell != null) {
                    // 在抬起后把状态置为默认
                    rows[mClickCell.i].cells[mClickCell.j].state = State.DEFAULT_NUM;
                    mClickCell = null;
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 测量点击单元格
     *
     * @param col 列
     * @param row 行
     */
    private void measureClickCell(int col, int row) {
        if (col >= TOTAL_COL || row >= TOTAL_ROW) {
            mCallBack.clickEmptyRect();
            return;
        }
        if (rows[row] != null) {
            mClickCell = new Cell(rows[row].cells[col].num, rows[row].cells[col].state, rows[row].cells[col].i,
                    rows[row].cells[col].j);
            rows[row].cells[col].state = State.CLICK_NUM;
            if ("-5".equals(rows[row].cells[col].num)) {
                mCallBack.deleteNum();
            } else if (".".equals(rows[row].cells[col].num)) {
                //忽略逗号
                mCallBack.clickEmptyRect();
            } else {
                mCallBack.clickNum(rows[row].cells[col].num);
            }
            invalidate();
        }
    }

    /**
     * 填充数字
     */
    private void fillDate() {
        int postion = 0;
        for (int i = 0; i < TOTAL_ROW; i++) {
            rows[i] = new Row(i);
            for (int j = 0; j < TOTAL_COL; j++) {
                if (i == 3 && j == 0) {
                    rows[i].cells[j] = new Cell(".", State.DEFAULT_NUM, i, j);
                    continue;
                } else if (i == 3 && j == 2) {
                    rows[i].cells[j] = new Cell("-5", State.DEFAULT_NUM, i, j);
                    continue;
                } else {
                    rows[i].cells[j] = new Cell(numKeys.get(postion), State.DEFAULT_NUM, i, j);
                    postion++;
                }
            }
        }
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_soft_key_delete);
        //  bitmapBg = ((BitmapDrawable) getResources().getDrawable(R.drawable.custom_num_key_bg)).getBitmap();
    }

    /**
     * 随机键盘
     *
     * @param isRandom
     */
    public void setRandomKeyBoard(boolean isRandom) {
        if (isRandom) {
            Collections.shuffle(numKeys);
            initDate();
            invalidate();
        }
    }

    /**
     * cell的state
     */
    private enum State {
        DEFAULT_NUM, CLICK_NUM
    }

    public interface CallBack {
        void clickNum(String num);// 回调点击的数字

        void deleteNum();// 回调删除

        void clickEmptyRect();// 回调点击无效区域
    }

    /**
     * 组 以一行为一组
     */
    private class Row {
        public int j;
        // 一行3个单元格
        public Cell[] cells = new Cell[TOTAL_COL];

        Row(int j) {
            this.j = j;
        }

        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null)
                    cells[i].drawSelf(canvas);
            }

        }
    }

    // 单元格
    private class Cell {
        public String num;
        public State state;
        /**
         * i = 行 j = 列
         */
        public int i;
        public int j;

        public Cell(String num, State state, int i, int j) {
            super();
            this.num = num;
            this.state = state;
            this.i = i;
            this.j = j;
        }

        // 绘制一个单元格 如果颜色需要自定义可以修改
        public void drawSelf(Canvas canvas) {
            //不绘制小数点
            if (".".equals(num)) {
                return;
            }

//            switch (state) {
//                case CLICK_NUM:
//                    // 绘制点击效果灰色背景
////                    canvas.drawRect((float) (mCellWidth * j), (float) (mCellHight * i),
////                            (float) (mCellWidth * (j + 1)), (float) (mCellHight * (i + 1)), itemClickBgPaint);
//                    if (j == 2) {
//                        //靠右边
//                        canvas.drawRoundRect((float) (mCellWidth * j) + space3, (float) (mCellHight * i) + space6,
//                                (float) (mCellWidth * (j + 1)) - space6, (float) (mCellHight * (i + 1)), raduisPx, raduisPx, itemClickBgPaint);
//                    } else if (j == 0) {
//                        //靠左边
//                        canvas.drawRoundRect((float) (mCellWidth * j) + space6, (float) (mCellHight * i) + space6,
//                                (float) (mCellWidth * (j + 1)) - space3, (float) (mCellHight * (i + 1)), raduisPx, raduisPx, itemClickBgPaint);
//
//
//                    } else {
//                        canvas.drawRoundRect((float) (mCellWidth * j) + space3, (float) (mCellHight * i) + space6,
//                                (float) (mCellWidth * (j + 1)) - space3, (float) (mCellHight * (i + 1)), raduisPx, raduisPx, itemClickBgPaint);
//
//                    }
//
//                    return;
//            }

            if (state == State.CLICK_NUM) {
                mCutTextPaint = itemClickBgPaint;
            } else {
                mCutTextPaint = itemBgPaint;
            }

            if ("-5".equals(num)) {
                // 绘制删除图片
                canvas.drawBitmap(bitmap, (float) (mCellWidth * 2.5 - bitmap.getWidth() / 2), (float) (mCellHight * 3.5 - bitmap.getHeight() / 2 + space6 / 2), mCutTextPaint);
            } else {

                //绘制背景,控制间距
                if (j == 2) {
                    //靠右边
                    canvas.drawRoundRect((mCellWidth * j) + space3, (mCellHight * i) + space6,
                            (mCellWidth * (j + 1)) - space6, mCellHight * (i + 1), raduisPx, raduisPx, mCutTextPaint);
                } else if (j == 0) {
                    //靠左边
                    canvas.drawRoundRect((mCellWidth * j) + space6, (mCellHight * i) + space6,
                            (mCellWidth * (j + 1)) - space3, mCellHight * (i + 1), raduisPx, raduisPx, mCutTextPaint);


                } else {
                    //中间数字
                    canvas.drawRoundRect((mCellWidth * j) + space3, (mCellHight * i) + space6,
                            (mCellWidth * (j + 1)) - space3, mCellHight * (i + 1), raduisPx, raduisPx, mCutTextPaint);

                }


                // 绘制数字
                canvas.drawText(num, (float) ((j + 0.5) * mCellWidth - mTextPaint.measureText(num) / 2),
                        (float) ((i + 0.5) * mCellHight + (mTextPaint.measureText(num, 0, 1) + space6) / 2),
                        mTextPaint);
            }


        }
    }

}
