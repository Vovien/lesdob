package com.tubewiki.mine.view.collect

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.view.state.showNoSearchDataState
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.adapter.MineCollectionHospitalAdapter
import com.tubewiki.mine.databinding.FragmentHospitalBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 医院收藏
 */
@Route(path = "/mine/collection/hospital_fragment")
class CollectionHospitalFragment :
    ViewModelFragment<MineFragmentViewModel, FragmentHospitalBinding>(),
    OnRefreshLoadMoreListener {
    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var isSearch: Boolean = false

    private val adapter by lazy { MineCollectionHospitalAdapter() }
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        initPageState(binding.container)
        setPlaceholderView()
        started{
            initData()

            binding.recyclerView.init(adapter)

            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@CollectionHospitalFragment)
            }
        }
    }

    private fun setPlaceholderView() {
        binding.container.retry = {
            refreshDataWhenError()
        }
    }

    private fun initData() {
        binding.smartRefresh.resetNoMoreData()
        viewModel.getMineHospitalCollection(true, keyWord)
    }


    override fun getData() {
        super.getData()
        viewModel.mineHospitalCollectionResult.observe(this) {
            if (parentFragment is CollectionFragment) {
                (parentFragment as CollectionFragment).setSearchFinish(2)
            }
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data2) {
                (parentFragment as CollectionFragment).updateTitle(3, it.data1.total)
                adapter.setNewInstance(it.data1.datas)
            } else {
                adapter.addData(it.data1.datas)

            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getMineHospitalCollection(true, keyWord)

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.getMineHospitalCollection(false, keyWord)

    }

    override fun showContentState() {
        super.showContentState()
        binding.smartRefresh.finish()
    }


    override fun showEmptyState() {
        super.showEmptyState()
        if (isSearch) {
            binding.container.showNoSearchDataState()
        } else super.showEmptyState()
        binding.smartRefresh.finish()
        (parentFragment as CollectionFragment).updateTitle(3, 0)
        //  adapter.setEmptyView(R.layout.default_white_bg_viewstatus_box_empty)
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getMineHospitalCollection(true, keyWord)
    }


    var keyWord = ""
    fun searchData(keyWord: String) {
        showLoadingState()
        this.keyWord = keyWord
        adapter.highlight = keyWord
        binding.smartRefresh.resetNoMoreData()
        viewModel.getMineHospitalCollection(true, keyWord)
    }
}