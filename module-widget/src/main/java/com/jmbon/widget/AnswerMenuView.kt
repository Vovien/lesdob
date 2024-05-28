package com.jmbon.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout

/**
 * @author : leimg
 * time   : 2021/4/12
 * desc   : 回答问题的菜单组件view
 * version: 1.0
 */
class AnswerMenuView : RelativeLayout {

    lateinit var ivHide: ImageView //隐藏软键盘按钮
    lateinit var ivTitleSize: ImageView //字体大小功能按钮
    lateinit var ivBold: ImageView //字体加粗功能按钮
    lateinit var ivItalic: ImageView //字体倾斜功能按钮
    lateinit var ivUl: ImageView //字体无序功能按钮
    lateinit var ivOl: ImageView //字体有序功能按钮
    lateinit var ivQuote: ImageView //字体引号功能按钮
    lateinit var ivPicture: ImageView //添加图片功能按钮
    lateinit var ivVideo: ImageView //添加视频功能按钮

    lateinit var ivTitleSizeDisabled: ImageView //禁止字体大小功能按钮
    lateinit var ivBoldDisabled: ImageView //禁止字体加粗功能按钮
    lateinit var ivItalicDisabled: ImageView //禁止字体倾斜功能按钮
    lateinit var ivUlDisabled: ImageView //禁止字体无序功能按钮
    lateinit var ivOlDisabled: ImageView //禁止字体有序功能按钮
    lateinit var ivQuoteDisabled: ImageView //禁止字体引号功能按钮
    lateinit var ivPictureDisabled: ImageView //禁止图片功能按钮
    lateinit var ivVideoDisabled: ImageView //禁止视频功能按钮


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        LayoutInflater.from(context).inflate(R.layout.answer_menu_layout, this, true)
        initViews()

    }

    private fun initViews() {
        ivHide = findViewById<ImageView>(R.id.iv_hide)
        ivTitleSize = findViewById<ImageView>(R.id.iv_title_size)
        ivBold = findViewById<ImageView>(R.id.iv_bold)
        ivItalic = findViewById<ImageView>(R.id.iv_italic)
        ivUl = findViewById<ImageView>(R.id.iv_ul)
        ivOl = findViewById<ImageView>(R.id.iv_ol)
        ivQuote = findViewById<ImageView>(R.id.iv_quote)
        ivPicture = findViewById<ImageView>(R.id.iv_picture)
        ivVideo = findViewById<ImageView>(R.id.iv_video)

        ivTitleSizeDisabled = findViewById<ImageView>(R.id.iv_title_size_disabled)
        ivBoldDisabled = findViewById<ImageView>(R.id.iv_bold_disabled)
        ivItalicDisabled = findViewById<ImageView>(R.id.iv_italic_disabled)
        ivUlDisabled = findViewById<ImageView>(R.id.iv_ul_disabled)
        ivOlDisabled = findViewById<ImageView>(R.id.iv_ol_disabled)
        ivQuoteDisabled = findViewById<ImageView>(R.id.iv_quote_disabled)
        ivPictureDisabled = findViewById<ImageView>(R.id.iv_picture_disabled)
        ivVideoDisabled = findViewById<ImageView>(R.id.iv_video_disabled)


    }

    fun setFontSizeImageResource(imgSrc: Int) {
        ivTitleSize.setImageResource(imgSrc)
    }

    fun setBoldImageResource(imgSrc: Int) {
        ivBold.setImageResource(imgSrc)
    }

    fun setItalicImageResource(imgSrc: Int) {
        ivItalic.setImageResource(imgSrc)
    }

    fun setUlImageResource(imgSrc: Int) {
        ivUl.setImageResource(imgSrc)
    }

    fun setOlImageResource(imgSrc: Int) {
        ivOl.setImageResource(imgSrc)
    }

    fun setQuoteImageResource(imgSrc: Int) {
        ivQuote.setImageResource(imgSrc)
    }

    fun setPictureImageResource(imgSrc: Int) {
        ivPicture.setImageResource(imgSrc)
    }

    fun setVideoImageResource(imgSrc: Int) {
        ivVideo.setImageResource(imgSrc)
    }


    fun addHideSoftKeywordClickListener(listener: OnClickListener): Unit {
        ivHide.setOnClickListener(listener)
    }

    fun addFontSizeClickListener(listener: OnClickListener) {
        ivTitleSize.setOnClickListener(listener)
    }

    fun addBoldClickListener(listener: OnClickListener) {
        ivBold.setOnClickListener(listener)
    }

    fun addItalicClickListener(listener: OnClickListener) {
        ivItalic.setOnClickListener(listener)
    }

    fun addUlOrderClickListener(listener: OnClickListener) {
        ivUl.setOnClickListener(listener)
    }

    fun addOlOrderClickListener(listener: OnClickListener) {
        ivOl.setOnClickListener(listener)
    }

    fun addQuoteClickListener(listener: OnClickListener) {
        ivQuote.setOnClickListener(listener)
    }

    fun addPictureClickListener(listener: OnClickListener) {
        ivPicture.setOnClickListener(listener)
    }

    fun addVideoClickListener(listener: OnClickListener) {
        ivVideo.setOnClickListener(listener)
    }

}