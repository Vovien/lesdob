package com.tubewiki.mine.view.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.resumed
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.adapter.MineQuestionAdapter
import com.tubewiki.mine.databinding.FragmentQuestionBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/mine/questions/fragment")
class QuestionsFragment : ViewModelFragment<MineFragmentViewModel, FragmentQuestionBinding>(),
    OnRefreshLoadMoreListener {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var uid = 0

    private val adapter by lazy { MineQuestionAdapter() }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        initPageState(binding.container)
        setPlaceholderView()
        resumed {
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@QuestionsFragment)
            }
            binding.recyclerView.init(adapter)
        }
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
    private fun setPlaceholderView() {

        binding.container.retry = { refreshDataWhenError() }
    }

    private fun initData() {
        loadData(true)
    }

    private fun loadData(isRefresh: Boolean) {
        if (isRefresh) {
            binding.smartRefresh.resetNoMoreData()
        }
        if (uid == Constant.getUserId()) {
            if (uid == Constant.getUserId()) {
                viewModel.getMineQuestion(isRefresh)
            } else {
                showEmptyState()
            }
        } else {
            viewModel.getOtherUserQuestion(uid, isRefresh)
        }
    }

    override fun getData() {
        super.getData()
        viewModel.mineQuestionResult.observe(this) {
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

        viewModel.otherUserQuestionResult.observe(this) {
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


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        loadData(true)
    }
}