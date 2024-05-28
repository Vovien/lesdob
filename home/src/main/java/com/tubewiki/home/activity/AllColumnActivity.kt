package com.tubewiki.home.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.middleware.utils.UMEventKey
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.widget.BannerView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.tubewiki.home.R
import com.tubewiki.home.adapter.AllColumnAdapter
import com.tubewiki.home.databinding.ActivityAllColumnBinding
import com.tubewiki.home.viewmodel.MainFragmentViewModel


/**
 * 全部专栏
 */
@Route(path = "/home/activity/all_column")
class AllColumnActivity :
    ViewModelActivity<MainFragmentViewModel, ActivityAllColumnBinding>(),
    OnRefreshLoadMoreListener {

    private val bannerView by lazy {
        BannerView(this).apply {
            setPadding(8.dp, 0, 8.dp, 8.dp)
        }
    }

    private val allColumnAdapter by lazy {
        AllColumnAdapter().apply {
            addHeaderView(bannerView)
        }
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.all_column))
        initStateLayout(binding.stateLayout)
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)

        (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false


        binding.recyclerView.init(
            allColumnAdapter,
            layoutManager = GridLayoutManager(this, 2)
        )

        allColumnAdapter.setOnItemClickListener { adapter, view, pos ->
            var data = allColumnAdapter.getItem(pos)

            ArouterUtils.toArticleColumnDetailActivity(data.article_detail?.id ?: 0)

            MobAnalysisUtils.mInstance.sendEvent(
                UMEventKey.Event_FertilityGuidePage_ColumnCard_Click,
                mutableMapOf("Column_id" to "${data.article_detail?.id ?: 0}")
            )
        }

    }

    var page = 1
    override fun initData() {
        started {
            viewModel.getBannerList().next {
                bannerView.isVisible = this.banners.isNotEmpty()
                bannerView.setData(this.banners) {

                }
            }
        }
        started {
            viewModel.topicAllList(page).netCatch {
                showErrorState()
            }.next {
                showContentState()

                if (page == 1) {
                    allColumnAdapter.setNewInstance(data)
                } else {
                    allColumnAdapter.addData(data)
                }

                page++
                binding.smartRefresh.finishLoadMore()

                if (data.size < Constant.PAGE_SIZE) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                }

            }
        }

    }


    override fun getData() {
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        binding.smartRefresh.finishRefresh()

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        initData()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        page = 1
        initData()

    }


}