package com.tubewiki.home.activity.article.details


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.adapter.ArticleReferenceItemAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.*
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.common.CommonBusinessHelper
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.ArticleReferenceFooterItemLayoutBinding
import com.jmbon.middleware.dialog.FeedbackDialog
import com.jmbon.middleware.extention.setStateBackground
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.utils.AnimatorUtils.startLightAnimation
import com.jmbon.middleware.valid.action.Action
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.jmbon.widget.html.X5WebView
import com.lxj.xpopup.XPopup
import com.qmuiteam.qmui.kotlin.onClick
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.tubewiki.home.R
import com.tubewiki.home.activity.article.viewmodel.ArticleDetailViewModel
import com.tubewiki.home.adapter.ArticleCircleAdapter
import com.tubewiki.home.databinding.ActivityArticleDetailsContetnBinding
import com.umeng.commonsdk.internal.crash.UMCrashManager
import kotlinx.coroutines.flow.onCompletion
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs


/**
 * 文章详情
 */
@Route(path = "/home/article/details")
class ArticleDetailsActivity :
    ViewModelActivity<ArticleDetailViewModel, ActivityArticleDetailsContetnBinding>(),
    NetworkUtils.OnNetworkStatusChangedListener {

    private var x5WebView: X5WebView? = null

    var webScrollY = 0

    // 文章 ID
    @Autowired(name = TagConstant.ARTICLE_ID)
    @JvmField
    var articleId = 0

    private val articleCircleAdapter by lazy { ArticleCircleAdapter() }

    override fun beforeViewInit() {
        super.beforeViewInit()
        supportActionBar?.hide()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    @SuppressLint("MissingPermission")
    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .navigationBarColor(R.color.white)
            .statusBarDarkFont(true)
            .init()
        initTitle()
        initPageState()
        initContentView()
        initListener()
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.collectStatus.observe(this) {
            if (it) {
                "收藏成功"
            } else {
                "取消收藏"
            }.showToast()
            binding.ivCollect.isSelected = it
        }
    }

    private fun initListener() {
        ClickUtils.applySingleDebouncing(binding.ivCollect) {
            Action {
                viewModel.collectArticle(articleId, !binding.ivCollect.isSelected)
            }.logInToIntercept()
        }

        ClickUtils.applySingleDebouncing(binding.content.rlFeedError) {
            Action {
                showFeedErrorDialog()
            }.logInToIntercept()
        }

        articleCircleAdapter.setOnItemClickListener { adapter, view, position ->
            var data = articleCircleAdapter.getItem(position)
            BannerHelper.onClick(
                CommonBanner(
                    item_type = data.item_type , identity = data.identity
                )
            )
        }

        NetworkUtils.registerNetworkStatusChangedListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initContentView() {
        x5WebView = X5WebView(this)
        binding.content.scrollerLayout.addView(x5WebView, 1)
        x5WebView!!.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        x5WebView!!.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        x5WebView?.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(p0: WebView?, p1: String?) {
                    super.onPageFinished(p0, p1)
                    if (!this@ArticleDetailsActivity.isFinishing) {
                        showContentState()
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        H5ArouterUtils.urlProcessing(it)
                    }
                    return true
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    request?.url.toString().let {
                        H5ArouterUtils.urlProcessing(it)
                    }
                    return true
                }
            }
        }
        // binding.content.scrollerLayout.setSmoothScrollRate(300)
        initRecycleView()

        binding.content.scrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->

            var rate = abs(scrollY).toFloat() / 92f.dp()
            webScrollY = scrollY - x5WebView?.top!!
            if (scrollY < 100) {
                rate = 0f
            }
            binding.textName.alpha = rate
            CommonBusinessHelper.showWechatDialog(
                scrollY,
                Constant.enterArticleDetailCount,
                viewModel.firstShowFlow.value
            ) {
                viewModel.firstShowFlow.value = false
            }
        }
    }

    @JavascriptInterface
    fun resize(height: Float) {
        ThreadUtils.runOnUiThread {
            val layoutParams: ViewGroup.LayoutParams = x5WebView!!.layoutParams
            layoutParams.height = (height * ScreenUtils.getScreenDensity()).toInt()
            x5WebView!!.layoutParams = layoutParams
            binding.content.scrollerLayout.checkLayoutChange()
        }
    }

    var linerMax = 0
    var linerMin = 0
    private fun initArticleDetails(it: TubeArticleDetail?) {
        if (it == null) {
            return
        }

        initTopicLayout(it.article_detail)
        binding.content.apply {
            Constant.addArticleDetailCount()
            textTitle.text = it.article_detail?.title
            loadH5(it.article_detail?.content ?: "")
            binding.ivCollect.isSelected = it.collect_status == 1
            // 引用
            if (it.article_detail?.reference.isNotNullEmpty()) {
                showMore.visible()
                //引用文件
                val adapter = ArticleReferenceItemAdapter()
                rvReference.init(adapter)
                // 取第一个
                val oneList = it.article_detail?.reference?.getOrNull(0)?.run { mutableListOf(this) }
                    ?: run { mutableListOf() }
                adapter.setList(oneList)
                // 由内容则隐藏
                showMore.post {
                    linerMin = showMore.height
                    var view = ArticleReferenceFooterItemLayoutBinding.inflate(
                        LayoutInflater.from(this@ArticleDetailsActivity)
                    ).root
                    adapter.addFooterView(view)
                    adapter.setList(it.article_detail?.reference)
                    showMore.post {
                        linerMax = showMore.height
                        showMore.layoutParams?.height = linerMin
                    }
                }

                // ture 收起状态
                textShowMore.setOnSingleClickListener({ _it ->
                    textShowMore.text = if (textShowMore.isChecked) "展开" else "收起"
                    textShowMore.isChecked = !textShowMore.isChecked

                    val valueAnimator = ValueAnimator.ofInt(
                        if (textShowMore.isChecked) linerMin else linerMax,
                        if (textShowMore.isChecked) linerMax else linerMin
                    ).setDuration(250)
                    valueAnimator.addUpdateListener { animation ->
                        binding.content.showMore.layoutParams.height =
                            animation.animatedValue as Int
                        binding.content.showMore.requestLayout()
                    }
                    valueAnimator.addListener(onStart = { _ ->
                        if (textShowMore.isChecked) {
                            adapter.setList(it.article_detail?.reference)
                            llReferenceMedia.showDividers = LinearLayout.SHOW_DIVIDER_END
                        }
                    }, onEnd = { _ ->
                        if (!textShowMore.isChecked) {
                            adapter.setNewInstance(oneList)
                            llReferenceMedia.showDividers = LinearLayout.SHOW_DIVIDER_NONE
                        }
                    })
                    valueAnimator.start()
                })
            } else {
                showMore.gone()
            }

            // 您可能还想看
            if (it.tags.isNotNullEmpty()) {
                it.tags?.forEach { item ->
                    binding.content.ryRelatedSearch.addView(buildHotBtn(item.id, item.tag))
                }
            } else {
                binding.content.llWantRead.gone()
            }

            // 群聊
            if (it.circles.isNotNullEmpty()) {
                articleCircleAdapter.setList(it.circles)
            } else {
                binding.content.llGroupChat.gone()
            }
        }
    }

    private fun initTopicLayout(articleInfo: ArticleInfoBean?) {
        if (articleInfo == null) {
            binding.content.clOperate.gone()
            return
        }
        
        binding.content.apply {
            clTubeTest.isVisible = articleInfo.left_button != null
            tvCustomScheme.isVisible = articleInfo.right_button != null
            clOperate.isVisible = clTubeTest.isVisible && tvCustomScheme.isVisible
            tvTubeTest.setTextWhenNotEmpty(articleInfo.left_button?.title)
            tvCustomScheme.setTextWhenNotEmpty(articleInfo.right_button?.title)
            tvLabel.setTextWhenNotEmpty(articleInfo.right_button?.subscript)
            clLabel.isVisible = !articleInfo.right_button?.subscript.isNullOrBlank()
            if (clLabel.isVisible) {
                tvAnim.startLightAnimation()
            }
            clTubeTest.setOnClickListener {
                articleInfo.left_button?.let {
                    BannerHelper.onClick(
                        CommonBanner(
                            item_type = it.item_type,
                            identity = it.identity
                        )
                    )
                } ?: kotlin.run {
                    toWxMiniProgram(
                        this@ArticleDetailsActivity,
                        type = "invite_group",
                        groupName = tvTubeTest.text.toString()
                    )
                }
            }
            tvCustomScheme.onClick {
                articleInfo.right_button?.let {
                    BannerHelper.onClick(
                        CommonBanner(
                            item_type = it.item_type,
                            identity = it.identity
                        )
                    )
                }
            }
        }
    }

    private fun buildHotBtn(articleId: Int, str: String): TextView {
        val btn = TextView(this)
        btn.text = str
        btn.setTextColor(ColorUtils.getColor(R.color.color_262626))
        btn.textSize = 14f
        btn.setBackgroundResource(R.drawable.shape_hot_search_bg)
        btn.setOnClickListener {
            ArouterUtils.toArticleDetailsActivity(articleId)
        }
        return btn
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun loadH5(
        h5Str: String,
    ) {
        x5WebView?.loadDataWithBaseURL(
            BuildConfig.H5_URL,
            ArticleHtmlTools.createHtmlType1(h5Str),
            "text/html",
            "utf-8",
            null
        )

        var startx = 0f
        var starty = 0f
        var offsetx = 0f
        var offsety = 0f
        x5WebView?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    startx = event.x
                    starty = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    offsetx = abs(event.x - startx)
                    offsety = abs(event.y - starty)
                    if (offsetx > offsety) {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }

                else -> {}
            }
            false
        }
        showContentState()
    }

    private fun initTitle() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivCollect.setStateBackground(
            this,
            falseBackgroundResId = R.drawable.home_collect_normal_icon,
            trueBackgroundResId = R.drawable.home_collected_icon
        )
    }

    override fun getData() {
        if (articleId > 0) {
            started {
                viewModel.getDetails(articleId).netCatch {
                    if (code.toInt() == 404) {
                        showContentState()
                        //文章不存在
                        binding.content.emptyHint.visible()
                    } else {
                        showErrorState()
                    }
                }.onCompletion {

                }.next {
                    initArticleDetails(data1)
                }
            }
            viewModel.getPopupImage()
        }
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getData()
    }

    private fun initRecycleView() {
        binding.content.ryRelatedReading.init(
            articleCircleAdapter
        )
    }


    override fun onDestroy() {
        KotlinBus.unregister(this.hashCode().toString())
        EventBus.getDefault().unregister(this)
        try {
            binding.content.scrollerLayout.removeView(x5WebView)
            x5WebView?.destroy()
        } catch (e: Exception) {
            UMCrashManager.reportCrash(Utils.getApp(), e)
        } finally {
            super.onDestroy()
        }

        NetworkUtils.unregisterNetworkStatusChangedListener(this)
    }


    override fun onResume() {
        super.onResume()
        time = System.currentTimeMillis()
    }

    /**
     * 纠错dialog
     */
    private fun showFeedErrorDialog() {
        val reportListData = arrayListOf(
            CustomDialogTypeBean(
                resources.getString(R.string.find_error), 1
            ),
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.content_error), 2
            ),
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.miss_part_content), 2
            ),
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.currency_cancle), 3
            ) as MultiItemEntity,
        )
        XPopup.Builder(this).isDestroyOnDismiss(true).dismissOnTouchOutside(true)
            .dismissOnBackPressed(true).autoDismiss(true).enableDrag(false)
            .asCustom(CustomListBottomDialog(this, reportListData) {
                //举报回答
                if (it == 1 || it == 2) {
                    ARouter.getInstance().build("/middleware/activity/feed_error").withInt(
                        TagConstant.PARAMS, it
                    ).withInt(
                        TagConstant.TYPE, 1
                    ).withInt(
                        TagConstant.ITEM_ID, articleId
                    ).withString(
                        TagConstant.PARAMS2, binding.content.textTitle.text.toString()
                    ).navigation()
                }

            }).show()
    }

    /**
     * 反馈 dialog
     */
    private fun showFeedBackDialog() {
        XPopup.Builder(this).isDestroyOnDismiss(true).dismissOnTouchOutside(true)
            .dismissOnBackPressed(true).isDestroyOnDismiss(true).enableDrag(false)
            .asCustom(FeedbackDialog(this) { type, dialog ->
                started {
                    viewModel.feedback(articleId, type).netCatch {
                        message.showToast()
                        dialog.dismiss()
                    }.next {
                        "反馈成功".showToast()
                        dialog.dismiss()
                    }

                }
            }).show()
    }


    override fun finish() {
        overridePendingTransition(
            R.anim.activity_bottom_out, R.anim.activity_background
        )
        super.finish()
    }

    override fun onDisconnected() {
    }

    override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        getData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {
        if (event.login) {
            getData()
        }
    }

    private var time = 0L
}