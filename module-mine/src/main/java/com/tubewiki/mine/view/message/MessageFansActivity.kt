package com.tubewiki.mine.view.message

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.jmbon.middleware.bean.User
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.logInToIntercept
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.FansMessageAdapter
import com.tubewiki.mine.databinding.ActivityMessageFansBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * 粉丝消息
 */
@Route(path = "/mine/message/fans")
class MessageFansActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityMessageFansBinding>(),
    OnRefreshLoadMoreListener {

    private val adapter by lazy { FansMessageAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.fans))
        initStateLayout(binding.stateLayout)
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)


        binding.recyclerView.init(adapter)

        viewModel.getFansMessage(false)


        adapter.setOnItemChildClickListener { adapter, view, position ->
            val fan = adapter.data.get(position) as User
            when (view.id) {
                R.id.sb_focus_on -> Runnable {
                    // focus或unfocus
                    //如果关注了或者相互关注就取消关注
                    viewModel.focusUser(fan.uid, !(fan.isFocus && fan.isMutualFocus), position)
                }.logInToIntercept()
            }
        }

        adapter.setOnItemClickListener { adapter, view, position ->
            val fan = adapter.data.get(position) as User
            ARouter.getInstance().build("/mine/message/personal_page")
                .withInt(TagConstant.PARAMS, fan.uid).navigation()
        }
    }

    private var isFirst = true
    override fun onResume() {
        super.onResume()

        if (!isFirst) {
            adapter.isAllRead = true
            adapter.notifyDataSetChanged()
        }
        isFirst = false
    }

    override fun initData() {
    }

    override fun getData() {
        viewModel.fansMessageDataResult.observe(this, {
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (!it.data1.isNullOrEmpty()) {
                if (it.data2) {
                    adapter.setNewInstance(it.data1)

                } else {
                    adapter.addData(it.data1)
                }
            }

            binding.smartRefresh.finish()
        })

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getFansMessage(true)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.getFansMessage(false)

    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getFansMessage(true)
    }

}