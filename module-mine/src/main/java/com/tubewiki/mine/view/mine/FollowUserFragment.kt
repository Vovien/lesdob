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
import com.tubewiki.mine.adapter.FollowUserAdapter
import com.tubewiki.mine.bean.FollowUserData
import com.tubewiki.mine.databinding.FragmentQuestionBinding
import com.tubewiki.mine.view.model.FollowViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * time   : 2021/5/26
 * desc   : 我的关注-专栏
 * version: 1.0
 */
@Route(path = "/mine/follow/follow_user")
class FollowUserFragment : ViewModelFragment<FollowViewModel, FragmentQuestionBinding>(),
    OnRefreshLoadMoreListener {

    private val adapter by lazy { FollowUserAdapter() }


    override fun initView(view: View) {
        initPageState(binding.container)
        binding.container.offset = 80f.dp()
        resumed {
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@FollowUserFragment)
            }

            binding.recyclerView.init(adapter)

            adapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.sb_focus_on) {
                    val data = adapter.data.get(position) as FollowUserData.User
                    viewModel.focusUser(data.uid, !data.isFocus, position)
                }
            }

            viewModel.getFollowUserData(true)
        }
    }


    override fun getData() {
        viewModel.followUserResult.observe(this, {

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
            if (it.isUser) {
                for (index in (adapter.data.size - 1) downTo 0) {
                    if (adapter.data[index].uid == it.id) {
                        adapter.data[index].isFocus = it.isFocus
                        adapter.notifyItemChanged(index)
                        if (adapter.data.size == 0) {
                            viewModel.getFollowUserData(true)
                        }
                        return@register
                    }
                }
            }
        }

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (!isDetached) {
            viewModel.getFollowUserData(false)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        if (!isDetached) {
            viewModel.getFollowUserData(true)
        }
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
        viewModel.getFollowUserData(true)
    }
}