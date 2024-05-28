package com.tubewiki.home.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.databinding.CommonListLayoutBinding
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.tubewiki.home.R
import com.tubewiki.home.bean.QuestionItemRecordBean
import com.tubewiki.home.router.HomeRouter
import com.tubewiki.home.viewmodel.MainFragmentViewModel

/******************************************************************************
 * Description: 问题分类页面
 *
 * Author: jhg
 *
 * Date: 2023/10/12
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = HomeRouter.HOME_QUESTION_CATEGORY)
class QuestionCategoryActivity: ViewModelActivity<MainFragmentViewModel, CommonListLayoutBinding>() {

    @JvmField
    @Autowired
    var categoryId: Int = 0

    private val recordAdapter by lazy {
        object: BaseQuickAdapter<QuestionItemRecordBean, BaseViewHolder>(R.layout.item_question_category_record_layout) {

            override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                super.onAttachedToRecyclerView(recyclerView)
                setOnItemClickListener { _, _, position ->
                    Action{
                        data[position].apply {
                            ARouter.getInstance().navigation(IMiniToolProvider::class.java).toAdvisoryHelp(
                                questionId =  question_id,
                                question = question
                            )
                        }
                    }.logInToIntercept()
                }
            }

            override fun convert(holder: BaseViewHolder, item: QuestionItemRecordBean) {
                holder.setText(R.id.tv_content, item.question)
            }
        }
    }
    override fun initView(savedInstanceState: Bundle?) {
        initPageState()
        ImmersionBar.with(this)
            .statusBarColor(R.color.black)
            .transparentNavigationBar()
            .init()
        setTitleBarColor(Color.BLACK)
        val imageTintList = ColorStateList.valueOf(Color.WHITE)
        titleBarView.leftImageButton.imageTintList = imageTintList
        binding.rvContent.setPadding(20.dp, 100.dp, 20.dp, 24.dp)
        binding.rvContent.init(recordAdapter, dividerHeight = 12f, vertical = false)
        binding.srlRefresh.setEnableRefresh(false)
        binding.srlRefresh.setEnableLoadMore(false)
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        viewModel.questionCategoryListLD.observe(this) {
            showContentState()
            binding.srlRefresh.dealPage(viewModel.currentPage, it, recordAdapter) { _, emptyText ->
                emptyText.text = "暂无分类问题"
            }
        }
        viewModel.getQuestionCategory(categoryId)
    }
}