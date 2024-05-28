package com.jmbon.middleware.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.utils.SizeUtil
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.ToastHasMessageDetectorListener
import com.jmbon.middleware.utils.dp
import com.jmbon.widget.interpolator.FastSlowEaseCubicInterpolator
import com.lxj.xpopup.core.PositionPopupView

/**
 *
 * @author : leimg
 * time   : 2021/5/22
 * desc   :数据更新提示 -仿toast效果的dialog
 * version: 1.0
 */
@SuppressLint("ViewConstructor")
class ToastHasMessageDialog(
    context: Context,
    val type: ToastType,
    val itemId: Int = 0,
    val msgContent: String = "",
    val title: String = "",
    val questionId: Int = 0
) :
    PositionPopupView(context) {


    override fun getImplLayoutId(): Int {
        return R.layout.toast_has_message_dialog_layout
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        val title = findViewById<TextView>(R.id.textTitle)
        val textEnd = findViewById<TextView>(R.id.textEnd)
        val content = findViewById<TextView>(R.id.textMsg)
        val root = findViewById<View>(R.id.rootView)
        val params = popupContentView.layoutParams as LayoutParams
        params.gravity = Gravity.TOP
        val detector = GestureDetector(context, ToastHasMessageDetectorListener {
            popupContentView.animate().translationY(SizeUtils.dp2px(24f).toFloat()).alpha(0f)
                .setInterpolator(FastSlowEaseCubicInterpolator()).setDuration(
                    350
                )
                .start()
            popupContentView.postDelayed({ dismiss() }, 350)
        })
        root.setOnTouchListener { _, event ->
            return@setOnTouchListener detector.onTouchEvent(event)
        }
        when (type) {
            ToastType.CIRCLE -> {
                if (this@ToastHasMessageDialog.title.isEmpty()) {
                    title.text = "来自圈子的消息"
                    textEnd.text = ""
                } else {
                    title.text = "来自[${this@ToastHasMessageDialog.title}"
                    textEnd.text = "]圈子的消息"
                }

                content.text =
                    if (msgContent.isEmpty()) context.getString(R.string.someone_answered_your_question) else msgContent
                root.setOnClickListener {
                    ARouter.getInstance().build("/circle/ask/details")
                        .withInt(TagConstant.QUESTION_ID, itemId)
                        .withBoolean(TagConstant.NEED_REFRESH, true)
                        .navigation()
                    MobAnalysisUtils.mInstance.sendEvent("Event_TopPush_AskHelp")

                    dismiss()
                }
            }
            ToastType.QUESTIONS -> {
                title.text = "您的问题“${msgContent}”"
                textEnd.text = "”"

                content.text = context.getString(R.string.has_new_answer)
                root.setOnClickListener {
                    ARouter.getInstance().build("/question/activity/answer_detail")
                        .withInt("question_id", questionId)
                        .withInt("answer_id", itemId)
                        .navigation()
                    MobAnalysisUtils.mInstance.sendEvent("Event_TopPush_QA")

                    dismiss()
                }
            }
        }
        root.postDelayed({
            dismiss()
        }, 4550)
    }

    override fun getPopupWidth(): Int {
        return SizeUtil.getWidth() - SizeUtils.dp2px(20f)
    }

}

enum class ToastType {
    CIRCLE,
    QUESTIONS,
}