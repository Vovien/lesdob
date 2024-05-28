package com.jmbon.widget.progress_button

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.hjq.toast.ToastUtils.setGravity
import com.jmbon.widget.R
import com.jmbon.widget.progress_button.JmbonButton.LoginStatus.IS_BUTTON_STATE
import com.jmbon.widget.progress_button.JmbonButton.LoginStatus.IS_LOADING_STATE


/**
 * Created by Allen on 2017/7/6.
 *
 *
 * 超级按钮  实现shape所有的属性
 */
class JmbonButton : FrameLayout {

    companion object {
        const val WHITE_LOADING = 1
        const val BLACK_LOADING = 2
        const val GREEN_LOADING = 3

        const val GREEN_FULL = 1
        const val BLACK_BORDER = 2
        const val GREEN_BORDER = 3
        const val RED_FULL = 4
        const val GRAY_BORDER = 6

        //shape的样式
        const val RECTANGLE = 0
        const val OVAL = 1
        const val LINE = 2
        const val RING = 3


        //渐变色的显示方式
        const val TOP_BOTTOM = 0
        const val TR_BL = 1
        const val RIGHT_LEFT = 2
        const val BR_TL = 3
        const val BOTTOM_TOP = 4
        const val BL_TR = 5
        const val LEFT_RIGHT = 6
        const val TL_BR = 7

        //文字显示的位置方式
        const val TEXT_GRAVITY_CENTER = 0
        const val TEXT_GRAVITY_LEFT = 1
        const val TEXT_GRAVITY_RIGHT = 2
        const val TEXT_GRAVITY_TOP = 3
        const val TEXT_GRAVITY_BOTTOM = 4

    }

    private var showLoading = false
    private var showLoadingWhenClick = false
    private var onlyLoginToModifyState = true
    private var btn: TextView? = null
    private var btnStyle: Int = 0
    private var loadingColorType: Int = 0
    private var lottieView: LottieAnimationView? = null
    private var canClick = true


    private val defaultColor = 0x20000000
    private val defaultSelectorColor = 0x20000000

    private var solidColorData = Color.WHITE
    private var selectorPressedColor = 0
    private var selectorDisableColor = 0
    private var selectorNormalColor = 0

    private var cornersRadius = 0f
    private var cornersTopLeftRadius = 0f
    private var cornersTopRightRadius = 0f
    private var cornersBottomLeftRadius = 0f
    private var cornersBottomRightRadius = 0f

    private var strokeWidth = 0
    private var strokeColor = 0
    private var strokeDisableColor = -1
    private var strokePressedColor = -1

    private var strokeDashWidth = 0f
    private var strokeDashGap = 0f

    private var sizeWidth = 0
    private var sizeHeight = 0

    private var gradientAngle = 0
    private var gradientCenterX = 0
    private var gradientCenterY = 0
    private var gradientGradientRadius = 0

    private var gradientStartColor = 0
    private var gradientCenterColor = 0
    private var gradientEndColor = 0

    private var gradientType = 0

    //"linear"	线形渐变。这也是默认的模式
    private val linear = 0

    //"radial"	辐射渐变。startColor即辐射中心的颜色
    private val radial = 1

    //"sweep"	扫描线渐变。
    private val sweep = 2

    private var gradientUseLevel = false

    private var useSelector = false


    private var shapeType = 0

    private var gravity = 0

    private var shapeBuilder: ShapeDrawable

    private var loadingScale = 1f
    var selectedText: String = ""
    var normalText: String = ""
    var disableText: String = ""


