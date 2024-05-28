package com.tubewiki.mine.view.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.resumed
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.FollowDoctorAdapter
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
@Route(path = "/mine/follow/follow_doctor")
class FollowDoctorFragment : ViewModelFragment<FollowViewModel, FragmentQuestionBinding>(),
    OnRefreshLoadMoreListener {


    private val doctorAdapter by lazy { FollowDoctorAdapter() }


    override fun initView(view: View) {
        initPageState(binding.container)
        binding.container.offset = 80f.dp()
        resumed {
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@FollowDoctorFragment)
            }

            binding.recyclerView.init(doctorAdapter)

            doctorAdapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.sb_focus_on) {
                    val data = adapter.data.get(position) as FollowUserData.User
                    viewModel.focusUser(data.uid, !data.isFocus, position)
                }
            }

            doctorAdapter.setOnItemClickListener { adapter, view, position ->
                val item = doctorAdapter.data[position]

                ARouter.getInstance().build("/mine/message/personal_page")
                    .withInt(TagConstant.USER_CANCEL, item.isCancel)
                    .withInt(TagConstant.PARAMS, item.uid).navigation()

            }

            viewModel.getFollowDoctorData(true)
        }
    }


    override fun getData() {
        viewModel.followDoctorResult.observe(this) {

            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data2) {
                doctorAdapter.setNewInstance(it.data1)
            } else {
                doctorAdapter.addData(it.data1)
            }
        }


        KotlinBus.register(
            this.hashCode().toString(),
            UI,
            FocusChangedEvent::class.java
        ) {
            if (it.isUser) {
                for (index in (doctorAdapter.data.size - 1) downTo 0) {
                    if (doctorAdapter.data[index].uid == it.id) {
                        doctorAdapter.data[index].isFocus = it.isFocus
                        doctorAdapter.notifyItemChanged(index)
                        if (doctorAdapter.data.size == 0) {
                            viewModel.getFollowDoctorData(true)
                        }
                        return@register
                    }
                }
            }
        }

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (!isDetached) {
            viewModel.getFollowDoctorData(false)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        if (!isDetached) {
            viewModel.getFollowDoctorData(true)
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
        viewModel.getFollowDoctorData(true)
    }
}