package com.apkdv.mvvmfast.view.state

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

/**
 * @ProjectName: MultiStatePage
 */
object MultiStatePage {

    /**
     * 实现原理
     * 1.根据目标view在父view中的位置索引,移除原目标view,
     * 2.将MultiStateContainer添加到原view的索引处
     * 3.MultiStateContainer 的 layoutParams 是原目标View的 layoutParams
     *
     * @param targetView view
     * @param retry 重试方法
     * @param offset 偏移
     * @param orientation 偏移方向  [Gravity.TOP]、[Gravity.BOTTOM]、[Gravity.LEFT]、[Gravity.RIGHT]
     */
    @JvmStatic
    @JvmOverloads
    fun bindMultiState(
        targetView: View, retry: () -> Unit = {},
        offset: Int = 0, orientation: Int = Gravity.BOTTOM
    ): MultiStateContainer {
        val parent = targetView.parent as ViewGroup?
        var targetViewIndex = 0
        val multiStateContainer = MultiStateContainer(targetView.context, targetView,retry, offset, orientation)
        parent?.let { targetViewParent ->
            for (i in 0 until targetViewParent.childCount) {
                if (targetViewParent.getChildAt(i) == targetView) {
                    targetViewIndex = i
                    break
                }
            }
            targetViewParent.removeView(targetView)
            targetViewParent.addView(multiStateContainer, targetViewIndex, targetView.layoutParams)
        }
        multiStateContainer.initialization()
        return multiStateContainer
    }

    /**
     * 实现原理
     * 1. android.R.id.content 是Activity setContentView 内容的父view
     * 2. 在这个view中移除原本要添加的contentView
     * 3. 将MultiStateContainer设置为 content的子View  MultiStateContainer中持有原有的Activity setContentView
     */
    @JvmStatic
    @JvmOverloads
    fun bindMultiState(
        activity: Activity, retry: () -> Unit = {},
        offset: Int = 0, orientation: Int = Gravity.BOTTOM
    ): MultiStateContainer {
        val targetView = activity.findViewById<ViewGroup>(android.R.id.content)
        val targetViewIndex = 0
        val oldContent: View = targetView.getChildAt(targetViewIndex)
        targetView.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val multiStateContainer = MultiStateContainer(oldContent.context, oldContent,retry, offset, orientation)
        targetView.addView(multiStateContainer, targetViewIndex, oldLayoutParams)
        multiStateContainer.initialization()
        return multiStateContainer
    }

    var config: MultiStateConfig = MultiStateConfig()

    @JvmStatic
    fun config(config: MultiStateConfig): MultiStatePage {
        this.config = config
        return this
    }

}