package com.jmbon.widget.refresh

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.invisible
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.jmbon.widget.R

/**
 * PACKAGE_NAME: cn.com.enorth.easymakeapp.view.customsmartview
 * PROJECT_NAME:learnbaodi
 * 创建日期 :2019/10/11
 * 创建时间: 15/49
 * author  kunkun5love
 * 说明: smartRefrush中自定义的head 需要实现RefreshHeader
 */
class JmbonRefreshView : FrameLayout {
    private var mRefrushSmartHeadView: View? = null
    private var llRefresh: LinearLayout? = null
    private var tvRecommend: TextView? = null
    private var tvFinishTips: TextView? = null
    private var tvFooter: TextView? = null
    private var igRefreshFooter: ImageView? = null

    private var isSimpleTipsMode = false //简单提示模式
    private var isContinueStatus = false //继续浏览


    /**
     * 我们这边写好了  两个布局堃堃5love:
     * 刷新head  R.layout.layout_ptr_header
     *
     *
     * 堃堃5love:
     * 加载更多:   R.layout.layout_load_more
     *
     * @param context
     */
    constructor(context: Context) : super(context) {
        initRefreshView(context)
    }

    //  下面的都是RefreshHeader继承的方法  start
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initRefreshView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initRefreshView(context)
    }

    /**
     * 打气一个view,并进行初始化
     *
     * @param context
     */
    private fun initRefreshView(context: Context) {

        mRefrushSmartHeadView =
            LayoutInflater.from(context).inflate(R.layout.jmbon_refresh_layout, null)
        addView(mRefrushSmartHeadView)
        mRefrushSmartHeadView?.let {
//            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(600f));
//            it.layoutParams = layoutParams;
            llRefresh = it.findViewById(R.id.ll_refresh)
            tvFinishTips = it.findViewById(R.id.tv_answer_finish)
            tvRecommend = it.findViewById(R.id.tv_recommend)
            igRefreshFooter = it.findViewById(R.id.refresh_footer)
            tvFooter = it.findViewById(R.id.tv_ptr_footer)
        }
    }

    /**
     * 继续浏览内容状态
     */
    fun setContinueRecommendTipsStatus() {
        llRefresh?.invisible()
        tvFinishTips?.gone()
        tvRecommend?.visible()
        tvRecommend?.text = StringUtils.getString(R.string.continue_browser_curr_qa)
        tvRecommend?.typeface = Typeface.DEFAULT
        isContinueStatus = true
    }

    override fun setBackgroundColor(color: Int) {
        mRefrushSmartHeadView?.setBackgroundColor(color)
    }
    
    /**
     * 推荐内容状态
     */
    fun setRecommendTipsStatus() {
        llRefresh?.invisible()
        tvFinishTips?.gone()
        tvRecommend?.visible()
        tvFooter?.text = StringUtils.getString(R.string.pull_up_switch)
        tvRecommend?.text = StringUtils.getString(R.string.recommend_content)
        isSimpleTipsMode = true
        tvRecommend?.typeface = Typeface.DEFAULT
    }

    /**
     * 浏览文章结束状态
     */
    fun setFinishArticleTipsStatus() {
        isSimpleTipsMode = true
        llRefresh?.invisible()
        tvFinishTips?.visible()
        tvFinishTips?.text = StringUtils.getString(R.string.finish_article_content)
        tvRecommend?.visible()
        tvRecommend?.text = StringUtils.getString(R.string.next_recommend_content)
        tvFooter?.text = StringUtils.getString(R.string.pull_up_switch)
        tvRecommend?.typeface = Typeface.DEFAULT_BOLD
    }

    /**
     * 浏览问答结束状态
     */
    fun setFinishQaTipsStatus() {
        isSimpleTipsMode = true
        llRefresh?.invisible()
        tvFinishTips?.visible()
        tvFinishTips?.text = StringUtils.getString(R.string.finish_qa_content)
        tvRecommend?.visible()
        tvRecommend?.text = StringUtils.getString(R.string.next_recommend_content)
        tvFooter?.text = StringUtils.getString(R.string.pull_up_switch)
        tvRecommend?.typeface = Typeface.DEFAULT_BOLD
    }


    /**
     * 浏览问答结束状态
     */
    fun setFinishHideQaTipsStatus() {
        isSimpleTipsMode = true
        llRefresh?.invisible()
        tvFinishTips?.gone()
        tvFinishTips?.text = StringUtils.getString(R.string.finish_qa_content)
        tvRecommend?.visible()
        tvRecommend?.text = StringUtils.getString(R.string.next_recommend_content)
        tvFooter?.text = StringUtils.getString(R.string.pull_up_switch)
        tvRecommend?.typeface = Typeface.DEFAULT_BOLD
    }


    /**
     * 状态重置
     */
    fun resetStatus() {
        isSimpleTipsMode = false
        llRefresh?.visible()
        tvFooter?.text = StringUtils.getString(R.string.pull_up_switch_answer)
        igRefreshFooter?.animate()?.rotation(0f)
        tvFinishTips?.gone()
        tvRecommend?.gone()

    }

    /**
     * 状态重置
     */
    fun setNormalStatus() {
        isSimpleTipsMode = false
        llRefresh?.visible()
        tvFooter?.text = StringUtils.getString(R.string.pull_up_switch_answer)
        igRefreshFooter?.animate()?.rotation(0f)
        tvFinishTips?.gone()
        tvRecommend?.gone()
    }

    fun setSimpleTipsMode(isSimpleTipsMode: Boolean) {
        this.isSimpleTipsMode = isSimpleTipsMode

        tvFooter?.text =
            StringUtils.getString(if (isSimpleTipsMode) R.string.pull_up_switch else R.string.pull_up_switch_answer)
    }

    fun pullUpCanceled() {
        //上拉取消
        tvFooter?.text =
            StringUtils.getString(if (isSimpleTipsMode) R.string.pull_up_switch else R.string.pull_up_switch_answer)
        igRefreshFooter?.animate()?.rotation(0f)
    }

    fun releaseToLoad() {
        tvFooter?.let {
            if (!it.text.contains(StringUtils.getString(R.string.release_up))) {
                callVibrator(it)
                igRefreshFooter?.animate()?.rotation(180f)
            }
        }
        //上拉将要释放开始刷新
        tvFooter?.text =
            StringUtils.getString(if (isSimpleTipsMode) R.string.release_up else R.string.release_up_answer)
        //  igRefreshFooter?.animate()?.rotation(180f)
    }

    fun pullUpToLoad() {
        llRefresh?.visible()
        tvRecommend?.gone()
        tvFooter?.let {
            if (!it.text.contains(StringUtils.getString(R.string.pull_up_switch))) {
                igRefreshFooter?.animate()?.rotation(0f)
            }
        }
        //开始上拉
        tvFooter?.text =
            StringUtils.getString(if (isSimpleTipsMode) R.string.pull_up_switch else R.string.pull_up_switch_answer)
        // igRefreshFooter?.animate()?.rotation(0f)
        mRefrushSmartHeadView?.requestLayout()
    }

    fun pullUpResetToLoad() {
        if (isSimpleTipsMode || isContinueStatus) {
            tvRecommend?.visible()
            llRefresh?.invisible()
        }

    }

    private fun callVibrator(view: View) {
        view.performHapticFeedback(
            HapticFeedbackConstants.LONG_PRESS,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }
}