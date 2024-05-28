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
import com.tubewiki.mine.adapter.FollowTopicAdapter
import com.tubewiki.mine.bean.FollowTopicData
import com.tubewiki.mine.databinding.FragmentQuestionBinding
import com.tubewiki.mine.view.model.FollowViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * time   : 2021/5/26
 * desc   : 我的关注-话题
 * version: 1.0
 */
@Route(path = "/mine/follow/follow_topic")
class FollowTopicFragment : ViewModelFragment<FollowViewModel, FragmentQuestionBinding>(),
    OnRefreshLoadMoreListener {


    private val adapter by lazy { FollowTopicAdapter() }


    override fun initView(view: View) {
        initPageState(binding.container)
        binding.container.offset = 80f.dp()
        resumed {
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@FollowTopicFragment)
            }

            binding.recyclerView.init(adapter)

            adapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.sb_focus_on) {
                    var data = adapter.data.get(position) as FollowTopicData.Topic
                    //取消关注问题
                    viewModel.topicFocus(data.topicId, false, position)
                }
            }

            viewModel.getFollowTopicData(true)
        }
    }


    override fun getData() {


        viewModel.followTopicResult.observe(this, {

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
            if (!it.isFocus && it.isTopic) {
                for (index in (adapter.data.size - 1) downTo 0) {
                    if (adapter.data[index].topicId == it.id) {
                        adapter.removeAt(index)
                        if (adapter.data.size == 0) {
                            viewModel.getFollowTopicData(true)
                        }
                        return@register
                    }
                }
            }
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (!isDetached)
            viewModel.getFollowTopicData(false)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        if (!isDetached)
            viewModel.getFollowTopicData(true)
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
        viewModel.getFollowTopicData(true)
    }
}