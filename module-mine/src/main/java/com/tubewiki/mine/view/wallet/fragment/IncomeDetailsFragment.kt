package com.tubewiki.mine.view.wallet.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.resumed
import com.apkdv.mvvmfast.utils.SizeUtil
import com.apkdv.mvvmfast.view.state.state.EmptyState
import com.drakeet.multitype.MultiTypeAdapter
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.initAdapter
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.delegate.IncomeViewDelegate
import com.tubewiki.mine.adapter.delegate.WithdrawalViewDelegate
import com.tubewiki.mine.databinding.FragmentIncomeDetailsBinding
import com.tubewiki.mine.view.model.WalletViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import java.util.*


/**
 * 收入明细
 */
@Route(path = "/mine/wallet/fragment/income")
class IncomeDetailsFragment : ViewModelFragment<WalletViewModel, FragmentIncomeDetailsBinding>(),
    OnLoadMoreListener {
    // type 0 收入  1 提现
    @Autowired(name = TagConstant.TYPE)
    @JvmField
    var type: Int = 0

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    private val items = ArrayList<Any>()
    lateinit var adapter: MultiTypeAdapter

    override fun initView(view: View) {
        binding.apply {
            initPageState(binding.container)
            binding.container.retry = { refreshDataWhenError() }
            binding.container.offset = SizeUtil.getHeight() / 3
            adapter = recyclerView.initAdapter(
                dividerHeight = 1f,
                left = 70f,
                vertical = false,
                showEnd = true,
                dividerColor = resources.getColor(R.color.color_F9F9F9)
            )
            adapter.register(IncomeViewDelegate())
            adapter.register(WithdrawalViewDelegate())
            adapter.items = items
            smartRefresh.setOnLoadMoreListener(this@IncomeDetailsFragment)
        }
        resumed {
            initData(true)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initData(isRefresh: Boolean) {
        viewLifecycleOwner.lifecycleScope.apply {
            viewModel.apply {
                if (type == 0)
                    launchWhenStarted {
                        incomeList(isRefresh).next {
                            if (isRefresh)
                                items.clear()
                            items.addAll(data1)
                            processData(data2)
                        }
                    }
                else
                    launchWhenStarted {
                        rewardsList(isRefresh).next {
                            if (isRefresh)
                                items.clear()
                            items.addAll(data1)
                            processData(data2)
                        }
                    }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun processData(noMore: Boolean) {
        adapter.notifyDataSetChanged()
        if (adapter.items.isNotEmpty())
            showContentState()
        else showEmptyState()
        if (noMore)
            binding.smartRefresh.finishLoadMoreWithNoMoreData()
        else
            binding.smartRefresh.finish()
    }

    override fun showEmptyState() {
        if (stateContainer?.lastState != EmptyState::class.java.name)
            super.showEmptyState()
    }


    override fun onResume() {
        super.onResume()
        if (type == 1) {
            binding.smartRefresh.resetNoMoreData()
            initData(true)
        }
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        initData(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        initData(false)
    }
}