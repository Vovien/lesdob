package com.tubewiki.mine.view.collect

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.view.state.showEmptyState2
import com.apkdv.mvvmfast.view.state.showNoSearchDataState
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.adapter.MineCollectionAnswerAdapter
import com.tubewiki.mine.databinding.FragmentAnswerBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/mine/collection/answer_fragment")
class CollectionAnswerFragment : ViewModelFragment<MineFragmentViewModel, FragmentAnswerBinding>(),
    OnRefreshLoadMoreListener {

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var isSearch: Boolean = false

    private val adapter by lazy { MineCollectionAnswerAdapter() }

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
                smartRefresh.setOnRefreshLoadMoreListener(this@CollectionAnswerFragment)
            }
            initData()
        }
    }

    private fun setPlaceholderView() {
        binding.container.retry = {
            refreshDataWhenError()
        }
    }

    private fun initData() {
        binding.smartRefresh.resetNoMoreData()
        viewModel.getMineAnswerCollection(true, keyWord)
    }


    override fun getData() {
        super.getData()
        viewModel.mineAnswerCollectionResult.observe(this) {
            if (parentFragment is CollectionFragment) {
                (parentFragment as CollectionFragment).setSearchFinish(0)
            }
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data2) {
                (parentFragment as CollectionFragment).updateTitle(0, it.data1.total)
                adapter.setNewInstance(it.data1.datas)
            } else {
                adapter.addData(it.data1.datas)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getMineAnswerCollection(true, keyWord)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.getMineAnswerCollection(false, keyWord)

    }


    override fun showContentState() {
        super.showContentState()
        binding.smartRefresh.finish()
    }


    override fun showEmptyState() {
        if (isSearch) {
            binding.container.showNoSearchDataState()
        } else super.showEmptyState()
        binding.smartRefresh.finish()
        (parentFragment as CollectionFragment).updateTitle(0, 0)
        //  adapter.setEmptyView(R.layout.default_white_bg_viewstatus_box_empty)
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getMineAnswerCollection(true, keyWord)
    }

    var keyWord = ""
    fun searchData(keyWord: String) {
        showLoadingState()
        this.keyWord = keyWord
        adapter.highlight = keyWord
        binding.smartRefresh.resetNoMoreData()
        viewModel.getMineAnswerCollection(true, keyWord)
    }

}