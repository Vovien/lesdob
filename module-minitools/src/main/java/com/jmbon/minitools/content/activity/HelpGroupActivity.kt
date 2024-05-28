package com.jmbon.minitools.content.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.databinding.CommonListLayoutBinding
import com.jmbon.middleware.decoration.GridSpacingItemDecoration
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.R
import com.jmbon.minitools.content.adapter.HelpGroupAdapter
import com.jmbon.minitools.content.router.ContentRouter
import com.jmbon.minitools.content.viewmodel.ContentViewModel

/******************************************************************************
 * Description: 好孕互助群
 *
 * Author: jhg
 *
 * Date: 2023/9/15
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = ContentRouter.APP_HELP_GROUP)
class HelpGroupActivity: ViewModelActivity<ContentViewModel, CommonListLayoutBinding>() {

    @Autowired
    @JvmField
    var type: Int = 0

    @Autowired
    @JvmField
    var title: String = ""

    private val helpGroupAdapter by lazy {
        HelpGroupAdapter().apply {
            addFooterView(TextView(this@HelpGroupActivity).apply {
                setPadding(0, 22.dp, 0, 32.dp)
                gravity = Gravity.CENTER
                text = "到底咯"
                textSize = 16f
                setTextColor(R.color.white40.toColorInt())
            })
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .statusBarColorInt(Color.BLACK)
            .transparentNavigationBar()
            .init()
        setTitleName(title)
        titleBarView.setTitleColor(Color.WHITE)
        val imageTintList = ColorStateList.valueOf(Color.WHITE)
        titleBarView.leftImageButton.imageTintList = imageTintList
        titleBarView.setBackgroundColor(Color.BLACK)
        initPageState()
        binding.rvContent.layoutManager = GridLayoutManager(this, 2)
        binding.rvContent.apply {
            setPadding(0, 100.dp, 0, 0)
            addItemDecoration(GridSpacingItemDecoration(spacing = 10.dp, edgeSpacing = 20.dp))
            adapter = helpGroupAdapter
        }

        binding.srlRefresh.apply {
            setEnableRefresh(false)
            setEnableLoadMore(false)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        viewModel.helpGroupInfoLD.observe(this) {
            showContentState()
            if (!it?.group_type_title.isNullOrBlank()) {
                setTitleName(it?.group_type_title!!)
            }
            binding.srlRefresh.dealPage(viewModel.currentPage, it?.circle_list, helpGroupAdapter, emptyViewAction = {
                    _, emptyText ->
                emptyText.text = "暂无相关圈子"
            })
        }
        viewModel.getHelperGroupList(type)
    }
}