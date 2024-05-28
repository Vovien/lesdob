package com.tubewiki.mine.view.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.resumed
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.FollowQuestionAdapter
import com.tubewiki.mine.databinding.FragmentQuestionBinding
import com.tubewiki.mine.view.model.FollowViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


@Route(path = "/mine/follow/follow_questions")
class FollowQuestionsFragment : ViewModelFragment<FollowViewModel, FragmentQuestionBinding>(),
    OnRefreshLoadMoreListener {


    private val adapter by lazy { FollowQuestionAdapter() }

    override fun initView(view: View) {

        initPageState(binding.container)
        setPlaceholderView()
        resumed {
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@FollowQuestionsFragment)
            }
            binding.recyclerView.init(adapter)
            adapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.sb_focus_on) {
                    var data = adapter.data.get(position) as Question
                    //取消关注问题
                    viewModel.focusQuestion(position, data.questionId, false)
                }
            }
            viewModel.getFollowQuestionData(true)
        }
    }

    private fun setPlaceholderView() {
        binding.container.retry = {
            refreshDataWhenError()
        }
    }
    override fun getData() {
        super.getData()
        viewModel.followQuestionResult.observe(this, {
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
            if (!it.isFocus && it.isQuestion) {
                for (index in (adapter.data.size - 1) downTo 0) {
                    if (adapter.data[index].questionId == it.id) {
                        adapter.removeAt(index)
                        if (adapter.data.size == 0) {
                            viewModel.getFollowQuestionData(true)
                        }
                        return@register
                    }
                }
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        if (!isDetached)
            viewModel.getFollowQuestionData(true)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (!isDetached)
            viewModel.getFollowQuestionData(false)
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
        viewModel.getFollowQuestionData(true)
    }
}