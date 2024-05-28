package com.jmbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.allen.library.helper.ShapeBuilder;
import com.allen.library.helper.ShapeGradientAngle;
import com.allen.library.helper.ShapeGradientType;
import com.allen.library.helper.ShapeType;

/**
 * Created by Allen on 2017/7/6.
 * <p>
 * 超级按钮  实现shape所有的属性
 */

public class SuperView extends View {

    private final Context mContext;

    private final int defaultColor = 0x20000000;
    private final int defaultSelectorColor = 0x20000000;

    private int solidColor;
    private int selectorPressedColor;
    private int selectorDisableColor;
    private int selectorNormalColor;

    private float cornersRadius;
    private float cornersTopLeftRadius;
    private float cornersTopRightRadius;
    private float cornersBottomLeftRadius;
    private float cornersBottomRightRadius;

    private int strokeWidth;
    private int strokeColor;

    private float strokeDashWidth;
    private float strokeDashGap;

    private int sizeWidth;
    private int sizeHeight;

    private int gradientAngle;
    private int gradientCenterX;
    private int gradientCenterY;
    private int gradientGradientRadius;

    private int gradientStartColor;
    private int gradientCenterColor;
    private int gradientEndColor;

    private int gradientType;

    //"linear"	线形渐变。这也是默认的模式
    private static final int linear = 0;
    //"radial"	辐射渐变。startColor即辐射中心的颜色
    private static final int radial = 1;
    //"sweep"	扫描线渐变。
    private static final int sweep = 2;

    private boolean gradientUseLevel;

    private boolean useSelector;


    //shape的样式
    public static final int RECTANGLE = 0;
    public static final int OVAL = 1;
    public static final int LINE = 2;
    public static final int RING = 3;


    //渐变色的显示方式
    public static final int TOP_BOTTOM = 0;
    public static final int TR_BL = 1;
    public static final int RIGHT_LEFT = 2;
    public static final int BR_TL = 3;
    public static final int BOTTOM_TOP = 4;
    public static final int BL_TR = 5;
    public static final int LEFT_RIGHT = 6;
    public static final int TL_BR = 7;

    //文字显示的位置方式
    public static final int TEXT_GRAVITY_CENTER = 0;
    public static final int TEXT_GRAVITY_LEFT = 1;
    public static final int TEXT_GRAVITY_RIGHT = 2;
    public static final int TEXT_GRAVITY_TOP = 3;
    public static final int TEXT_GRAVITY_BOTTOM = 4;


    private int shapeType;

    private int gravity;

    private GradientDrawable gradientDrawable;

    private ShapeBuilder shapeBuilder;

    public SuperView(Context context) {
        this(context, null);
    }

