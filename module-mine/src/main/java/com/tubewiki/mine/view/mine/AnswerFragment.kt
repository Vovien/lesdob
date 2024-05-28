package com.tubewiki.mine.view.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.resumed
import com.apkdv.mvvmfast.utils.divider.HorizontalDividerItemDecoration
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.common.adapter.FollowQuestionRecommendAdapter
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.FragmentAnswerBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/mine/answer/fragment")
class AnswerFragment : ViewModelFragment<MineFragmentViewModel, FragmentAnswerBinding>(),
    OnRefreshLoadMoreListener {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var uid = 0

    private val adapter by lazy { FollowQuestionRecommendAdapter(isUseMinePage = true) }


    override fun initView(view: View) {

        initPageState(binding.container)
        setPlaceholderView()
        resumed {
            binding.smartRefresh.setEnableFooterFollowWhenNoMoreData(true)
            binding.recyclerView.init(
                adapter,
                HorizontalDividerItemDecoration.Builder(context)
                    .size(4f.dp())
                    .showLastDivider()
                    .color(ColorUtils.getColor(R.color.colorF9F9F9)).build()
            )

            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@AnswerFragment)
            }
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
            if (Constant.isLogin)
                viewModel.getMineAnswer(isRefresh)
            else showEmptyState()
        } else {
            viewModel.getOtherUserAnswer(uid, isRefresh)
        }
    }


    override fun getData() {
        super.getData()
        viewModel.mineAnswerResult.observe(this) {
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

        viewModel.otherUserAnswerResult.observe(this) {
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

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        loadData(true)
    }
}