    var drawableLeft: Drawable? = null
    var drawableTop: Drawable? = null
    var drawableRight: Drawable? = null
    var drawableBottom: Drawable? = null
    var drawableStart: Drawable? = null
    var drawableEnd: Drawable? = null
    var drawablePadding = 0


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        btn = TextView(context, attrs, defStyleAttr)
        btn?.alpha = alpha


        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.JmbonButton)
        val height: Int =
            typedArray.getLayoutDimension(R.styleable.JmbonButton_android_layout_height, 40)

        drawableLeft = typedArray.getDrawable(R.styleable.JmbonButton_jDrawableLeft)
        drawableTop = typedArray.getDrawable(R.styleable.JmbonButton_jDrawableTop)
        drawableBottom = typedArray.getDrawable(R.styleable.JmbonButton_jDrawableBottom)
        drawableRight = typedArray.getDrawable(R.styleable.JmbonButton_jDrawableRight)
        drawableStart = typedArray.getDrawable(R.styleable.JmbonButton_jDrawableStart)
        drawableEnd = typedArray.getDrawable(R.styleable.JmbonButton_jDrawableEnd)
        drawablePadding = typedArray.getDimensionPixelSize(
            R.styleable.JmbonButton_jDrawablePadding,
            drawablePadding
        )

        // This call will save the initial left/right drawables
//        btn?.setCompoundDrawablesWithIntrinsicBounds(
//            drawableLeft, drawableTop, drawableRight, drawableBottom
//        )
        btn?.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawableLeft,
            drawableTop,
            drawableRight,
            drawableBottom
        )
        btn?.compoundDrawablePadding = drawablePadding

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        btn?.layoutParams = params
        btn?.setPadding(0, 0, 0, 0)

        btn?.gravity = Gravity.CENTER
        addView(btn)

        normalText = typedArray.getString(R.styleable.JmbonButton_android_text) ?: ""
        selectedText = typedArray.getString(R.styleable.JmbonButton_jSelectText) ?: ""
        disableText = typedArray.getString(R.styleable.JmbonButton_jDisableText) ?: ""

        shapeBuilder = ShapeDrawable()
        gravity = typedArray.getInt(R.styleable.JmbonButton_jGravity, 0)

        shapeType =
            typedArray.getInt(R.styleable.JmbonButton_jShapeType, GradientDrawable.RECTANGLE)

        solidColorData = typedArray.getColor(R.styleable.JmbonButton_jSolidColor, defaultColor)

        selectorPressedColor =
            typedArray.getColor(R.styleable.JmbonButton_jSelectorPressedColor, defaultSelectorColor)
        selectorDisableColor =
            typedArray.getColor(R.styleable.JmbonButton_jSelectorDisableColor, defaultSelectorColor)
        selectorNormalColor =
            typedArray.getColor(R.styleable.JmbonButton_jSelectorNormalColor, defaultSelectorColor)

        cornersRadius =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jCornersRadius, -1).toFloat()

        cornersTopLeftRadius =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jCornersTopLeftRadius, 0)
                .toFloat()
        cornersTopRightRadius =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jCornersTopRightRadius, 0)
                .toFloat()
        cornersBottomLeftRadius =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jCornersBottomLeftRadius, 0)
                .toFloat()
        cornersBottomRightRadius =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jCornersBottomRightRadius, 0)
                .toFloat()

        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jStrokeWidth, 0)
        strokeDashWidth =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jStrokeDashWidth, 0).toFloat()
        strokeDashGap =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jStrokeDashGap, 0).toFloat()

        strokeColor = typedArray.getColor(R.styleable.JmbonButton_jStrokeColor, defaultColor)
        strokePressedColor =
            typedArray.getColor(R.styleable.JmbonButton_jStrokePressedColor, defaultColor)
        strokeDisableColor =
            typedArray.getColor(R.styleable.JmbonButton_jStrokeDisableColor, defaultColor)

        sizeWidth = typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jSizeWidth, 0)
        sizeHeight = typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jSizeHeight, 48f.dp())

        gradientAngle = typedArray.getFloat(R.styleable.JmbonButton_jGradientAngle, -1f).toInt()
        gradientCenterX =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jGradientCenterX, 0)
        gradientCenterY =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jGradientCenterY, 0)
        gradientGradientRadius =
            typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jGradientGradientRadius, 0)

        gradientStartColor = typedArray.getColor(R.styleable.JmbonButton_jGradientStartColor, -1)
        gradientCenterColor = typedArray.getColor(R.styleable.JmbonButton_jGradientCenterColor, -1)
        gradientEndColor = typedArray.getColor(R.styleable.JmbonButton_jGradientEndColor, -1)

        gradientType = typedArray.getInt(R.styleable.JmbonButton_jGradientType, linear)
        gradientUseLevel = typedArray.getBoolean(R.styleable.JmbonButton_jGradientUseLevel, false)

        useSelector = typedArray.getBoolean(R.styleable.JmbonButton_jUseSelector, false)


        showLoading = typedArray.getBoolean(R.styleable.JmbonButton_jShowLoading, showLoading)
        btnStyle = typedArray.getInt(R.styleable.JmbonButton_jButtonStyle, 0)

        // 圆角
        cornersRadius = if (cornersRadius == -1f) height / 5f else cornersRadius
        if (showLoading) {
            showLoadingWhenClick =
                typedArray.getBoolean(
                    R.styleable.JmbonButton_jShowLoadingWhenClick,
                    showLoadingWhenClick
                )
            onlyLoginToModifyState =
                typedArray.getBoolean(
                    R.styleable.JmbonButton_jOnlyLoginToModifyState,
                    onlyLoginToModifyState
                )
            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jStrokeWidth, 1)
            loadingColorType =
                typedArray.getDimensionPixelSize(R.styleable.JmbonButton_jLoadingColorType, 0)
            if (loadingColorType == 0)
                loadingColorType = if (btnStyle == 0)
                    GREEN_LOADING
                else if (btnStyle == 4) WHITE_LOADING else btnStyle
            loadingScale = if (height == 32f.dp()) 0.6f else 1f
            addLoading(strokeWidth, loadingColorType)
        }