    public SuperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getAttr(attrs);
        init();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SuperView);
        gravity = typedArray.getInt(R.styleable.SuperView_vGravity, 0);

        shapeType = typedArray.getInt(R.styleable.SuperView_vShapeType, GradientDrawable.RECTANGLE);

        solidColor = typedArray.getColor(R.styleable.SuperView_vSolidColor, defaultColor);

        selectorPressedColor = typedArray.getColor(R.styleable.SuperView_vSelectorPressedColor, defaultSelectorColor);
        selectorDisableColor = typedArray.getColor(R.styleable.SuperView_vSelectorDisableColor, defaultSelectorColor);
        selectorNormalColor = typedArray.getColor(R.styleable.SuperView_vSelectorNormalColor, defaultSelectorColor);

        cornersRadius = typedArray.getDimensionPixelSize(R.styleable.SuperView_vCornersRadius, 0);


        cornersTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.SuperView_vCornersTopLeftRadius, 0);
        cornersTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.SuperView_vCornersTopRightRadius, 0);
        cornersBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.SuperView_vCornersBottomLeftRadius, 0);
        cornersBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.SuperView_vCornersBottomRightRadius, 0);

        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.SuperView_vStrokeWidth, 0);
        strokeDashWidth = typedArray.getDimensionPixelSize(R.styleable.SuperView_vStrokeDashWidth, 0);
        strokeDashGap = typedArray.getDimensionPixelSize(R.styleable.SuperView_vStrokeDashGap, 0);

        strokeColor = typedArray.getColor(R.styleable.SuperView_vStrokeColor, defaultColor);

        sizeWidth = typedArray.getDimensionPixelSize(R.styleable.SuperView_vSizeWidth, 0);
        sizeHeight = typedArray.getDimensionPixelSize(R.styleable.SuperView_vSizeHeight, dip2px(mContext, 48));

        gradientAngle = (int) typedArray.getFloat(R.styleable.SuperView_vGradientAngle, -1);
        gradientCenterX = typedArray.getDimensionPixelSize(R.styleable.SuperView_vGradientCenterX, 0);
        gradientCenterY = typedArray.getDimensionPixelSize(R.styleable.SuperView_vGradientCenterY, 0);
        gradientGradientRadius = typedArray.getDimensionPixelSize(R.styleable.SuperView_vGradientGradientRadius, 0);

        gradientStartColor = typedArray.getColor(R.styleable.SuperView_vGradientStartColor, -1);
        gradientCenterColor = typedArray.getColor(R.styleable.SuperView_vGradientCenterColor, -1);
        gradientEndColor = typedArray.getColor(R.styleable.SuperView_vGradientEndColor, -1);

        gradientType = typedArray.getInt(R.styleable.SuperView_vGradientType, linear);
        gradientUseLevel = typedArray.getBoolean(R.styleable.SuperView_vGradientUseLevel, false);

        useSelector = typedArray.getBoolean(R.styleable.SuperView_vUseSelector, false);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        //Log.e("cornersRadius", getMeasuredHeight() + "," + getMeasuredHeight() * 1.0f / dip2px(getContext(), 8));
        //设置默认圆角，高度除以8
        if (cornersRadius == 0 && cornersBottomLeftRadius == 0 && cornersTopLeftRadius == 0 && cornersTopRightRadius == 0 && cornersBottomRightRadius == 0) {
            setShapeCornersRadius(getMeasuredHeight() * 1.0f / dip2px(getContext(), 8));
            setUseShape();
        }
    }

    private void init() {
        setClickable(true);
        shapeBuilder = new ShapeBuilder();
        shapeBuilder
                .setShapeType(ShapeType.fromValue(shapeType))
                .setShapeCornersRadius(cornersRadius)
                .setShapeCornersTopLeftRadius(cornersTopLeftRadius)
                .setShapeCornersTopRightRadius(cornersTopRightRadius)
                .setShapeCornersBottomRightRadius(cornersBottomRightRadius)
                .setShapeCornersBottomLeftRadius(cornersBottomLeftRadius)
                .setShapeSolidColor(solidColor)
                .setShapeStrokeColor(strokeColor)
                .setShapeStrokeWidth(strokeWidth)
                .setShapeStrokeDashWidth(strokeDashWidth)
                .setShapeStrokeDashGap(strokeDashGap)
                .setShapeUseSelector(useSelector)
                .setShapeSelectorNormalColor(selectorNormalColor)
                .setShapeSelectorPressedColor(selectorPressedColor)
                .setShapeSelectorDisableColor(selectorDisableColor)
                .setShapeSizeWidth(sizeWidth)
                .setShapeSizeHeight(sizeHeight)
                .setShapeGradientType(ShapeGradientType.fromValue(gradientType))
                .setShapeGradientAngle(ShapeGradientAngle.fromValue(gradientAngle))
                .setShapeGradientUseLevel(gradientUseLevel)
                .setShapeGradientCenterX(gradientCenterX)
                .setShapeGradientCenterY(gradientCenterY)
                .setShapeGradientStartColor(gradientStartColor)
                .setShapeGradientCenterColor(gradientCenterColor)
                .setShapeGradientEndColor(gradientEndColor)
                .into(this);
    }


    /////////////////对外暴露的方法//////////////

    /**
     * 设置Shape类型
     *
     * @param type 类型
     * @return 对象
     */
    public SuperView setShapeType(int type) {
        this.shapeType = type;
        return this;
    }

    /**
     * 设置文字对其方式
     *
     * @param gravity 对齐方式
     * @return 对象
     */
    public SuperView setTextGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * 设置按下的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperView setShapeSelectorPressedColor(int color) {
        this.shapeBuilder.setShapeSelectorPressedColor(color);
        return this;
    }

    /**
     * 设置正常的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperView setShapeSelectorNormalColor(int color) {
        this.shapeBuilder.setShapeSelectorNormalColor(color);
        return this;
    }

    /**
     * 设置不可点击的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperView setShapeSelectorDisableColor(int color) {
        this.shapeBuilder.setShapeSelectorDisableColor(color);
        return this;
    }

    /**
     * 设置填充的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    public SuperView setShapeSolidColor(int color) {
        this.shapeBuilder.setShapeSolidColor(color);
        return this;
    }

    /**
     * 设置边框宽度
     *
     * @param strokeWidth 边框宽度值
     * @return 对象
     */
    public SuperView setShapeStrokeWidth(int strokeWidth) {
        this.shapeBuilder.setShapeStrokeWidth(dip2px(mContext, strokeWidth));
        return this;
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor 边框颜色
     * @return 对象
     */
    public SuperView setShapeStrokeColor(int strokeColor) {
        this.shapeBuilder.setShapeStrokeColor(strokeColor);
        return this;
    }

    /**
     * 设置边框虚线宽度
     *
     * @param strokeDashWidth 边框虚线宽度
     * @return 对象
     */
    public SuperView setShapeStrokeDashWidth(float strokeDashWidth) {
        this.shapeBuilder.setShapeStrokeDashWidth(dip2px(mContext, strokeDashWidth));
        return this;
    }

    /**
     * 设置边框虚线间隙
     *
     * @param strokeDashGap 边框虚线间隙值
     * @return 对象
     */
    public SuperView setShapeStrokeDashGap(float strokeDashGap) {
        this.shapeBuilder.setShapeStrokeDashGap(dip2px(mContext, strokeDashGap));
        return this;
    }

    /**
     * 设置圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperView setShapeCornersRadius(float radius) {
        this.shapeBuilder.setShapeCornersRadius(dip2px(mContext, radius));
        return this;
    }

    /**
     * 设置左上圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperView setShapeCornersTopLeftRadius(float radius) {
        this.shapeBuilder.setShapeCornersTopLeftRadius(dip2px(mContext, radius));
        return this;
    }

    /**
     * 设置右上圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperView setShapeCornersTopRightRadius(float radius) {
        this.shapeBuilder.setShapeCornersTopRightRadius(dip2px(mContext, radius));
        return this;
    }

    /**
     * 设置左下圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperView setShapeCornersBottomLeftRadius(float radius) {
        this.shapeBuilder.setShapeCornersBottomLeftRadius(dip2px(mContext, radius));
        return this;
    }

    /**
     * 设置右下圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    public SuperView setShapeCornersBottomRightRadius(float radius) {
        this.shapeBuilder.setShapeCornersBottomRightRadius(dip2px(mContext, radius));
        return this;
    }

    /**
     * 设置shape的宽度
     *
     * @param sizeWidth 宽
     * @return 对象
     */
    public SuperView setShapeSizeWidth(int sizeWidth) {
        this.shapeBuilder.setShapeSizeWidth(dip2px(mContext, sizeWidth));
        return this;
    }

    /**
     * 设置shape的高度
     *
     * @param sizeHeight 高
     * @return 对象
     */
    public SuperView setShapeSizeHeight(int sizeHeight) {
        this.shapeBuilder.setShapeSizeHeight(dip2px(mContext, sizeHeight));
        return this;
    }

    /**
     * 设置背景渐变方式
     *
     * @param gradientAngle 渐变类型
     * @return 对象
     */
    public SuperView setShapeGradientAngle(int gradientAngle) {
        this.shapeBuilder.setShapeGradientAngle(ShapeGradientAngle.fromValue(gradientAngle));
        return this;
    }

    /**
     * 设置渐变中心X
     *
     * @param gradientCenterX 中心x
     * @return 对象
     */
    public SuperView setShapeGradientCenterX(int gradientCenterX) {
        this.shapeBuilder.setShapeGradientCenterX(gradientCenterX);
        return this;
    }

    /**
     * 设置渐变中心Y
     *
     * @param gradientCenterY 中心y
     * @return 对象
     */
    public SuperView setShapeGradientCenterY(int gradientCenterY) {
        this.shapeBuilder.setShapeGradientCenterY(gradientCenterY);
        return this;
    }

    /**
     * 设置渐变半径
     *
     * @param gradientGradientRadius 渐变半径
     * @return 对象
     */
    public SuperView setShapeGradientGradientRadius(int gradientGradientRadius) {
        this.shapeBuilder.setShapeGradientGradientRadius(gradientGradientRadius);
        return this;
    }

    /**
     * 设置渐变开始的颜色
     *
     * @param gradientStartColor 开始颜色
     * @return 对象
     */
    public SuperView setShapeGradientStartColor(int gradientStartColor) {
        this.shapeBuilder.setShapeGradientStartColor(gradientStartColor);
        return this;
    }

    /**
     * 设置渐变中间的颜色
     *
     * @param gradientCenterColor 中间颜色
     * @return 对象
     */
    public SuperView setShapeGradientCenterColor(int gradientCenterColor) {
        this.shapeBuilder.setShapeGradientCenterColor(gradientCenterColor);
        return this;
    }

    /**
     * 设置渐变结束的颜色
     *
     * @param gradientEndColor 结束颜色
     * @return 对象
     */
    public SuperView setShapeGradientEndColor(int gradientEndColor) {
        this.shapeBuilder.setShapeGradientEndColor(gradientEndColor);
        return this;
    }

    /**
     * 设置渐变类型
     *
     * @param gradientType 类型
     * @return 对象
     */
    public SuperView setShapeGradientType(int gradientType) {
        this.shapeBuilder.setShapeGradientType(ShapeGradientType.fromValue(gradientType));
        return this;
    }

    /**
     * 设置是否使用UseLevel
     *
     * @param gradientUseLevel true  or  false
     * @return 对象
     */
    public SuperView setShapeGradientUseLevel(boolean gradientUseLevel) {
        this.shapeBuilder.setShapeGradientUseLevel(gradientUseLevel);
        return this;
    }

    /**
     * 是否使用selector
     *
     * @param useSelector true  or  false
     * @return 对象
     */
    public SuperView setShapeUseSelector(boolean useSelector) {
        this.shapeBuilder.setShapeUseSelector(useSelector);
        return this;
    }

    /**
     * 使用shape
     * 所有与shape相关的属性设置之后调用此方法才生效
     */
    public void setUseShape() {
        this.shapeBuilder.into(this);
    }


    /**
     * 单位转换工具类
     *
     * @param context  上下文对象
     * @param dipValue 值
     * @return 返回值
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
