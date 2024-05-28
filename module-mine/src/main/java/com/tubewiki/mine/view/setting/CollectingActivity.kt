package com.tubewiki.mine.view.setting

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.BarUtils
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.adapter.ContentAdapter
import com.jmbon.middleware.databinding.CommonListLayoutBinding
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.tubewiki.mine.R
import com.tubewiki.mine.view.model.SettingViewModel

@Route(path = "/mine/setting/collect")
class CollectingActivity : ViewModelActivity<SettingViewModel, CommonListLayoutBinding>() {

    private val adapter by lazy {
        ContentAdapter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        initPageState()
        setTitleName("我的收藏")
        ImmersionBar.with(this)
            .statusBarColor(R.color.black)
            .transparentNavigationBar()
            .init()
        setTitleBarColor(Color.BLACK)
        titleBarView.centerTextView.setTextColor(Color.WHITE)
        val imageTintList = ColorStateList.valueOf(Color.WHITE)
        titleBarView.leftImageButton.imageTintList = imageTintList

        binding.apply {
            rvContent.setPadding(0, BarUtils.getStatusBarHeight() + 44.dp, 0, 0)
            srlRefresh.setEnableRefresh(false)
            rvContent.init(adapter, dividerHeight = 10f, vertical = false)
            srlRefresh.setOnLoadMoreListener {
                viewModel.currentPage++
                viewModel.getMineCollection()
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.mineCollection.observe(this) {
            showContentState()
            binding.srlRefresh.dealPage(viewModel.currentPage, it, adapter) { _, emptyText ->
                emptyText.text = "暂无收藏内容"
            }
        }
        viewModel.getMineCollection()
    }

    override fun onRestart() {
        super.onRestart()
        // 从其他页面返回后刷新列表
        viewModel.currentPage = 1
        viewModel.getMineCollection()
    }
}