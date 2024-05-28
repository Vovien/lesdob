package com.tubewiki.home.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.ColorCompute
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.adapter.ContentAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.dialog.WeChatDialog
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.AnimatorUtils.startLightAnimation
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.bindLoadUrlRadius
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.parseColor
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.utils.setTextWhenNotEmpty
import com.jmbon.middleware.utils.toWxMiniProgram
import com.qmuiteam.qmui.kotlin.onClick
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.tubewiki.home.R
import com.tubewiki.home.bean.Topic
import com.tubewiki.home.databinding.ActivityArticleColumnDetailBinding
import com.tubewiki.home.databinding.ItemArticleColumnDetailHeadLayoutBinding
import com.tubewiki.home.viewmodel.MainFragmentViewModel
import kotlin.math.abs


/**
 * 专题页面
 * @author MilkCoder
 * @date 2023/9/20 11:39
 * @copyright all rights reserved by ManTang
 */
@Route(path = "/home/activity/column_detail")
class ArticleColumnDetailActivity :
    ViewModelActivity<MainFragmentViewModel, ActivityArticleColumnDetailBinding>(),
    OnRefreshLoadMoreListener {

    @Autowired(name = TagConstant.TOPIC_ID)
    @JvmField
    var columnId = 0

    var page = 1

    private val columnDetailAdapter by lazy {
        ContentAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .navigationBarColor(R.color.black)
            .init()
        initPageState()

        val statusHeight =
            StatusBarCompat.getStatusBarHeight(this@ArticleColumnDetailActivity) + binding.titleBarLayout.paddingTop
        binding.titleBarLayout.setPadding(
            binding.titleBarLayout.paddingStart,
            statusHeight,
            binding.titleBarLayout.paddingEnd,
            binding.titleBarLayout.paddingBottom
        )


        binding.smartRefresh.setOnRefreshLoadMoreListener(this)

        (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false


        binding.recyclerView.init(columnDetailAdapter, dividerHeight = 1f, dividerColor = R.color.white06.toColorInt(), vertical = false)


        var maxOffset = 200f.dp()

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //已经滚动的距离，为0时表示已处于顶部。
                val offsetY = recyclerView.computeVerticalScrollOffset()
                Log.e("xyh", "dx: $dx,  dy: $dy,  offsetY: $offsetY")

                onChanged(offsetY.toFloat() / maxOffset)

                if (abs(offsetY) > 10) {
                    if (Constant.enterArticleColumnDetailCount == 1 && viewModel.firstShowFlow.value) {
                        val popupImage = viewModel.popupImageInfoLD.value?.pop_info
                        popupImage?.apply {
                            if (this.popupImg.isNotNullEmpty()) {
                                val dialog = WeChatDialog(ActivityUtils.getTopActivity(), this)
                                dialog.showDialog()
                                viewModel.firstShowFlow.value = false
                            }
                        }
                    }
                }
            }
        })

        binding.imageBack.setOnSingleClickListener({
            onBackPressed()
        })

        columnDetailAdapter.setOnItemClickListener { adapter, view, pos ->
            val data = columnDetailAdapter.getItem(pos)
            ArouterUtils.toArticleDetailsActivity(data.id)
        }

    }

    private fun onChanged(scale: Float) {
        binding.tvTitle.visibility = if (scale > 0.4) View.VISIBLE else View.INVISIBLE
        binding.tvTitle.alpha = scale

        binding.titleBarLayout.setBackgroundColor(
            ColorCompute.computeColor(
                Color.argb(0, 255, 255, 255),
                Color.BLACK,
                scale
            )
        )

        val btnColor = ColorCompute.computeColor(
            Color.argb(255, 255, 255, 255),
            Color.WHITE,
            scale
        )

        binding.imageBack.setColorFilter(btnColor)

    }

    override fun initData() {

        started {
            viewModel.topicList(columnId, page).netCatch {
                if (code.toInt() == 404) {
                    showContentState()
                    //专题不存在
                    binding.emptyHint.text = message
                    binding.emptyHint.visible()
                } else {
                    showErrorState()
                    message.showToast()
                }
            }.next {
                showContentState()
                if (page == 1) {
                    Constant.addArticleColumnDetailCount()
                    data2?.topic?.let {
                        initTopicLayout(it)
                    }
                }
                binding.smartRefresh.dealPage(page, data1.article_list, columnDetailAdapter) { _, emptyText ->
                    emptyText.text = "暂无相关文章"
                }
            }
        }

    }

    private fun initTopicLayout(headerInfo: Topic) {
        ItemArticleColumnDetailHeadLayoutBinding.inflate(layoutInflater).apply {
            tvTitle.text =
                headerInfo.customTitle.ifEmpty { headerInfo.topicName }
            binding.tvTitle.text = headerInfo.customTitle.ifEmpty { headerInfo.topicName }
            tvDesc.text =
                "${headerInfo.customDescription.ifEmpty { headerInfo.description }}"
            tvDesc.maxWidth = ScreenUtils.getScreenWidth() - 96.dp
            gpDesc.isVisible = !tvDesc.text.isNullOrBlank()
            ivCover.bindLoadUrlRadius(headerInfo.cover, urlBottomLeftRadius = 32)
            shapeFl.shapeBuilder?.setShapeCornersBottomLeftRadius(32.dp.toFloat())
                ?.setShapeSolidColor(ColorUtils.setAlphaComponent(parseColor(headerInfo.coverColor, Color.TRANSPARENT), 0.8f))
                ?.into(shapeFl)
            columnDetailAdapter.setHeaderView(root)
        }

        binding.apply {
            clTubeTest.isVisible = headerInfo.leftButton != null
            tvCustomScheme.isVisible = headerInfo.rightButton != null
            clOperate.isVisible = clTubeTest.isVisible && tvCustomScheme.isVisible
            tvTubeTest.setTextWhenNotEmpty(headerInfo.leftButton?.title)
            tvCustomScheme.setTextWhenNotEmpty(headerInfo.rightButton?.title)
            tvLabel.setTextWhenNotEmpty(headerInfo.rightButton?.label)
            clLabel.isVisible = !headerInfo.rightButton?.label.isNullOrBlank()
            if (clLabel.isVisible) {
                tvAnim.startLightAnimation()
            }
            binding.clTubeTest.setOnClickListener {
                headerInfo.leftButton?.let {
                    BannerHelper.onClick(
                        CommonBanner(
                            item_type = it.item_type,
                            identity = it.identity
                        )
                    )
                } ?: kotlin.run {
                    toWxMiniProgram(
                        this@ArticleColumnDetailActivity,
                        type = "invite_group",
                        groupName = binding.tvTubeTest.text.toString()
                    )
                }
            }
            tvCustomScheme.onClick {
                headerInfo.rightButton?.let {
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

    override fun getData() {
        if (Constant.enterArticleColumnDetailCount == 0) {
            viewModel.getPopupImage()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        initData()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        page = 1
        binding.smartRefresh.setEnableLoadMore(true)
        columnDetailAdapter.removeAllFooterView()
        initData()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}