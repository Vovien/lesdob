package com.jmbon.widget.dialog

import android.content.Context
import android.widget.TextView
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.widget.R
import com.jmbon.widget.interpolator.EaseCubicInterpolator
import com.jmbon.widget.interpolator.FastSlowEaseCubicInterpolator
import com.lxj.xpopup.animator.PopupAnimator
import com.lxj.xpopup.core.PositionPopupView

/**
 *
 * @author : leimg
 * time   : 2021/5/22
 * desc   :数据更新提示 -仿toast效果的dialog
 * version: 1.0
 */
class ToastUpdateDataDialog(context: Context, var updateNum: Int) : PositionPopupView(context) {


    override fun getImplLayoutId(): Int {
        return R.layout.toast_update_num_dialog_layout
    }

    override fun initPopupContent() {
        super.initPopupContent()
        findViewById<TextView>(R.id.tv_title).text = "为您更新了${updateNum}条数据"
    }
}


class MyToastPopupAnimator(
    offy: Float = 118f - SizeUtils.px2dp(
        StatusBarCompat.getStatusBarHeight(
            Utils.getApp()
        ).toFloat()
    )
) : PopupAnimator() {

    //起始y轴偏移量
    var offY = SizeUtils.dp2px(offy).toFloat()

    //动画y轴执行偏移量
    var transOffY = offY + SizeUtils.dp2px(40f)

    override fun initAnimator() {
        targetView.alpha = 0.32f
        targetView.translationY = offY
    }

    override fun animateShow() {
        //FastOutSlowInInterpolator
        targetView.animate().translationY(transOffY).alpha(1f)
            .setInterpolator(FastSlowEaseCubicInterpolator()).setDuration(
                350
            )
            .start()
    }

    override fun animateDismiss() {
        targetView.animate().alpha(0f)
            .setInterpolator(EaseCubicInterpolator()).setDuration(
                200
            )
            .start()

    }


}