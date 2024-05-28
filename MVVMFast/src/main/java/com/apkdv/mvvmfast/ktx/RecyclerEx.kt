package com.apkdv.mvvmfast.ktx

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL

//<editor-fold desc="DslAdapter相关">

/**清空原有的[ItemDecoration]*/
fun RecyclerView.clearItemDecoration() {
    for (i in itemDecorationCount - 1 downTo 0) {
        removeItemDecorationAt(i)
    }
}


//</editor-fold desc="DslAdapter相关">

//<editor-fold desc="基础">

/** 通过[V] [H] [GV2] [GH3] [SV2] [SV3] 方式, 设置 [LayoutManager] */
fun RecyclerView.resetLayoutManager(match: String) {
    var layoutManager: RecyclerView.LayoutManager? = null
    var spanCount = 1
    var orientation = RecyclerView.VERTICAL

    if (TextUtils.isEmpty(match) || "V".equals(match, ignoreCase = true)) {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
    } else {
        //线性布局管理器
        if ("H".equals(match, ignoreCase = true)) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        } else { //读取其他配置信息(数量和方向)
            val type = match.substring(0, 1)
            if (match.length >= 3) {
                try {
                    spanCount = Integer.valueOf(match.substring(2)) //数量
                } catch (e: Exception) {
                }
            }
            if (match.length >= 2) {
                if ("H".equals(match.substring(1, 2), ignoreCase = true)) {
                    orientation = RecyclerView.HORIZONTAL //方向
                }
            }
            //交错布局管理器
            if ("S".equals(type, ignoreCase = true)) {
                layoutManager =
                    StaggeredGridLayoutManager(
                        spanCount,
                        orientation
                    )
            } else if ("G".equals(type, ignoreCase = true)) {
                layoutManager =
                    GridLayoutManager(
                        context,
                        spanCount,
                        orientation,
                        false
                    )
            }
        }
    }

    if (layoutManager is LinearLayoutManager) {
        layoutManager.recycleChildrenOnDetach = true
    }
    this.layoutManager = layoutManager
}

/**
 * 取消RecyclerView的默认动画
 * */
fun RecyclerView.noItemAnim(animator: RecyclerView.ItemAnimator? = null) {
    itemAnimator = animator
}

/**
 * 取消默认的change动画
 * */
fun RecyclerView.noItemChangeAnim(no: Boolean = true) {
    if (itemAnimator == null) {
        itemAnimator = DefaultItemAnimator().apply {
            supportsChangeAnimations = !no
        }
    } else if (itemAnimator is SimpleItemAnimator) {
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = !no
    }
}

//</editor-fold desc="基础">


/**
 * 设置视图的宽高
 * */
fun View.setWidthHeight(width: Int, height: Int) {
    val params = layoutParams
    params.width = width
    params.height = height
    layoutParams = params
}

/**快速创建网格布局*/
fun gridLayout(
    context: Context,
    spanCount: Int = 4,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
): GridLayoutManager {
    return GridLayoutManager(
        context,
        spanCount,
        orientation,
        reverseLayout
    )
}


fun View.fullSpan(full: Boolean = true) {
    val layoutParams = layoutParams
    if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
        if (full != layoutParams.isFullSpan) {
            layoutParams.isFullSpan = true
            this.layoutParams = layoutParams
        }
    }
}