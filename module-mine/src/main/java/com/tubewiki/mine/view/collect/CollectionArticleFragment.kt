package com.tubewiki.mine.view.collect

import android.view.Gravity
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.view.state.showNoSearchDataState
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.adapter.MineCollectionArticleAdapter
import com.tubewiki.mine.databinding.FragmentAnswerBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/mine/collection/article_fragment")
class CollectionArticleFragment : ViewModelFragment<MineFragmentViewModel, FragmentAnswerBinding>(),
    OnRefreshLoadMoreListener {

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var isSearch: Boolean = false

    @Autowired(name = TagConstant.PAGE_TYPE)
    @JvmField
    var isCollection: Boolean = false

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var uid = 0

    private val adapter by lazy { MineCollectionArticleAdapter() }
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        initPageState(binding.container)
        setPlaceholderView()
        started {
            binding.recyclerView.init(adapter)
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@CollectionArticleFragment)
            }
            loadData(true)
        }
    }

    private fun setPlaceholderView() {
        binding.container.retry = { refreshDataWhenError() }
        if (!isCollection) {
            binding.container.offset = 180f.dp()
            binding.container.orientation = Gravity.BOTTOM
        }
    }


    private fun loadData(isRefresh: Boolean) {
        if (isRefresh) {
            binding.smartRefresh.resetNoMoreData()
        }
        if (uid == 0) {
            if (isSearch && keyWord.isNotNullEmpty()) {
                viewModel.getMineArticleCollection(isRefresh, keyWord)
                return
            }
            if (Constant.isLogin) {
                viewModel.getMineArticleCollection(isRefresh, keyWord)
            } else {
                showEmptyState()
            }
        } else {
            viewModel.getOtherUserArticle(uid, isRefresh)
        }
    }

    override fun getData() {
        super.getData()

        viewModel.mineArticleCollectionResult.observe(this) {
            if (parentFragment is CollectionFragment) {
                (parentFragment as CollectionFragment).setSearchFinish(1)
            }
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            binding.smartRefresh.finish()
            if (it.data2) {
                if (parentFragment is CollectionFragment)
                    (parentFragment as CollectionFragment).updateTitle(1, it.data1.total)
                adapter.setNewInstance(it.data1.datas)
            } else {
                adapter.addData(it.data1.datas)
            }
            if (adapter.data.isNullOrEmpty()) {
                showEmptyState()
            } else showContentState()
        }

        viewModel.otherUserArticleResult.observe(this) {
            if (parentFragment is CollectionFragment) {
                (parentFragment as CollectionFragment).setSearchFinish(1)
            }
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            binding.smartRefresh.finish()
            if (it.data2) {
                adapter.setNewInstance(it.data1)
            } else {
                adapter.addData(it.data1)
            }

            if (adapter.data.isNullOrEmpty())
                showEmptyState()
            else showContentState()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadData(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadData(false)
    }

    override fun showEmptyState() {
        if (isSearch) {
            binding.container.showNoSearchDataState()
        } else super.showEmptyState()
        if (parentFragment is CollectionFragment)
            (parentFragment as CollectionFragment).updateTitle(1, 0)
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        loadData(true)
    }

    var keyWord = ""
    fun searchData(keyWord: String) {
        showLoadingState()
        this.keyWord = keyWord
        adapter.highlight = keyWord
        binding.smartRefresh.resetNoMoreData()
        viewModel.getMineArticleCollection(true, keyWord)
    }

}