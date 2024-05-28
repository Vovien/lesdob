package com.tubewiki.home.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.adapter.ImageCommonBannerAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.UMEventKey
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.widget.picture.SelectPhotoUtils
import com.jmbon.widget.recyclerview.CenterLayoutManager
import com.luck.picture.lib.entity.LocalMedia
import com.tubewiki.home.R
import com.tubewiki.home.adapter.StepCategoryAdapter
import com.tubewiki.home.adapter.TubeStepCategoryDetailAdapter
import com.tubewiki.home.bean.TubeStepBeans
import com.tubewiki.home.databinding.ActivityTubeStepBinding
import com.tubewiki.home.viewmodel.MainFragmentViewModel
import com.youth.banner.indicator.CircleIndicator


/**
 * 试管流程页面
 */
@Route(path = "/home/activity/tube_step")
class TubeStepActivity :
    ViewModelActivity<MainFragmentViewModel, ActivityTubeStepBinding>() {
    @Autowired(name = TagConstant.COLUMN_ID)
    @JvmField
    var columnId = 0

    private val categoryAdapter by lazy {
        StepCategoryAdapter()
    }
    private val detailAdapter by lazy {
        TubeStepCategoryDetailAdapter()
    }

    private val bannerAdapter by lazy {
        ImageCommonBannerAdapter{

        }
    }


    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }


    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.tube_step))
        initStateLayout(binding.stateLayout)

        var mCenterLayoutManager =
            CenterLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.recyclerCategory.init(
            categoryAdapter, mCenterLayoutManager
        )

        binding.recyclerDetail.init(
            detailAdapter
        )

        binding.banner.apply {
            indicator = CircleIndicator(context)
            setAdapter(bannerAdapter, true)
        }
        categoryAdapter.setOnItemClickListener { adapter, view, pos ->
            categoryAdapter.selectedPos = pos
            categoryAdapter.notifyDataSetChanged()

            binding.recyclerCategory.smoothScrollToPosition(pos)

            started {
                viewModel.tubeSecondProcessList(categoryAdapter.getItem(pos).id).netCatch {
                    detailAdapter.setNewInstance(mutableListOf())
                }.next {
                    detailAdapter.setNewInstance(data)
                }
            }

            MobAnalysisUtils.mInstance.sendEvent(
                UMEventKey.Event_BuretProcessPage_FirstNav,
                mutableMapOf("navname" to "${categoryAdapter.getItem(pos).title}")
            )


        }
        detailAdapter.setOnItemClickListener { adapter, view, pos ->
            if (detailAdapter.getItem(pos).article == null) {
                ARouter.getInstance().build("/home/activity/empty_article")
                    .navigation()
                return@setOnItemClickListener
            }
            detailAdapter.getItem(pos).article?.let {
                ArouterUtils.toArticleDetailsActivity(it.article_detail?.id ?: 0)
            }
        }

    }


    override fun initData() {
    }


    override fun getData() {

        started {
            viewModel.getGuideBanner().next {
                val banners = this.map {
                    CommonBanner(
                        img = it.cover,
                        identity = it.identity.toString(),
                        item_type = it.itemType
                    )
                }
                bannerAdapter.setDatas(banners)
            }
        }

        started {
            viewModel.tubeProcessList(columnId).netCatch {
                showErrorState()
            }.next {
                showContentState()

                if (top.isNotNullEmpty()) {
                    initTopMenu(top)
                } else {
                    binding.llTop.gone()
                }


                if (firstList.isNotNullEmpty()) {
                    if (columnId == 0) {
                        categoryAdapter.selectedPos = 0
                        categoryAdapter.setNewInstance(firstList)
                        detailAdapter.setNewInstance(secondList)
                        MobAnalysisUtils.mInstance.sendEvent(
                            UMEventKey.Event_BuretProcessPage_FirstNav,
                            mutableMapOf("navname" to "${firstList[0].title}")
                        )

                    } else {

                        var data = firstList.find {
                            it.id == columnId
                        }

                        var index = firstList.indexOf(data)
                        if (index < 0 || index >= firstList.size) {
                            return@next
                        }

                        categoryAdapter.selectedPos = index
                        categoryAdapter.setNewInstance(firstList)
                        detailAdapter.setNewInstance(secondList)
                        binding.recyclerCategory.smoothScrollToPosition(index)
                        MobAnalysisUtils.mInstance.sendEvent(
                            UMEventKey.Event_BuretProcessPage_FirstNav,
                            mutableMapOf("navname" to "${firstList[index].title}")
                        )
                    }
                }

            }
        }

    }

    private fun initTopMenu(top: MutableList<TubeStepBeans.Step>) {

        if (top.size > 0) {
            top[0].apply {
                binding.tvTitle1.text = title
                binding.tvSubTitle1.text = subTitle
                binding.ivCover1.load(icon)


                binding.sclMenu1.setOnSingleClickListener({

                    val media = LocalMedia(img, 0, 0, "")
                    var list = arrayListOf<LocalMedia>()
                    list.add(media)
                    SelectPhotoUtils.externalPicturePreview(this@TubeStepActivity, 0, list)

                    MobAnalysisUtils.mInstance.sendEvent(UMEventKey.Event_BuretProcessPage_TimeCard_Click)
                })


            }
        }

        if (top.size > 1) {
            top[1].apply {
                binding.tvTitle2.text = title
                binding.tvSubTitle2.text = subTitle
                binding.ivCover2.load(icon)

                binding.sclMenu2.setOnSingleClickListener({
                    val media = LocalMedia(img, 0, 0, "")
                    var list = arrayListOf<LocalMedia>()
                    list.add(media)
                    SelectPhotoUtils.externalPicturePreview(this@TubeStepActivity, 0, list)

                    MobAnalysisUtils.mInstance.sendEvent(UMEventKey.Event_BuretProcessPage_FlowChart_Click)
                })

            }
        }
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()

        getData()
    }


}