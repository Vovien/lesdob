package com.jmbon.middleware.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/******************************************************************************
 * Description: 给 GridLayoutManager or StaggeredGridLayoutManager 设置间距，可设置去除首尾间距个数，默认尾部1
 *
 * Author: jhg
 *
 * Date: 2023/6/21
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class GridSpacingItemDecoration @JvmOverloads constructor(
    /**
     * Item的间距, 水平和垂直方向都一样
     */
    private val spacing: Int = 0,
    /**
     * Item的水平间距
     */
    private val horizontalSpacing: Int = spacing,
    /**
     * Item的垂直间距
     */
    private val verticalSpacing: Int = spacing,
    /**
     * 边框的间距
     */
    private val edgeSpacing: Int = 0,
    /**
     * 起始位置不需要设置间距的Item数
     */
    private val skipStartCount: Int = 0,
    /**
     * 结束位置不需要设置间距的Item数
     */
    private val skipEndCount: Int = 0,
) :
    RecyclerView.ItemDecoration() {
    /**
     * 每行个数
     */
    private var mSpanCount = 0

    private var fullPosition = -1

    /**
     * 横向还是纵向
     */
    private var mOrientation = RecyclerView.VERTICAL

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lastPosition = state.itemCount - 1
        var position = parent.getChildAdapterPosition(view)
        if (skipStartCount <= position && position <= lastPosition - skipEndCount) {
            // 行，如果是横向就是列
            var spanGroupIndex = -1
            // 列，如果是横向就是行
            var column = 0
            // 瀑布流是否占满一行
            var fullSpan = false
            val layoutManager = parent.layoutManager
            if (layoutManager is GridLayoutManager) {
                val gridLayoutManager = layoutManager
                val spanSizeLookup = gridLayoutManager.spanSizeLookup
                val spanCount = gridLayoutManager.spanCount
                // 当前position的spanSize
                val spanSize = spanSizeLookup.getSpanSize(position)
                // 横向还是纵向
                mOrientation = gridLayoutManager.orientation
                // 一行几个，如果是横向就是一列几个
                mSpanCount = spanCount / spanSize
                // =0 表示是最左边 0 2 4
                val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
                // 列
                column = spanIndex / spanSize
                // 行 减去skipStartCount,得到从0开始的行
                spanGroupIndex =
                    spanSizeLookup.getSpanGroupIndex(position, spanCount) - skipStartCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                // 瀑布流获取列方式不一样
                val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                // 横向还是纵向
                mOrientation = layoutManager.orientation
                // 列
                column = params.spanIndex
                // 是否是全一行
                fullSpan = params.isFullSpan
                mSpanCount = layoutManager.spanCount
            }
            if (mSpanCount <= 0) {
                mSpanCount = 1
            }

            // 减掉不设置间距的position,得到从0开始的position
            position = position - skipStartCount
            if (edgeSpacing > 0) {
                includeEdge(outRect, column, fullSpan)
            } else {
                /*
                 *示例：
                 * spacing = 10 ；spanCount = 3
                 * --------0--------
                 * 0   3+7   6+4    0
                 * -------10--------
                 * 0   3+7   6+4    0
                 * --------0--------
                 */
                unIncludeEdge(outRect, position, spanGroupIndex, column, fullSpan)
            }
        }
    }

    private fun includeEdge(
        outRect: Rect,
        column: Int,
        fullSpan: Boolean
    ) {
        if (fullSpan) {
            outRect.left = 0
            outRect.right = 0
        } else {
            if (mOrientation == RecyclerView.VERTICAL) {
                when (column) {
                    0 -> {
                        outRect.left = edgeSpacing
                        outRect.right = horizontalSpacing / 2
                    }
                    mSpanCount - 1 -> {
                        outRect.left = horizontalSpacing / 2
                        outRect.right = edgeSpacing
                    }
                    else -> {
                        outRect.left = horizontalSpacing / 2
                        outRect.right = horizontalSpacing / 2
                    }
                }
                outRect.bottom = verticalSpacing
            } else {
                when (column) {
                    0 -> {
                        outRect.top = edgeSpacing
                        outRect.bottom = horizontalSpacing / 2
                    }
                    mSpanCount - 1 -> {
                        outRect.top = horizontalSpacing / 2
                        outRect.bottom = edgeSpacing
                    }
                    else -> {
                        outRect.top = horizontalSpacing / 2
                        outRect.bottom = horizontalSpacing / 2
                    }
                }
                outRect.right = horizontalSpacing
            }
        }
    }

    private fun unIncludeEdge(
        outRect: Rect,
        position: Int,
        spanGroupIndex: Int,
        column: Int,
        fullSpan: Boolean
    ) {
        if (fullSpan) {
            outRect.left = 0
            outRect.right = 0
        } else {
            if (mOrientation == RecyclerView.VERTICAL) {
                outRect.left = column * horizontalSpacing / mSpanCount
                outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / mSpanCount
            } else {
                outRect.top = column * verticalSpacing / mSpanCount
                outRect.bottom = verticalSpacing - (column + 1) * verticalSpacing / mSpanCount
            }
        }
        if (spanGroupIndex > -1) {
            if (spanGroupIndex >= 1) {
                if (mOrientation == RecyclerView.VERTICAL) {
                    // 超过第0行都显示上间距
                    outRect.top = verticalSpacing
                } else {
                    // 超过第0列都显示左间距
                    outRect.left = horizontalSpacing
                }
            }
        } else {
            if (fullPosition == -1 && position < mSpanCount && fullSpan) {
                // 找到头部第一个整行的position
                fullPosition = position
            }
            // Stagger上间距显示规则
            val isStaggerShowTop =
                position >= mSpanCount || fullSpan && position != 0 || fullPosition != -1 && position != 0
            if (isStaggerShowTop) {
                if (mOrientation == RecyclerView.VERTICAL) {
                    // 超过第0行都显示上间距
                    outRect.top = verticalSpacing
                } else {
                    // 超过第0列都显示左间距
                    outRect.left = horizontalSpacing
                }
            }
        }
    }
}