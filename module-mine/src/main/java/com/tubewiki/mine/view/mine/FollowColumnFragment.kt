package com.tubewiki.mine.view.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.resumed
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.FollowColumnAdapter
import com.tubewiki.mine.bean.FollowColumnData
import com.tubewiki.mine.databinding.FragmentQuestionBinding
import com.tubewiki.mine.view.model.FollowViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * time   : 2021/5/26
 * desc   : 我的关注-专栏
 * version: 1.0
 */
@Route(path = "/mine/follow/follow_column")
class FollowColumnFragment : ViewModelFragment<FollowViewModel, FragmentQuestionBinding>(),
    OnRefreshLoadMoreListener {


    private val adapter by lazy { FollowColumnAdapter() }


    override fun initView(view: View) {
        initPageState(binding.container)
        binding.container.offset = 80f.dp()

        resumed {
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@FollowColumnFragment)
            }

            binding.recyclerView.init(adapter)

            adapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.sb_focus_on) {
                    var data = adapter.data.get(position) as FollowColumnData.Column
                    viewModel.columnFocus(position, data.columnId, false)
                }
            }

            viewModel.getFollowColumnData(true)
        }
    }


    override fun getData() {
        viewModel.followColumnResult.observe(this, {

            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data2) {
                adapter.setNewInstance(it.data1)
            } else {
                adapter.addData(it.data1)
            }

        })
        KotlinBus.register(
            this.hashCode().toString(),
            UI,
            FocusChangedEvent::class.java
        ) {
            if (!it.isFocus && it.isColumn) {
                for (index in (adapter.data.size - 1) downTo 0) {
                    if (adapter.data[index].columnId == it.id) {
                        adapter.removeAt(index)
                        if (adapter.data.size == 0) {
                            viewModel.getFollowColumnData(true)
                        }
                        return@register
                    }
                }
            }
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (!isDetached)
            viewModel.getFollowColumnData(false)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        if (!isDetached)
            viewModel.getFollowColumnData(true)
    }

    override fun showContentState() {
        super.showContentState()
        binding.smartRefresh.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        KotlinBus.unregister(this.hashCode().toString())
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getFollowColumnData(false)
    }
}