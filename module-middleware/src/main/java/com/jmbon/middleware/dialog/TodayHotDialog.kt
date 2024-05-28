package com.jmbon.middleware.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.utils.SizeUtil
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.lxj.xpopup.core.PositionPopupView

@SuppressLint("ViewConstructor")
class TodayHotDialog(
    context: Context,
    val type: HotType,
    val itemId: Int = 0,
    val title: String = "",
    val content: String = "",
) :
    PositionPopupView(context) {


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_toady_hot
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

        //首页，还有问答详情和文章详情间距不一样
        val bottom =
            if (ActivityUtils.getTopActivity().javaClass.name == "com.tubewiki.android.view.MainActivity"
                || ActivityUtils.getTopActivity().javaClass.name == "com.tubewiki.home.view.article.details.ArticleDetailsActivity"
                || ActivityUtils.getTopActivity().javaClass.name == "com.tubewiki.questions.activity.AnswerDetailActivity"
            ) 57f.dp() else 0
        val params = popupContentView.layoutParams as LayoutParams
        params.gravity = Gravity.BOTTOM
        params.bottomMargin = bottom + 4f.dp()
        val title = findViewById<TextView>(R.id.textTitle)
        val content = findViewById<TextView>(R.id.textContent)
        val root = findViewById<View>(R.id.rootView)
        title.text = this@TodayHotDialog.title
        content.text = this@TodayHotDialog.content

        root.setOnSingleClickListener({
            when (type) {
                HotType.QUESTIONS -> {
                    ARouter.getInstance().build("/question/activity/ask_detail")
                        .withInt(TagConstant.QUESTION_ID, itemId)
                        .navigation()

                }
                HotType.ARTICLE -> {
                    ArouterUtils.toArticleDetailsActivity(itemId)
                }
            }
            dismiss()
        })

        root.postDelayed({
            dismiss()
        }, 6000)
    }

    override fun getPopupWidth(): Int {
        return SizeUtil.getWidth() - SizeUtils.dp2px(20f)
    }
}

enum class HotType {
    ARTICLE,
    QUESTIONS,
}