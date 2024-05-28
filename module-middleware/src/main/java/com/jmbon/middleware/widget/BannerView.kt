package com.jmbon.middleware.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.ScreenUtils
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.databinding.ViewBannerLayoutBinding
import com.jmbon.middleware.extention.loadUrlWithSize
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import java.util.Collections


/******************************************************************************
 * Description: 通用的Banner组件
 *
 * Author: jhg
 *
 * Date: 2023/3/9
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class BannerView : FrameLayout {

    private val bannerWidth = ScreenUtils.getScreenWidth() - 40.dp
    private val bannerHeight = 110.dp

    /**
     * 事件事件
     */
    private var clickAction: (position: Int) -> Unit = {}

    private val bannerAdapter by lazy {
        object: BannerImageAdapter<CommonBanner>(
            Collections.emptyList()) {
            override fun onBindView(
                holder: BannerImageHolder?,
                data: CommonBanner?,
                position: Int,
                size: Int
            ) {
                holder?.imageView?.let {
                    it.loadUrlWithSize(data?.img, width = bannerWidth, height = bannerHeight, placeholder = 0)
                }
            }
        }
    }

    private val bannerLayout by lazy {
        ViewBannerLayoutBinding.inflate(LayoutInflater.from(context))
    }

    constructor(context: Context): super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, style: Int): super(context, attrs, style)  {
        initView()
    }

    private fun initView() {
        bannerLayout.apply {
            initBanner()
            addView(this.root, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 110.dp))
        }
    }

    /**
     * 初始化Banner
     */
    private fun initBanner() {
        bannerLayout.banner.apply {
            indicator = CircleIndicator(context)
            // 设置Banner的适配器
            setAdapter(bannerAdapter, true)

            // 设置Banner的点击事件
            setOnBannerListener { _, position ->
                BannerHelper.onClick(bannerAdapter.getData(position))
                clickAction(position)
            }
        }
    }

    /**
     * 为Banner填充数据
     */
    fun setData(dataList: List<CommonBanner>?,clickAction: (position: Int) -> Unit = {}) {
        bannerLayout.banner.adapter.setDatas(dataList)
        this.clickAction = clickAction
    }
}

