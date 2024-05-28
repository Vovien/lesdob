package com.tubewiki.mine.view.message

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.OfficialMessageAdapter
import com.tubewiki.mine.bean.OfficialMessageData
import com.tubewiki.mine.databinding.FragmentMutuallyMessageBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/mine/fragment/message/official")
class JmMessageFragment :
    ViewModelFragment<MessageCenterViewModel, FragmentMutuallyMessageBinding>(),
    OnRefreshLoadMoreListener {
    private val adapter by lazy { OfficialMessageAdapter() }

    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type = "all"


    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        initStateLayout(binding.stateLayout)
        binding.smartRefresh.setOnRefreshLoadMoreListener(this)

        binding.recyclerView.init(adapter)

        viewModel.getOfficialMessage(type, true)

        adapter.setOnItemClickListener { adapter, view, position ->
            val official = adapter.data.get(position) as OfficialMessageData.Official

        }

        adapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tv_wallet) {
                ARouter.getInstance().build("/mine/wallet").navigation()
            }
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

    override fun getData() {
        super.getData()
        viewModel.officialMessageDataResult.observe(this) {
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data1.isNotNullEmpty()) {
                if (it.data2) {
                    adapter.setNewInstance(it.data1)
                } else {
                    adapter.addData(it.data1)
                }
            }

            binding.smartRefresh.finish()
        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        viewModel.getOfficialMessage(type, true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        viewModel.getOfficialMessage(type, false)
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getOfficialMessage(type, true)

    }
}