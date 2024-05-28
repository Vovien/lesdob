package com.tubewiki.android.fragment

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.databinding.CommonListLayoutBinding
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.extention.getViewModel
import com.tubewiki.android.adapter.CircleRecommendAdapter
import com.tubewiki.android.viewmodel.MainViewModel

@Route(path = "/app/circle/recommend")
class RecommendFragment :
    ViewModelFragment<MainViewModel, CommonListLayoutBinding>() {

    private val parentViewModel by lazy {
        parentFragment.getViewModel(MainViewModel::class.java)
    }

    private val circleAdapter by lazy { CircleRecommendAdapter() }
    override fun initView(view: View) {
        binding.apply {
            srlRefresh.setEnableRefresh(false)
            srlRefresh.setEnableLoadMore(false)
            rvContent.adapter = circleAdapter
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        parentViewModel?.circleConfigLD?.observe(this) {
            binding.srlRefresh.dealPage(viewModel.currentPage, it?.circle_list, circleAdapter) { _, emptyText ->
                emptyText.text = "暂无推荐圈子"
            }
        }
    }
}