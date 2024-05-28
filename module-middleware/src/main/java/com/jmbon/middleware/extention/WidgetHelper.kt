package com.jmbon.middleware.extention

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.viewpager2.widget.ViewPager2
import com.apkdv.mvvmfast.glide.GlideUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.jmbon.middleware.R
import com.jmbon.middleware.utils.BitmapUtils
import com.jmbon.middleware.utils.getRealUrl
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter


/******************************************************************************
 * Description: Android View组件的扩展方法
 *
 * Author: jhg
 *
 * Date: 2023/4/11
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
fun <T> SmartRefreshLayout.dealPage(
    currentPage: Int,
    dataList: List<T>?,
    adapter: BaseQuickAdapter<T, *>,
    emptyView: View? = View.inflate(context, R.layout.default_viewstatus_box_empty, null),
    emptyViewAction: (emptyIcon: ImageView, emptyText: TextView) -> Unit = { _, _ -> }
) {
    if (isRefreshing) {
        finishRefresh()
    }
    if (dataList.isNullOrEmpty()) {
        if (currentPage <= 1) {
            if (emptyView != null && !adapter.hasEmptyView()) {
                emptyViewAction.invoke(
                    emptyView.findViewById(R.id.state_layout_empty_img),
                    emptyView.findViewById(R.id.state_layout_empty_hint)
                )
                adapter.setEmptyView(emptyView)
            }
            adapter.setList(emptyList())
        } else {
            finishLoadMoreWithNoMoreData()
        }
    } else {
        if (isLoading) {
            finishLoadMore()
        }
        if (currentPage <= 1) {
            resetNoMoreData()
            //adapter.recyclerView?.scrollToPosition(0)
            adapter.setList(dataList)
        } else {
            adapter.addData(dataList)
        }
    }
}

fun ShapeableImageView.loadUrl(
    url: String?,
    radius: Int = 0,
    placeholder: Int = R.drawable.icon_topic_placeholder
) {
    if (radius > 0) {
        shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(radius.toFloat())
    }
    GlideUtil.getInstance().loadUrl(this, url.getRealUrl(), placeholder)
}

fun ShapeableImageView.loadUrl(
    url: String?,
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    bottomRightRadius: Int = 0,
    placeholder: Int = R.drawable.icon_topic_placeholder
) {
    shapeAppearanceModel = ShapeAppearanceModel.Builder()
        .setTopLeftCornerSize(topLeftRadius.toFloat())
        .setTopRightCornerSize(topRightRadius.toFloat())
        .setBottomLeftCornerSize(bottomLeftRadius.toFloat())
        .setBottomRightCornerSize(bottomRightRadius.toFloat())
        .build()
    GlideUtil.getInstance().loadUrl(this, url.getRealUrl(), placeholder)
}

fun ImageView.loadUrlWithSize(
    url: String?,
    width: Int = 0,
    height: Int = 0,
    placeholder: Int = R.drawable.icon_topic_placeholder
) {
    GlideUtil.getInstance().loadUrl(this, url.getRealUrl(width, height), placeholder)
}

fun ShapeableImageView.loadUrlWithSize(
    url: String?,
    width: Int = 0,
    height: Int = 0,
    radius: Int = 0,
    placeholder: Int = R.drawable.icon_topic_placeholder
) {
    if (radius > 0) {
        shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(radius.toFloat())
    }
    GlideUtil.getInstance().loadUrl(this, url.getRealUrl(width, height), placeholder)
}

fun ShapeableImageView.loadUrlWithSize(
    url: String?,
    width: Int = 0,
    height: Int = 0,
    topLeftRadius: Int = 0,
    topRightRadius: Int = 0,
    bottomLeftRadius: Int = 0,
    bottomRightRadius: Int = 0,
    placeholder: Int = R.drawable.icon_topic_placeholder
) {
    shapeAppearanceModel = ShapeAppearanceModel.Builder()
        .setTopLeftCornerSize(topLeftRadius.toFloat())
        .setTopRightCornerSize(topRightRadius.toFloat())
        .setBottomLeftCornerSize(bottomLeftRadius.toFloat())
        .setBottomRightCornerSize(bottomRightRadius.toFloat())
        .build()
    GlideUtil.getInstance().loadUrl(this, url.getRealUrl(width, height), placeholder)
}

/**
 * 下载并保存图片到相册
 */
inline fun String?.saveToAlbum(
    context: Context?,
    crossinline action: (result: Boolean) -> Unit = {}
) {
    if (isNullOrBlank() || context == null) {
        action(false)
        return
    }

    GlideUtil.getInstance().loadUrlSimpleTarget(this, object :
        GlideUtil.GlideUtilSimpleTarget {
        override fun onResourceReady(resource: Drawable?) {
            val bitmap = resource?.toBitmapOrNull() ?: return
            val result = BitmapUtils.saveBitmap(
                context,
                bitmap,
                "${context.cacheDir.path}/${System.currentTimeMillis()}.jpeg"
            )
            action(result)
        }

        override fun onLoadFailed() {
            action(false)
        }
    })
}

fun MagicIndicator.bindPager(vpContent: ViewPager2, titleAdapter: CommonNavigatorAdapter) {
    val commonNavigator = CommonNavigator(context)
    commonNavigator.adapter = titleAdapter
    navigator = commonNavigator
    vpContent.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindPager.onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindPager.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindPager.onPageSelected(position)
        }
    })
}