package com.jmbon.minitools.advisory.adapter

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.glide.GlideUtil
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.extention.getDrawable
import com.jmbon.middleware.extention.getLifecycleOwner
import com.jmbon.middleware.extention.loadUrl
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.toWxMiniProgram
import com.jmbon.minitools.R
import com.jmbon.minitools.advisory.bean.AdvisoryItemForm
import com.jmbon.minitools.advisory.bean.AdvisoryItemForm.Companion.ITEM_TYPE_ROBOT
import com.jmbon.minitools.advisory.bean.AdvisoryItemForm.Companion.ITEM_TYPE_USER
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.coroutines.launch

/******************************************************************************
 * Description: 咨询对话Adapter
 *
 * Author: jhg
 *
 * Date: 2023/5/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class AdvisoryChatAdapter : BaseMultiItemQuickAdapter<AdvisoryItemForm, BaseViewHolder>() {

    /**
     * 文字动画
     */
    private var textAnimator: ValueAnimator? = null

    /**
     * 进度条动画
     */
    private var progressAnimator: ValueAnimator? = null

    init {
        addItemType(ITEM_TYPE_ROBOT, R.layout.item_advisory_ask_layout)
        addItemType(ITEM_TYPE_USER, R.layout.item_advisory_answer_layout)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        clearAnimation()
    }

    override fun convert(holder: BaseViewHolder, item: AdvisoryItemForm) {
        holder.setText(R.id.tv_content, item.content)
        holder.setGone(R.id.tv_content, item.isFakeData)
        holder.getView<ShapeableImageView>(R.id.iv_icon).shapeAppearanceModel =
            ShapeAppearanceModel().withCornerSize(22.dp.toFloat())
        if (item.itemType == ITEM_TYPE_ROBOT) {
            if (item.isFakeData) {
                holder.getView<ProgressBar>(R.id.pb_progress).setProgressStyle()
                holder.setVisible(R.id.cl_replying, true)
                setAnimation(
                    holder.getView(R.id.tv_tip),
                    holder.getView(R.id.pb_progress),
                    holder.getView(R.id.tv_progress)
                )
            } else {
                holder.setGone(R.id.cl_replying, true)
                clearAnimation()
            }
            holder.setImageResource(R.id.iv_icon, R.drawable.advisory_robot_icon)
            holder.getView<View>(R.id.tv_add_wechat).onClick {
                toWxMiniProgram(ActivityUtils.getTopActivity(),10)
//                    ARouter.getInstance().navigation(IMiniToolProvider::class.java)
//                        .showAddFertilityCounselorDialog(context)
            }
        } else {
            holder.setGone(R.id.iv_report, true)
            holder.setGone(R.id.tv_count, true)
            if (!item.avatar.isNullOrBlank()) {
                holder.getView<ShapeableImageView>(R.id.iv_icon).loadUrl(item.avatar, radius = 22.dp)
            } else {
                GlideUtil.getInstance().loadUrl(
                    holder.getView(R.id.iv_icon),
                    Constant.userInfo.avatarFile,
                    R.drawable.icon_circle_placeholder
                )
            }
        }
    }

    /**
     * 设置回复中的动画
     */
    private fun setAnimation(tipText: TextView, progressBar: ProgressBar, valueText: TextView) {
        if (textAnimator?.isRunning == true) {
            return
        }

        textAnimator = ValueAnimator.ofInt(4).apply {
            repeatCount = -1
            duration = 2400
            addUpdateListener {
                tipText.text = "小助手正在回复${".".repeat(it.animatedValue as Int)}"
            }
            start()
        }

        /**
         * 动画流程:
         * 1. 1秒内达到60-85%;
         * 2. 随后2秒内达到97%;
         * 3. 最后1秒钟到达100%;
         */
        context.getLifecycleOwner()?.lifecycleScope?.launch {
            val endValue = (60..85).random()
            progressBar.progress = 0
            startStepAnimation(
                progressBar,
                valueText,
                endValue,
                1000,
                AccelerateDecelerateInterpolator()
            ) {
                startStepAnimation(progressBar, valueText, 97, 2000, DecelerateInterpolator()) {
                    startStepAnimation(
                        progressBar,
                        valueText,
                        100,
                        1000,
                        DecelerateInterpolator()
                    ) {

                    }
                }
            }
        }
    }

    /**
     * 清除动画
     */
    private fun clearAnimation() {
        textAnimator?.cancel()
        textAnimator = null
        progressAnimator?.cancel()
        progressAnimator = null
    }

    private fun startStepAnimation(
        progressBar: ProgressBar,
        valueText: TextView,
        endValue: Int,
        duration: Long,
        interpolator: TimeInterpolator,
        endAction: () -> Unit
    ) {
        progressAnimator = ValueAnimator.ofInt(progressBar.progress, endValue).apply {
            this.duration = duration
            this.interpolator = interpolator
            addUpdateListener {
                val value = it.animatedValue as Int
                progressBar.progress = value
                valueText.text = "${value}%"
                if (value == endValue) {
                    progressAnimator?.cancel()
                    progressAnimator = null
                    endAction()
                }
            }
            start()
        }
    }

    private fun ProgressBar.setProgressStyle() {
        background = getDrawable(backgroundColor = R.color.colorF5F5F5.toColorInt(), radius = 5.5.dp)
        val tempProgressDrawable = getDrawable(gradientColor = intArrayOf(
                Color.parseColor("#E02020"),
                Color.parseColor("#FA6400"),
                Color.parseColor("#F7B500"),
                Color.parseColor("#6DD400"),
                Color.parseColor("#0091FF"),
                Color.parseColor("#6236FF"),
                Color.parseColor("#B620E0"),
            ),
            orientation = GradientDrawable.Orientation.LEFT_RIGHT,
            radius = 5.5.dp
        )
        val clipDrawable = ClipDrawable(tempProgressDrawable, Gravity.START, ClipDrawable.HORIZONTAL)
        progressDrawable = clipDrawable
    }
}