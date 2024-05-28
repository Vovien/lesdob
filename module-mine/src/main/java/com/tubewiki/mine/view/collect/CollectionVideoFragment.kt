package com.tubewiki.mine.view.collect

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.divider.GridSpacingItemDecoration2
import com.apkdv.mvvmfast.view.state.showNoSearchDataState
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.adapter.MineCollectionVideoAdapter
import com.tubewiki.mine.databinding.FragmentCollectVideoBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 个人/收藏视频fragment
 */
@Route(path = "/mine/collection/video_fragment")
class CollectionVideoFragment :
    ViewModelFragment<MineFragmentViewModel, FragmentCollectVideoBinding>(),
    OnRefreshLoadMoreListener {

    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var isSearch: Boolean = false

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var uid = 0


    @Autowired(name = TagConstant.PAGE_TYPE)
    @JvmField
    var isCollection: Boolean = false

    private val adapter by lazy { MineCollectionVideoAdapter(uid) }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        initPageState(binding.container)
        setPlaceholderView()
        started {
            val linearLayoutManager = GridLayoutManager(requireContext(), 3)
            binding.recyclerView.init(adapter, linearLayoutManager)
            binding.recyclerView.addItemDecoration(GridSpacingItemDecoration2(3, 3f.dp(), false))
            binding.apply {
                smartRefresh.setOnRefreshLoadMoreListener(this@CollectionVideoFragment)
            }
            adapter.setOnItemClickListener { _, _, position ->
                ARouter.getInstance().build("/video/details")
                    .withInt(TagConstant.VIDEO_FROM_HOME_POSITION, position)
                    .withBoolean(TagConstant.VIDEO_FROM_HOME, uid != 0)
                    .withBoolean(TagConstant.VIDEO_FROM_COLLECTION, isCollection)
                    .withString(TagConstant.VIDEO_COLLECTION_SEARCH_KEY, keyWord)
                    .withParcelable(TagConstant.VIDEO_DATA, adapter.data[position])
                    .navigation()
            }
            initData()

        }
    }

    private fun setPlaceholderView() {
        binding.container.retry = {
            refreshDataWhenError()
        }
        if (!isCollection) {
            binding.container.offset = 180f.dp()
            binding.container.orientation = Gravity.BOTTOM
        }
    }

    private fun initData() {
        binding.smartRefresh.resetNoMoreData()
        loadData(true)
    }

    fun loadData(isRefresh: Boolean) {
        if (uid == 0) {
            viewModel.getMineVideoCollection(isRefresh, keyWord)
        } else {
            viewModel.getOtherUserVideo(uid, isRefresh)
        }
    }

    override fun getData() {
        super.getData()
        viewModel.mineVideoCollectionResult.observe(this) {
            if (parentFragment is CollectionFragment) {
                (parentFragment as CollectionFragment).setSearchFinish(0)
            }
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            if (it.data2) {
                if (parentFragment is CollectionFragment) {
                    (parentFragment as CollectionFragment).updateTitle(2, it.data1.total)
                }
                adapter.setNewInstance(it.data1.datas)
            } else {
                adapter.addData(it.data1.datas)
            }
        }

        viewModel.otherUserVideoResult.observe(this) {
            if (parentFragment is CollectionFragment) {
                (parentFragment as CollectionFragment).setSearchFinish(1)
            }
            if (it.data3) {
                binding.smartRefresh.finishLoadMoreWithNoMoreData()
            }
            binding.smartRefresh.finish()
            if (it.data2) {
                adapter.setNewInstance(it.data1)
            } else {
                adapter.addData(it.data1)
            }

            if (adapter.data.isNullOrEmpty())
                showEmptyState()
            else showContentState()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadData(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadData(false)

    }


    override fun showContentState() {
        super.showContentState()
        binding.smartRefresh.finish()
    }


    override fun showEmptyState() {
        if (isSearch) {
            binding.container.showNoSearchDataState()
        } else super.showEmptyState()
        binding.smartRefresh.finish()
        if (parentFragment is CollectionFragment)
            (parentFragment as CollectionFragment).updateTitle(2, 0)
        //  adapter.setEmptyView(R.layout.default_white_bg_viewstatus_box_empty)
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        loadData(true)
    }

    var keyWord = ""
    fun searchData(keyWord: String) {
        showLoadingState()
        this.keyWord = keyWord
        //adapter.highlight = keyWord
        binding.smartRefresh.resetNoMoreData()
        loadData(true)
    }
}