//        <enum name="white_border" value="0" />
//        <enum name="black_border" value="1" />
//        <enum name="green_full" value="2" />
        if (btnStyle != 0) {
            // 绿色描边、绿色、红色状态 、标题状态、使用粗体
            if (btnStyle == GREEN_BORDER || btnStyle == GREEN_FULL || btnStyle == RED_FULL) {
                btn?.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            setEnableStyle()
        }
        init()
        val enable = typedArray.getBoolean(R.styleable.JmbonButton_android_enabled, true)
        if (!enable)
            isEnabled = enable
        typedArray.recycle()

    }

    private fun setEnableStyle() {
        when (btnStyle) {
            GRAY_BORDER -> {
                btn?.setTextColor(ColorUtils.getColor(R.color.text_color))
                selectorPressedColor = Color.TRANSPARENT
                selectorDisableColor = Color.TRANSPARENT
                selectorNormalColor = Color.TRANSPARENT
                strokeWidth = 1f.dp()
                strokeColor = ColorUtils.getColor(R.color.color_979797)
                strokePressedColor = ColorUtils.getColor(R.color.color_262626)
                strokeDisableColor = ColorUtils.getColor(R.color.picture_color_e5)
            }

            BLACK_BORDER -> {
                btn?.setTextColor(ColorUtils.getColor(R.color.text_color))
                selectorPressedColor = Color.TRANSPARENT
                selectorDisableColor = Color.TRANSPARENT
                selectorNormalColor = Color.TRANSPARENT
                strokeWidth = 1f.dp()
                strokeColor = ColorUtils.getColor(R.color.text_color)
                strokePressedColor = ColorUtils.getColor(R.color.color_262626)
                strokeDisableColor = ColorUtils.getColor(R.color.picture_color_e5)
            }
            GREEN_BORDER -> {
                btn?.setTextColor(
                    createColorStateList(
                        ColorUtils.getColor(R.color.color_80_currency),
                        ColorUtils.getColor(R.color.color_currency)
                    )
                )
                selectorPressedColor = Color.TRANSPARENT
                selectorDisableColor = Color.TRANSPARENT
                selectorNormalColor = Color.TRANSPARENT
                strokeWidth = 1f.dp()
                strokeColor = ColorUtils.getColor(R.color.color_currency)
                strokePressedColor = ColorUtils.getColor(R.color.color_80_currency)
                strokeDisableColor = ColorUtils.getColor(R.color.picture_color_e5)
            }
            GREEN_FULL -> {
                btn?.setTextColor(Color.WHITE)
                selectorPressedColor = ColorUtils.getColor(R.color.color_80_currency)
                selectorDisableColor = ColorUtils.getColor(R.color.picture_color_e5)
                selectorNormalColor = ColorUtils.getColor(R.color.color_currency)
                strokeWidth = 0
                strokeColor = ColorUtils.getColor(R.color.transparent)
            }
            RED_FULL -> {
                btn?.setTextColor(Color.WHITE)
                selectorPressedColor = ColorUtils.getColor(R.color.color_FF4B4B)
                selectorDisableColor = ColorUtils.getColor(R.color.transparent)
                selectorNormalColor = ColorUtils.getColor(R.color.color_FF4B4B)
                strokeWidth = 0
                strokeColor = ColorUtils.getColor(R.color.transparent)

            }
            else -> {
                btn?.setTextColor(Color.WHITE)
                selectorPressedColor = ColorUtils.getColor(R.color.color_80_currency)
                selectorDisableColor = ColorUtils.getColor(R.color.picture_color_e5)
                selectorNormalColor = ColorUtils.getColor(R.color.color_currency)
                strokeWidth = 0
                strokeColor = ColorUtils.getColor(R.color.transparent)
            }
        }
    }

    private fun createColorStateList(pressed: Int, normal: Int): ColorStateList {
        //状态
        val states = arrayOfNulls<IntArray>(2)
        //按下
        states[0] = intArrayOf(android.R.attr.state_pressed)
        //默认
        states[1] = intArrayOf()

        //状态对应颜色值（按下，默认）
        val colors = intArrayOf(pressed, normal)
        return ColorStateList(states, colors)
    }

    private fun addLoading(strokeWidth: Int, loadingColorType: Int) {
        lottieView = LottieAnimationView(context)
        lottieView?.setCacheComposition(true)
        when (loadingColorType) {
            WHITE_LOADING -> lottieView?.setAnimation("lottie/white_loading.json")
            BLACK_LOADING -> lottieView?.setAnimation("lottie/black_loading.json")
            else -> lottieView?.setAnimation("lottie/green_loading.json")
        }
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        params.setMargins(strokeWidth)
        lottieView?.layoutParams = params
        if (loadingScale != 1f) {
            lottieView?.scaleX = loadingScale
            lottieView?.scaleY = loadingScale
        }

        addView(lottieView)
        lottieView?.playAnimation()
        lottieView?.repeatCount = LottieDrawable.INFINITE
        buttonVisible()
    }

    private fun loadingVisible() {
        if (showLoading) {
            stateLoading()
            canClick = false
            // 在显示 loading 状态时 切换到正常状态
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (canClick) super.onTouchEvent(event) else true
    }

    fun setTextWithSelect(text: CharSequence?) {
        text?.apply {
            isSelected = true
            btn?.text = this
        }
    }

    fun setTextWithUnSelect(text: CharSequence?) {
        text?.apply {
            isSelected = true
            btn?.text = this
        }
    }

    private fun buttonVisible() {
        lottieView?.gone()
        canClick = true
        setShapeSelectorPressedColor(selectorPressedColor).setUseShape()
        btn?.visible()
    }

    fun stateLoading() {
        if (showLoading) {
            if (isSelected) {
                when (loadingColorType) {
                    GREEN_LOADING, BLACK_LOADING -> lottieView?.setAnimation("lottie/gray_loading.json")
                    else -> lottieView?.setAnimation("lottie/white_loading.json")
                }
            } else {
                when (loadingColorType) {
                    WHITE_LOADING -> lottieView?.setAnimation("lottie/white_loading.json")
                    BLACK_LOADING -> lottieView?.setAnimation("lottie/black_loading.json")
                    else -> lottieView?.setAnimation("lottie/green_loading.json")
                }
            }
            lottieView?.visible()
            btn?.gone()
        }
    }

    /**
     * 屏蔽点击事件
     */
    fun stateLoadingNetClick() {
        loadingVisible()
    }

    fun stateButton() {
        buttonVisible()
        if (btnStyle != 0) {
            setEnableStyle()
            init()
        }
    }

    fun stateLoadingGrayButton() {
        stateLoading()
        canClick = false
        setShapeSelectorPressedColor(ColorUtils.getColor(R.color.color_BFBFBF))
        setShapeSelectorNormalColor(ColorUtils.getColor(R.color.color_BFBFBF))
        setUseShape()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (btnStyle != 0) {
            // enable
            buttonVisible()
            btn?.isEnabled = enabled
            if (enabled) {
                setEnableStyle()
                init()
                if (disableText.isNotEmpty())
                    btn?.text = normalText
            } else {

                if (btnStyle == GRAY_BORDER || btnStyle == BLACK_BORDER || btnStyle == GREEN_BORDER) {
                    btn?.setTextColor(ColorUtils.getColor(R.color.picture_color_e5))
                    setShapeStrokeWidth(1)
                        .setShapeStrokeColor(ColorUtils.getColor(R.color.picture_color_e5))
                }
                if (btnStyle == RED_FULL) {
                    btn?.setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
                    setShapeStrokeWidth(1)
                        .setShapeStrokeDashWidth(5f)
                        .setShapeStrokeDashGap(5f)
                        .setShapeStrokeColor(ColorUtils.getColor(R.color.picture_color_e5))
                }
                if (disableText.isNotEmpty())
                    btn?.text = disableText
            }
            setUseShape()
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (canClick)
            super.setOnClickListener(l)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (btnStyle != 0) {
            // enable
            super.setEnabled(true)
            if (btnStyle != 0) {
                // 选中的时候不应该显示 loading
                if (selected) {
                    when (btnStyle) {
                        GRAY_BORDER, BLACK_BORDER, GREEN_BORDER -> {
                            btn?.setTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
                            setShapeStrokeWidth(1)
                            setShapeStrokeColor(ColorUtils.getColor(R.color.picture_color_e5))
                            setShapeStrokePressedColor(ColorUtils.getColor(R.color.picture_color_e5))
                        }
                        RED_FULL -> {
                            btn?.setTextColor(ColorUtils.getColor(R.color.white))
                            setShapeSelectorPressedColor(ColorUtils.getColor(R.color.color_BFBFBF))
                            setShapeSelectorNormalColor(ColorUtils.getColor(R.color.color_BFBFBF))
                            setShapeStrokeColor(ColorUtils.getColor(R.color.picture_color_e5))
                            setShapeStrokePressedColor(ColorUtils.getColor(R.color.picture_color_e5))
                        }
                        else -> {
                            btn?.setTextColor(Color.WHITE)
                            setShapeSelectorPressedColor(ColorUtils.getColor(R.color.color_BFBFBF))
                            setShapeSelectorNormalColor(ColorUtils.getColor(R.color.color_BFBFBF))
                        }
                    }
                    setUseShape()
                    if (selectedText.isNotEmpty())
                        btn?.text = selectedText
                } else {
                    // 开启正常默认。取消禁用状态
                    setEnableStyle()
                    init()
                    if (selectedText.isNotEmpty())
                        btn?.text = normalText
                }
            }
            buttonVisible()
        }
    }


    override fun performClick(): Boolean {
        if (onlyLoginToModifyState) {
            if (LoginStatus.isLogin)
                showLoginWhenHasView()
        } else showLoginWhenHasView()

        return super.performClick()
    }

    private fun showLoginWhenHasView() {
        if (showLoadingWhenClick && lottieView?.visibility != VISIBLE) {
            loadingVisible()
        }
    }

    fun setLoading(
        show: Boolean,
        showType: Int = WHITE_LOADING,
        paddingDp: Int = SizeUtils.dp2px(1f)
    ) {
        if (showLoading != show) {
            showLoading = show
            if (showLoading) {
                addLoading(paddingDp, showType)
            } else lottieView?.let { removeView(it) }
        }
    }

    fun Float.dp(): Int {
        return SizeUtils.dp2px(this)
    }

    fun Int.dp(): Int {
        return SizeUtils.dp2px(this.toFloat())
    }

    private fun init() {
        shapeBuilder
            .setShapeType(shapeType)
            .setShapeCornersRadius(cornersRadius)
            .setShapeCornersTopLeftRadius(cornersTopLeftRadius)
            .setShapeCornersTopRightRadius(cornersTopRightRadius)
            .setShapeCornersBottomRightRadius(cornersBottomRightRadius)
            .setShapeCornersBottomLeftRadius(cornersBottomLeftRadius)
            .setShapeSolidColor(solidColor)
            .setShapeStrokeColor(strokeColor)
            .setShapeStrokePressedColor(strokePressedColor)
            .setShapeStrokeDisableColor(strokeDisableColor)
            .setShapeStrokeWidth(strokeWidth)
            .setShapeStrokeDashWidth(strokeDashWidth)
            .setShapeStrokeDashGap(strokeDashGap)
            .setShapeUseSelector(useSelector)
            .setShapeSelectorNormalColor(selectorNormalColor)
            .setShapeSelectorPressedColor(selectorPressedColor)
            .setShapeSelectorDisableColor(selectorDisableColor)
            .setShapeSizeWidth(sizeWidth)
            .setShapeSizeHeight(sizeHeight)
            .setShapeGradientType(gradientType)
            .setShapeGradientAngle(gradientAngle)
            .setShapeGradientUseLevel(gradientUseLevel)
            .setShapeGradientCenterX(gradientCenterX)
            .setShapeGradientCenterY(gradientCenterY)
            .setShapeGradientStartColor(gradientStartColor)
            .setShapeGradientCenterColor(gradientCenterColor)
            .setShapeGradientEndColor(gradientEndColor)
            .setShapeGradientEndColor(gradientEndColor)
            .into(this)
        if (isInEditMode)
            return
        setSGravity()
    }


    /**
     * 设置文字对其方式
     */
    private fun setSGravity() {
        when (gravity) {
            0 -> setGravity(Gravity.CENTER)
            1 -> setGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)
            2 -> setGravity(Gravity.RIGHT or Gravity.CENTER_VERTICAL)
            3 -> setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            4 -> setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        }
    }


    /////////////////对外暴露的方法//////////////

    /////////////////对外暴露的方法//////////////
    /**
     * 设置Shape类型
     *
     * @param type 类型
     * @return 对象
     */
    fun setShapeType(type: Int): JmbonButton {
        shapeType = type
        return this
    }

    /**
     * 设置文字对其方式
     *
     * @param gravity 对齐方式
     * @return 对象
     */
    fun setTextGravity(gravity: Int): JmbonButton {
        this.gravity = gravity
        return this
    }

    /**
     * 设置按下的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    fun setShapeSelectorPressedColor(color: Int): JmbonButton {
        shapeBuilder.setShapeSelectorPressedColor(color)
        return this
    }

    /**
     * 设置正常的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    fun setShapeSelectorNormalColor(color: Int): JmbonButton {
        shapeBuilder.setShapeSelectorNormalColor(color)
        return this
    }

    fun getShapeSelectorNormalColor(): Int {
        return shapeBuilder.getShapeSelectorNormalColor()
    }

    /**
     * 设置不可点击的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    fun setShapeSelectorDisableColor(color: Int): JmbonButton {
        shapeBuilder.setShapeSelectorDisableColor(color)
        return this
    }

    /**
     * 设置填充的颜色
     *
     * @param color 颜色
     * @return 对象
     */
    fun setShapeSolidColor(color: Int): JmbonButton {
        shapeBuilder.setShapeSolidColor(color)
        return this
    }

    /**
     * 设置边框宽度
     *
     * @param strokeWidth 边框宽度值
     * @return 对象
     */
    fun setShapeStrokeWidth(strokeWidth: Int): JmbonButton {
        shapeBuilder.setShapeStrokeWidth(strokeWidth.toFloat().dp())
        return this
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor 边框颜色
     * @return 对象
     */
    fun setShapeStrokeColor(strokeColor: Int): JmbonButton {
        shapeBuilder.setShapeStrokeColor(strokeColor)
        return this
    }

    /**
     * 设置点击边框颜色
     *
     * @param strokeColor 边框颜色
     * @return 对象
     */
    fun setShapeStrokePressedColor(strokePressedColor: Int): JmbonButton {
        shapeBuilder.setShapeStrokePressedColor(strokePressedColor)
        return this
    }

    /**
     * 设置边框虚线宽度
     *
     * @param strokeDashWidth 边框虚线宽度
     * @return 对象
     */
    fun setShapeStrokeDashWidth(strokeDashWidth: Float): JmbonButton {
        shapeBuilder.setShapeStrokeDashWidth(strokeDashWidth.dp().toFloat())
        return this
    }

    /**
     * 设置边框虚线间隙
     *
     * @param strokeDashGap 边框虚线间隙值
     * @return 对象
     */
    fun setShapeStrokeDashGap(strokeDashGap: Float): JmbonButton {
        shapeBuilder.setShapeStrokeDashGap(strokeDashGap.dp().toFloat())
        return this
    }

    /**
     * 设置圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    fun setShapeCornersRadiusDp(radius: Float): JmbonButton {
        shapeBuilder.setShapeCornersRadius(radius.dp().toFloat())
        return this
    }

    fun setShapeCornersRadiusPx(radius: Float): JmbonButton {
        shapeBuilder.setShapeCornersRadius(radius)
        return this
    }

    /**
     * 设置左上圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    fun setShapeCornersTopLeftRadius(radius: Float): JmbonButton {
        shapeBuilder.setShapeCornersTopLeftRadius(radius.dp().toFloat())
        return this
    }

    /**
     * 设置右上圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    fun setShapeCornersTopRightRadius(radius: Float): JmbonButton {
        shapeBuilder.setShapeCornersTopRightRadius(radius.dp().toFloat())
        return this
    }

    /**
     * 设置左下圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    fun setShapeCornersBottomLeftRadius(radius: Float): JmbonButton {
        shapeBuilder.setShapeCornersBottomLeftRadius(radius.dp().toFloat())
        return this
    }

    /**
     * 设置右下圆角半径
     *
     * @param radius 半径
     * @return 对象
     */
    fun setShapeCornersBottomRightRadius(radius: Float): JmbonButton {
        shapeBuilder.setShapeCornersBottomRightRadius(radius.dp().toFloat())
        return this
    }

    /**
     * 设置shape的宽度
     *
     * @param sizeWidth 宽
     * @return 对象
     */
    fun setShapeSizeWidth(sizeWidth: Int): JmbonButton {
        shapeBuilder.setShapeSizeWidth(sizeWidth.toFloat().dp())
        return this
    }

    /**
     * 设置shape的高度
     *
     * @param sizeHeight 高
     * @return 对象
     */
    fun setShapeSizeHeight(sizeHeight: Int): JmbonButton {
        shapeBuilder.setShapeSizeHeight(sizeHeight.dp())
        return this
    }

    /**
     * 设置背景渐变方式
     *
     * @param gradientAngle 渐变类型
     * @return 对象
     */
    fun setShapeGradientAngle(gradientAngle: Int): JmbonButton {
        shapeBuilder.setShapeGradientAngle(gradientAngle)
        return this
    }

    /**
     * 设置渐变中心X
     *
     * @param gradientCenterX 中心x
     * @return 对象
     */
    fun setShapeGradientCenterX(gradientCenterX: Int): JmbonButton {
        shapeBuilder.setShapeGradientCenterX(gradientCenterX)
        return this
    }

    /**
     * 设置渐变中心Y
     *
     * @param gradientCenterY 中心y
     * @return 对象
     */
    fun setShapeGradientCenterY(gradientCenterY: Int): JmbonButton {
        shapeBuilder.setShapeGradientCenterY(gradientCenterY)
        return this
    }

    /**
     * 设置渐变半径
     *
     * @param gradientGradientRadius 渐变半径
     * @return 对象
     */
    fun setShapeGradientGradientRadius(gradientGradientRadius: Int): JmbonButton {
        shapeBuilder.setShapeGradientGradientRadius(gradientGradientRadius)
        return this
    }

    /**
     * 设置渐变开始的颜色
     *
     * @param gradientStartColor 开始颜色
     * @return 对象
     */
    fun setShapeGradientStartColor(gradientStartColor: Int): JmbonButton {
        shapeBuilder.setShapeGradientStartColor(gradientStartColor)
        return this
    }

    /**
     * 设置渐变中间的颜色
     *
     * @param gradientCenterColor 中间颜色
     * @return 对象
     */
    fun setShapeGradientCenterColor(gradientCenterColor: Int): JmbonButton {
        shapeBuilder.setShapeGradientCenterColor(gradientCenterColor)
        return this
    }

    /**
     * 设置渐变结束的颜色
     *
     * @param gradientEndColor 结束颜色
     * @return 对象
     */
    fun setShapeGradientEndColor(gradientEndColor: Int): JmbonButton {
        shapeBuilder.setShapeGradientEndColor(gradientEndColor)
        return this
    }

    /**
     * 设置渐变类型
     *
     * @param gradientType 类型
     * @return 对象
     */
    fun setShapeGradientType(gradientType: Int): JmbonButton {
        shapeBuilder.setShapeGradientType(gradientType)
        return this
    }

    /**
     * 设置是否使用UseLevel
     *
     * @param gradientUseLevel true  or  false
     * @return 对象
     */
    fun setShapeGradientUseLevel(gradientUseLevel: Boolean): JmbonButton {
        shapeBuilder.setShapeGradientUseLevel(gradientUseLevel)
        return this
    }

    /**
     * 是否使用selector
     *
     * @param useSelector true  or  false
     * @return 对象
     */
    fun setShapeUseSelector(useSelector: Boolean): JmbonButton {
        shapeBuilder.setShapeUseSelector(useSelector)
        return this
    }

    fun setIsShowLoadingWhenClick(showLoadingWhenClick: Boolean) {
        this.showLoadingWhenClick = showLoadingWhenClick
    }

    /**
     * 只有登陆对的时候显示 longing
     *
     * @param type 类型
     * @return 对象
     */
    fun setOnlyLoginToModifyState(onlyLoginToModifyState: Boolean) {
        this.onlyLoginToModifyState = onlyLoginToModifyState
    }


    /**
     * 使用shape
     * 所有与shape相关的属性设置之后调用此方法才生效
     */
    fun setUseShape() {
        shapeBuilder.into(this)
    }

    override fun setAlpha(alpha: Float) {
        super.setAlpha(alpha)

        btn?.alpha = alpha
    }

    fun setBtnStyle(style: Int) {
        this.btnStyle = style
        setEnableStyle()
        init()
    }

    var text: CharSequence
        get() = btn?.text.toString()
        set(value) {
            normalText = value.toString()
            btn?.text = normalText
        }


    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE)
            btn?.visible()
    }

    fun getStatus(): Int {
        LogUtils.i("Text: ${btn?.isVisible} loading: ${lottieView?.isVisible} enable $isEnabled select $isSelected")
        return if (lottieView?.isVisible == true && btn?.isGone == true)
            IS_LOADING_STATE
        else if (lottieView?.isGone == true && btn?.isVisible == true) {
            IS_BUTTON_STATE
        } else IS_BUTTON_STATE
    }

    object LoginStatus {
        var isLogin = false
        const val IS_LOADING_STATE = 0
        const val IS_BUTTON_STATE = 1
    }

}

