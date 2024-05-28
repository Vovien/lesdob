package com.tubewiki.mine.view.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.adapter.DraftAdapter
import com.tubewiki.mine.databinding.FragmentAnswerBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/mine/questions/draft")
class DraftFragment : ViewModelFragment<MineFragmentViewModel, FragmentAnswerBinding>(),
    OnRefreshLoadMoreListener {
    private val adapter by lazy { DraftAdapter() }

    override fun initView(view: View) {
        initPageState(binding.container)
        setPlaceholderView()
        binding.recyclerView.init(adapter)
        binding.apply {
            smartRefresh.setOnRefreshLoadMoreListener(this@DraftFragment)
        }
        initData()
    }
    private fun setPlaceholderView() {
        binding.container.retry = {
            refreshDataWhenError()
        }
    }
    private fun initData() {
        binding.smartRefresh.resetNoMoreData()
        viewModel.getMineDraft(true)
    }

    private var isFirst = true
    override fun onResume() {
        super.onResume()
        if (!isFirst) {
            initData()
        }
        isFirst = false
    }

    override fun getData() {
        super.getData()

        viewModel.mineDraftResult.observe(this) {
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            binding.smartRefresh.finish()
            if (it.data2) {
                adapter.setNewInstance(it.data1)
            } else {
                adapter.addData(it.data1)

            }
        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getMineDraft(true)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.getMineDraft(false)

    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getMineDraft(true)
    }
}