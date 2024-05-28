package com.tubewiki.home.activity

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.utils.*
import com.jmbon.widget.recyclerview.CenterLayoutManager
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.adapter.CommonQuestionAdvBannerAdapter
import com.tubewiki.home.adapter.QuestionCategoryAdapter
import com.tubewiki.home.adapter.QuestionCategoryDetailAdapter
import com.tubewiki.home.databinding.ActivityCommonQuestionBinding
import com.tubewiki.home.viewmodel.MainFragmentViewModel


/**
 * 常见问题页面
 */
@Route(path = "/home/activity/common_question")
class CommonQuestionActivity :
    ViewModelActivity<MainFragmentViewModel, ActivityCommonQuestionBinding>() {
    @Autowired(name = TagConstant.TOPIC_ID)
    @JvmField
    var topicId = 0

    private val categoryAdapter by lazy {
        QuestionCategoryAdapter()
    }
    private val detailAdapter by lazy {
        QuestionCategoryDetailAdapter()
    }

    private val adapter by lazy {
        CommonQuestionAdvBannerAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
//        setTitleName(getString(R.string.common_question))
        StatusBarCompat.setTransparentStatusBar(this.window)
        StatusBarCompat.setStatusBarDarkMode(this, true)
        initStateLayout(binding.stateLayout)
        binding.back.onClick {
            onBackPressed()
        }

        var mCenterLayoutManager =
            CenterLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        binding.recyclerCategory.init(
            categoryAdapter, mCenterLayoutManager
        )


        binding.recyclerDetail.init(
            detailAdapter
        )



        categoryAdapter.setOnItemClickListener { adapter, view, pos ->
            categoryAdapter.selectedPos = pos
            categoryAdapter.notifyDataSetChanged()

            val data = categoryAdapter.getItem(pos)
            detailAdapter.setNewInstance(data.topics)
            binding.recyclerCategory.smoothScrollToPosition(pos)


            MobAnalysisUtils.mInstance.sendEvent(
                UMEventKey.Event_CommonProblemPage_FirstNav,
                mutableMapOf("navname" to "${data.problemName}")
            )

        }
        detailAdapter.setOnItemClickListener { adapter, view, pos ->
            val data = detailAdapter.getItem(pos)
            ArouterUtils.toArticleColumnDetailActivity(data.id)

            MobAnalysisUtils.mInstance.sendEvent(
                UMEventKey.Event_CommonProblemPage_SecondNav,
                mutableMapOf("navname" to "${data.topicName}")
            )
        }

    }


    override fun initData() {

    }


    override fun getData() {
        started {
            viewModel.getCommonQuestionAdv().netCatch {
                this.message.showToast()
            }.next {
                binding.banner.setIndicator(binding.indicator, false)
                adapter.setDatas(this.advList)
                binding.banner.setAdapter(adapter)
            }
        }
        started {
            viewModel.problemList().netCatch {
                showErrorState()
            }.next {
                showContentState()

                if (questions.isNotNullEmpty()) {
                    if (topicId == 0) {
                        categoryAdapter.selectedPos = 0
                        categoryAdapter.setNewInstance(questions)
                        detailAdapter.setNewInstance(questions[0].topics)
                        MobAnalysisUtils.mInstance.sendEvent(
                            UMEventKey.Event_CommonProblemPage_FirstNav,
                            mutableMapOf("navname" to "${questions[0].problemName}")
                        )
                    } else {
                        var data = questions.find {
                            it.id == topicId
                        }

                        var index = questions.indexOf(data)
                        if (index < 0 || index >= questions.size) {
                            return@next
                        }

                        categoryAdapter.selectedPos = index
                        categoryAdapter.setNewInstance(questions)
                        detailAdapter.setNewInstance(questions[index].topics)
                        binding.recyclerCategory.smoothScrollToPosition(index)

                        MobAnalysisUtils.mInstance.sendEvent(
                            UMEventKey.Event_CommonProblemPage_FirstNav,
                            mutableMapOf("navname" to "${questions[index].problemName}")
                        )
                    }
                } else {
                    showEmptyState()
                }


            }
        }
    }


    override fun onResume() {
        super.onResume()

    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()

        initData()
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}